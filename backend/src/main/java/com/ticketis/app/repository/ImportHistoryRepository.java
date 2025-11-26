package com.ticketis.app.repository;

import com.ticketis.app.model.ImportHistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportHistoryRepository extends JpaRepository<ImportHistoryItem, Long> {
    
}
