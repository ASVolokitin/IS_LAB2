package com.ticketis.app.controller;

import com.ticketis.app.service.fileImport.ImportBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/import-batches")
public class ImportBatchController {

    private final ImportBatchService batchService;

    @GetMapping("/{history_item_id}")
    public ResponseEntity<?> getAllBatchesByHistoryItemId(@PathVariable Long history_item_id) {
        return new ResponseEntity<>(batchService.getMapBatchesByImportHistoryItemId(history_item_id),  HttpStatus.OK);
    }
}
