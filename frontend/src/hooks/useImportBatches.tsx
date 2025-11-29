import { useEffect, useState, useCallback } from "react";
import { IMessage } from "@stomp/stompjs";
import { webSocketService } from "../services/webSocketService";
import { getImportBatchesByHistoryItemId } from "../services/api";
import { ImportBatch } from "../interfaces/ImportBatch";
import { devLog } from "../services/logger";

export function useImportBatches() {
  const [batchesByImportId, setBatchesByImportId] = useState<Map<number, ImportBatch[]>>(new Map());
  const [loading, setLoading] = useState<Map<number, boolean>>(new Map());

  const fetchBatches = useCallback(async (importHistoryId: number) => {
    try {
      setLoading(prev => new Map(prev).set(importHistoryId, true));
      devLog.log(`[BATCHES] Fetching batches for import history item ${importHistoryId}`);
      
      const response = await getImportBatchesByHistoryItemId(importHistoryId);
      const batchesData = response.data;
      const batches: ImportBatch[] = Array.isArray(batchesData) 
        ? batchesData 
        : Object.values(batchesData);
      
      setBatchesByImportId(prev => {
        const newMap = new Map(prev);
        newMap.set(importHistoryId, batches);
        return newMap;
      });
      
      devLog.log(`[BATCHES] Fetched ${batches.length} batch(es) for import history item ${importHistoryId}`);
    } catch (err: any) {
      devLog.error(`[BATCHES] Error fetching batches for import ${importHistoryId}:`, err);
    } finally {
      setLoading(prev => {
        const newMap = new Map(prev);
        newMap.set(importHistoryId, false);
        return newMap;
      });
    }
  }, []);

  const handleWebSocketMessage = useCallback((message: IMessage) => {
    try {
      const body = JSON.parse(message.body);
      devLog.log(`[BATCHES] Received websocket event:`, body);

      let importHistoryId: number | null = null;

      if (body.importHistoryId !== undefined) {
        importHistoryId = typeof body.importHistoryId === 'number' 
          ? body.importHistoryId 
          : parseInt(body.importHistoryId, 10);
      } else if (body.entityId !== undefined) {
        importHistoryId = typeof body.entityId === 'number' 
          ? body.entityId 
          : parseInt(body.entityId, 10);
      }

      if (importHistoryId !== null && !isNaN(importHistoryId)) {
        devLog.log(`[BATCHES] Refetching batches for import history item ${importHistoryId}`);
        fetchBatches(importHistoryId);
      } else {
        devLog.warn(`[BATCHES] Could not extract importHistoryId from websocket event:`, body);
      }
    } catch (err: any) {
      devLog.error(`[BATCHES] Error parsing websocket message:`, err);
    }
  }, [fetchBatches]);

  useEffect(() => {
    devLog.log(`[BATCHES] Subscribing to /topic/import_history`);
    const subscription = webSocketService.subscribe("/topic/import_history", handleWebSocketMessage);
    
    return () => {
      devLog.log(`[BATCHES] Unsubscribing from /topic/import_history`);
      subscription.unsubscribe();
    };
  }, [handleWebSocketMessage]);


  const getBatches = useCallback((importHistoryId: number): ImportBatch[] => {
    return batchesByImportId.get(importHistoryId) || [];
  }, [batchesByImportId]);

  const isLoading = useCallback((importHistoryId: number): boolean => {
    return loading.get(importHistoryId) || false;
  }, [loading]);


  const refreshBatches = useCallback((importHistoryId: number) => {
    fetchBatches(importHistoryId);
  }, [fetchBatches]);

  return {
    getBatches,
    isLoading,
    refreshBatches,
    batchesByImportId,
  };
}

