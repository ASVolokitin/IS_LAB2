package com.ticketis.app.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ImportResponse {

    private final Instant timestamp = Instant.now();
    private final String filename;
    private final Long fileSize;
    private final String message;
    private final String entityType;
    private Integer processedCount;
    private Integer errorCount;

    private final Long importHistoryId;
    private final boolean async;
    private final Integer totalRecords;

}
