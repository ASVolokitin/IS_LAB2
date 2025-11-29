package com.ticketis.app.dto.jms;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.ticketis.app.converter.JsonNodeListConverter;
import com.ticketis.app.model.ImportHistoryItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "import_batches")
public class ImportBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "import_id", nullable = false)
    private ImportHistoryItem importHistoryItem;

    @Min(value = 0)
    @Column(name = "batch_number")
    private Integer batchNumber;

    @Min(value = 0)
    @Column(name = "batch_size")
    private Integer batchSize;

    @Column(name = "batch_status", length = 255)
    private String batchStatus;

    @Convert(converter = JsonNodeListConverter.class)
    @Column(name = "records", columnDefinition = "TEXT")
    private List<JsonNode> records;

    @Min(value = 0)
    @Column(name = "total_records")
    private Integer totalRecords;

    @Min(value = 0)
    @Column(name = "processed_records")
    private Integer processedRecords;

}