package com.ticketis.app.importProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public interface ImportProcessor {
    
    List<String> processImport(List<JsonNode> entities);
    
    String getEntityType();
}

