package com.ticketis.app.dto;

import com.ticketis.app.model.enums.ImportStatus;
import com.ticketis.app.model.enums.WebSocketEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImportProgressEvent extends WebSocketEvent {
    private String taskId;
    private Integer importHistoryId;
    private Integer completedBatches;
    private Integer totalBatches;
    private Long processedRecords;
    private Long totalRecords;
    private Integer errorCount;
    private ImportStatus status;
    private String message;

    public ImportProgressEvent(WebSocketEventType eventType, String taskId) {
        super(eventType, null);
        this.taskId = taskId;
    }
}