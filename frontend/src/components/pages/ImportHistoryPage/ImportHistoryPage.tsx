import { useEffect, useState, useRef } from "react";
import { useEntities } from "../../../hooks/useEntities";
import { ImportHistoryTable } from "../../elements/ImportTable/ImportHistoryTable";
import NavBar from "../../elements/NavBar/NavBar"
import { ImportHistoryItem } from "../../../interfaces/ImportHistoryItem";
import { PageNav } from "../../elements/PageNav/PageNav";
import { useImportBatches } from "../../../hooks/useImportBatches";
import { uploadImportFile } from "../../../services/api";
import { Notification } from "../../elements/Notification/Notification";
import "./ImportHistoryPage.css";


export const ImportHistoryPage = () => {

    const [pageNumber, setPageNumber] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(5);
    const [maxPageValue, setMaxPageValue] = useState<number>(0);
    const [uploadLoading, setUploadLoading] = useState<boolean>(false);
    const [uploadError, setUploadError] = useState<string | null>(null);
    const [uploadSuccess, setUploadSuccess] = useState<string | null>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const { entities: hookedEntities, serverError, setServerError, entitiesAmount, refreshEntities } = useEntities<ImportHistoryItem>("import_history", pageNumber, pageSize);
    const { getBatches, isLoading, refreshBatches } = useImportBatches();

    const handlePageChange = (page: number) => {
        const minPageValue = 0;
        setPageNumber(Math.max(Math.min(page, maxPageValue), minPageValue));
    };

    useEffect(() => {
        setMaxPageValue(
            Math.floor(
                hookedEntities
                    ? entitiesAmount / pageSize -
                    Number(!Boolean(entitiesAmount % pageSize))
                    : 0
            )
        );
    }, [hookedEntities, pageSize]);

    const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) return;

        setUploadLoading(true);
        setUploadError(null);
        setUploadSuccess(null);

        try {
            const response = await uploadImportFile(file);
            setUploadSuccess(`File "${file.name}" uploaded successfully. Import started.`);
            if (fileInputRef.current) {
                fileInputRef.current.value = '';
            }
            setTimeout(() => {
                refreshEntities();
            }, 1000);
        } catch (err: any) {
            const errorMessage = err.response?.data?.message || err.message || "Failed to upload file";
            setUploadError(errorMessage);
        } finally {
            setUploadLoading(false);
        }
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

    return (
        <>
            <NavBar />
            <ImportHistoryTable 
                imports={hookedEntities} 
                getBatches={getBatches}
                isLoading={isLoading}
                refreshBatches={refreshBatches}
            />
            <div className="import-upload-container">
                <input
                    ref={fileInputRef}
                    type="file"
                    accept=".json"
                    onChange={handleFileSelect}
                    style={{ display: 'none' }}
                />
                <button
                    onClick={handleUploadClick}
                    disabled={uploadLoading}
                    className="upload-import-button"
                >
                    {uploadLoading ? "Uploading..." : "Upload Import File"}
                </button>
            </div>
            <PageNav page={pageNumber} size={pageSize} onPageChange={handlePageChange} entitiesAmount={entitiesAmount} />
            {uploadError && (
                <Notification
                    message={uploadError}
                    type="error"
                    isVisible={true}
                    onClose={() => setUploadError(null)}
                />
            )}
            {uploadSuccess && (
                <Notification
                    message={uploadSuccess}
                    type="success"
                    isVisible={true}
                    onClose={() => setUploadSuccess(null)}
                />
            )}
        </>
    )
}