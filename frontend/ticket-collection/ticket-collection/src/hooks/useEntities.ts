import { useCallback, useEffect, useState } from "react";
import { getAllTickets, getCoordinates, getEvents, getLocations, getPersons, getTicketsPage, getVenues } from "../services/api";
import { EntityType } from "../types/ConnectedObject";
import { webSocketService } from "../services/webSocketService";
import { SortOrder } from "../types/SortOrder";


export function useEntities<T>(
  entityType: EntityType,
  pageNumber: number,
  pageSize: number,
  sortField?: string,
  sortOrder?: SortOrder
) {
  const [entities, setEntities] = useState<T[]>([]);
  const [serverError, setServerError] = useState<string | null>();
  const [entitiesAmount, setEntitiesAmount] = useState<number>(-1);

  const refreshEntities = useCallback(async () => {
    if (!entityType) return;
    try {
      let response;
      if (entityType === "tickets") {
        const pageResponse = await getTicketsPage(pageNumber, pageSize, sortField, sortOrder);
        setEntitiesAmount(pageResponse.data.totalElements);
        response = { data: pageResponse.data.content };
      }
      setEntities(response?.data);
    } catch (err: any) {
      setServerError(err.response?.data?.message || "Error");
      setEntities([]);
    }
  }, [entityType, pageNumber, pageSize, sortField, sortOrder]);

  useEffect(() => {
    refreshEntities();
  }, [refreshEntities]);

  useEffect(() => {
    webSocketService.connect();
    const subscription = webSocketService.subscribe(`/topic/${entityType}`, (message) => {
      refreshEntities();
    });
    return () => subscription.unsubscribe();
  }, [entityType, refreshEntities]);

  return { entities, entitiesAmount, serverError, setServerError };
}

