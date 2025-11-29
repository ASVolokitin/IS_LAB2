export interface ImportBatch {
    id: number;
    batchNumber: number;
    batchSize: number;
    batchStatus: string;
    totalRecords: number;
    processedRecords: number;
    records?: any[];
}








