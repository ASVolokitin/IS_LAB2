package com.ticketis.app.model.asyncImport;

import com.ticketis.app.model.ImportHistoryItem;
import com.ticketis.app.model.Ticket;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "import_batches")
public class ImportBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_history_id", nullable = false)
    private ImportHistoryItem importHistoryItem;

    private Long taskId;

    private List<Ticket> records;
    
    @Min(value = 0)
    private Integer batchNumber;

    @Min(value = 0)
    private Integer totalBatches;

    @Min(value = 0)
    private Integer batchSize;
}