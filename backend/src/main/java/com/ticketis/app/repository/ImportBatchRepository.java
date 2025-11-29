package com.ticketis.app.repository;

import com.ticketis.app.dto.jms.ImportBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportBatchRepository extends JpaRepository<ImportBatchEntity, Long> {
    
    List<ImportBatchEntity> findByImportHistoryItemId(Long importHistoryItemId);
}
