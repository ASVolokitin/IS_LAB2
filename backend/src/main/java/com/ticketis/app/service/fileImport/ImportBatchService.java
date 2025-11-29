package com.ticketis.app.service.fileImport;

import com.ticketis.app.dto.jms.ImportBatchEntity;
import com.ticketis.app.exception.notfoundexception.FileImportRecordNotFoundException;
import com.ticketis.app.repository.ImportBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportBatchService {

    private final ImportBatchRepository batchRepository;

    public ImportBatchEntity createBatch(ImportBatchEntity batch) {
        return batchRepository.save(batch);
    }

    public ImportBatchEntity getBatchById(Long id) {
         return batchRepository.findById(id)
                .orElseThrow(() -> new FileImportRecordNotFoundException(id));
    }

    public Map<Long, ImportBatchEntity> getMapBatchesByImportHistoryItemId(Long importHistoryItemId) {
        return batchRepository.findByImportHistoryItemId(importHistoryItemId)
                .stream()
                .collect(Collectors.toMap(ImportBatchEntity::getId, batch -> batch));
    }

    public List<ImportBatchEntity> getBatchesByImportHistoryItemId(Long importHistoryItemId) {
        return batchRepository.findByImportHistoryItemId(importHistoryItemId);
    }

    public ImportBatchEntity saveBatch(ImportBatchEntity batch) {
        return batchRepository.save(batch);
    }
    
}
