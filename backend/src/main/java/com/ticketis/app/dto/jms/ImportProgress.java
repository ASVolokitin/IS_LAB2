package com.ticketis.app.dto.jms;

import com.ticketis.app.model.enums.BatchStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ImportProgress {
    private final Long importHistoryId;
    private final int totalBatches;
    private final int totalRecords;
    private final Map<Long, BatchProgress> batches = new ConcurrentHashMap<>();
    private final Instant startTime = Instant.now();
    
    public void updateBatch(Long batchId, int processedRecords, int errorCount, BatchStatus status) {
        BatchProgress batchProgress = new BatchProgress(batchId, processedRecords, errorCount, status);
        batches.put(batchId, batchProgress);
    }
    
    public boolean isCompleted() {
        return batches.values().stream()
                .allMatch(bp -> bp.getStatus() == BatchStatus.SUCCESS || bp.getStatus() == BatchStatus.FAILED)
                && batches.size() == totalBatches;
    }
    
    public boolean isFailed() {
        return batches.values().stream()
                .anyMatch(bp -> bp.getStatus() == BatchStatus.FAILED)
                && isCompleted();
    }
    
    public int getCompletedBatches() {
        return (int) batches.values().stream()
                .filter(bp -> bp.getStatus() == BatchStatus.SUCCESS || bp.getStatus() == BatchStatus.FAILED)
                .count();
    }
    
    public int getTotalProcessed() {
        return batches.values().stream()
                .mapToInt(BatchProgress::getProcessedRecords)
                .sum();
    }
    
    public int getTotalErrors() {
        return batches.values().stream()
                .mapToInt(BatchProgress::getErrorCount)
                .sum();
    }
}