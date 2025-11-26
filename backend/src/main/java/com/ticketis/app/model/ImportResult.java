package com.ticketis.app.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class ImportResult {
    private Integer processedCount;
    private Integer errorCount;
    private List<String> errors;
    
    public ImportResult(Integer processedCount, Integer errorCount, List<String> errors) {
        this.processedCount = processedCount;
        this.errorCount = errorCount;
        this.errors = errors != null ? errors : Collections.emptyList();
    }
}
