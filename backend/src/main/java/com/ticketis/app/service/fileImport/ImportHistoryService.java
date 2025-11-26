package com.ticketis.app.service.fileImport;

import com.ticketis.app.exception.notfoundexception.FileImportRecordNotFoundException;
import com.ticketis.app.model.ImportHistoryItem;
import com.ticketis.app.model.enums.ImportStatus;
import com.ticketis.app.repository.ImportHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImportHistoryService {

    private final ImportHistoryRepository importHistoryRepository;

    public Page<ImportHistoryItem> getImportsPage(Pageable pageable) {
        return importHistoryRepository.findAll(pageable);
    }

    private ImportHistoryItem getImportItemById(Long id) {
        return importHistoryRepository.findById(id)
                .orElseThrow(() -> new FileImportRecordNotFoundException(id));
    }

    public ImportHistoryItem createPendingImport(String originalFilename, String entityType) {
        ImportHistoryItem importItem = new ImportHistoryItem();
        importItem.setFilename(originalFilename);
        importItem.setImportStatus(ImportStatus.PENDING);
        importItem.setEntityType(entityType);
        importItem.setResultDescription("Import initiated");

        return importHistoryRepository.save(importItem);
    }

    public void updateStatus(Long importId, ImportStatus status, String description) {
        ImportHistoryItem item = getImportItemById(importId);
        item.setImportStatus(status);
        item.setResultDescription(description != null ? description : status.toString());
    }

    public void updateWithResult(Long importId, ImportStatus status, int processed, int errors, String description) {
        ImportHistoryItem item = getImportItemById(importId);
        item.setImportStatus(status);
        String resultDesc = String.format("%s. Processed: %d, Errors: %d",
                description, processed, errors);
        item.setResultDescription(resultDesc);
    }
}