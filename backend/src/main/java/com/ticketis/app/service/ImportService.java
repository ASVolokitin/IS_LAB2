package com.ticketis.app.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketis.app.dto.response.ImportResponse;
import com.ticketis.app.exception.FailedToProcessImportException;
import com.ticketis.app.exception.FailedToUploadFileException;
import com.ticketis.app.exception.FileImportValidationException;
import com.ticketis.app.exception.NoImportProcessorException;
import com.ticketis.app.exception.UnableToGetNecessaryFieldException;
import com.ticketis.app.importProcessor.ImportProcessor;
import com.ticketis.app.model.ImportHistoryItem;
import com.ticketis.app.model.Ticket;
import com.ticketis.app.model.enums.ImportStatus;
import com.ticketis.app.repository.ImportHistoryRepository;
import com.ticketis.app.specification.GenericSpecification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportService {

    @Value("${app.import.upload-dir:uploads/import}")
    private String uploadDir;

    private final ObjectMapper objectMapper;
    private final List<ImportProcessor> importProcessors;

    private final ImportHistoryRepository importHistoryRepository;

    public Page<ImportHistoryItem> getImportsPage(Pageable pageable) {
        return importHistoryRepository.findAll(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public ImportResponse uploadFile(MultipartFile file, String entityType) {

        ImportHistoryItem importItem = createPendingImportItem(file.getOriginalFilename());

        validateFile(file);

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);

            if (!"json".equalsIgnoreCase(fileExtension)) {
                throw new IllegalArgumentException("Only JSON files are supported for import");
            }

            String uniqueFilename = importItem.getFilename();
            Path filePath = uploadPath.resolve(uniqueFilename);
            importItem.setFilename(uniqueFilename);
            importHistoryRepository.save(importItem);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File uploaded successfully: {}", uniqueFilename);

            String message = processImportFile(filePath, entityType);

            updateImportStatus(importItem.getId(), ImportStatus.SUCCESS);

            return new ImportResponse(
                    uniqueFilename,
                    file.getSize(),
                    message,
                    entityType);

        } catch (NoImportProcessorException e) {
            String errorStatus = String.format("No processor found for entity type: %s", entityType);
            handleImportError(importItem, errorStatus, e);
            throw e;
        } catch (FileImportValidationException e) {
            handleImportError(importItem, e.getMessage(), e);
            throw e;
        } catch (UnableToGetNecessaryFieldException e) {
            handleImportError(importItem, e.getMessage(), e);
            throw e;
        } catch (IllegalArgumentException e) {
            String errorStatus = String.format("Invalid argument: %s", e.getMessage());
            handleImportError(importItem, errorStatus, e);
            throw e;
        } catch (JsonProcessingException e) {
            String errorStatus = "JSON processing error";
            handleImportError(importItem, errorStatus, e);
            throw new RuntimeException("JSON processing error", e);
        } catch (IOException e) {
            String errorStatus = "Error uploading file";
            handleImportError(importItem, errorStatus, e);
            throw new FailedToUploadFileException(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof JsonProcessingException) {
                JsonProcessingException jsonEx = (JsonProcessingException) e.getCause();
                String errorStatus = "JSON parsing error";
                handleImportError(importItem, errorStatus, jsonEx);
            } else {
                String errorStatus = "Unexpected error during import";
                handleImportError(importItem, errorStatus, e);
            }
            throw new FailedToProcessImportException(e.getMessage());
        }
    }

    private String processImportFile(Path filePath, String entityType)
            throws IOException, JsonProcessingException, FileImportValidationException,
            UnableToGetNecessaryFieldException, NoImportProcessorException {
        log.info("Processing import file: {} for entity type: {}", filePath, entityType);

        List<JsonNode> entities = parseJsonFile(filePath);

        if (entities.isEmpty()) {
            throw new IllegalArgumentException("No entities found in JSON file");
        }

        ImportProcessor processor = findProcessor(entityType);
        if (processor == null)
            throw new NoImportProcessorException(entityType);

        List<String> errors = processor.processImport(entities);

        if (!errors.isEmpty()) {
            String errorMessage = "Import completed with errors: " + String.join("; ", errors);
            log.warn(errorMessage);
            throw new FileImportValidationException(errors);
        }

        return String.format("Successfully imported %d %s(s)", entities.size(), entityType);
    }

    private List<JsonNode> parseJsonFile(Path filePath) throws IOException, JsonProcessingException {
        log.info("Parsing JSON file: {}", filePath);

        String content = Files.readString(filePath);

        JsonNode rootNode = objectMapper.readTree(content);

        if (rootNode.isArray()) {
            List<JsonNode> nodes = new ArrayList<>();
            for (JsonNode node : rootNode) {
                nodes.add(node);
            }
            log.info("Parsed {} entities from JSON array", nodes.size());
            return nodes;
        } else {
            List<JsonNode> nodes = new ArrayList<>();
            nodes.add(rootNode);
            log.info("Parsed single entity from JSON object");
            return nodes;
        }
    }

    private ImportProcessor findProcessor(String entityType) {
        if (entityType == null || entityType.isBlank()) {
            return null;
        }

        String normalizedType = entityType.toLowerCase().trim();

        Map<String, ImportProcessor> processorMap = importProcessors.stream()
                .collect(Collectors.toMap(
                        p -> p.getEntityType().toLowerCase(),
                        p -> p));

        return processorMap.get(normalizedType);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() == 0) {
            throw new IllegalArgumentException("File size cannot be zero");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private void handleImportError(ImportHistoryItem importItem, String errorMessage, Exception e) {
        log.error(errorMessage, e);
        updateImportStatusWithError(importItem.getId(), e.getMessage());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportHistoryItem createPendingImportItem(String originalFilename) {
        ImportHistoryItem importItem = new ImportHistoryItem();
        importItem.setImportStatus(ImportStatus.PENDING);
        importItem.setFilename(UUID.randomUUID().toString() + "_" + originalFilename);
        ImportHistoryItem saved = importHistoryRepository.save(importItem);
        importHistoryRepository.flush();
        return saved;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateImportStatus(Long id, ImportStatus status) {
        ImportHistoryItem item = importHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Import record not found"));
        item.setImportStatus(status);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateImportStatusWithError(Long id, String errorMessage) {
        System.out.println("Error!!!vjjj");
        ImportHistoryItem item = importHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Import record not found"));
        item.setImportStatus(ImportStatus.FAILED);
        item.setResultDescription(errorMessage);
    }
}
