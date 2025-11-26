package com.ticketis.app.service.fileImport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketis.app.exception.importBusinessException.FileImportValidationException;
import com.ticketis.app.exception.importBusinessException.NoImportProcessorException;
import com.ticketis.app.importProcessor.ImportProcessor;
import com.ticketis.app.model.ImportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportProcessorDispatcher {

    private final ObjectMapper objectMapper;
    private final Map<String, ImportProcessor> processorMap;

    @Autowired
    public ImportProcessorDispatcher(ObjectMapper objectMapper,
            List<ImportProcessor> processors) {
        this.objectMapper = objectMapper;
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(
                        p -> p.getEntityType().toLowerCase(),
                        p -> p));
    }
    
    @Transactional(rollbackFor = Exception.class)
    public ImportResult processImport(String filename, String entityType) throws IOException {
        ImportProcessor processor = findProcessor(entityType);
        Path filePath = Paths.get("uploads/import").resolve(filename); // TODO: inject path

        List<JsonNode> entities = parseJsonFile(filePath);

        if (entities.isEmpty()) {
            throw new FileImportValidationException(List.of("No entities found in JSON file"));
        }

        List<String> errors = processor.processImport(entities);

        return ImportResult.builder()
                .processedCount(entities.size())
                .errorCount(errors.size())
                .errors(errors)
                .build();
    }

    private ImportProcessor findProcessor(String entityType) {
        if (entityType == null || entityType.isBlank()) {
            throw new IllegalArgumentException("Entity type is required");
        }

        String normalizedType = entityType.toLowerCase().trim();
        ImportProcessor processor = processorMap.get(normalizedType);

        if (processor == null) {
            throw new NoImportProcessorException(entityType);
        }

        return processor;
    }

    private List<JsonNode> parseJsonFile(Path filePath) throws IOException {
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
}