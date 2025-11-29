package com.ticketis.app.dto.jms;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportBatchMessage implements Serializable {
    private Long batchId;
    private Long importHistoryId;
    private String entityType;
    private List<JsonNode> records;
    private int batchNumber;
    private int totalBatches;
    private int totalRecords;
}