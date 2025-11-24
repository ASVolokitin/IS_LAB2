package com.ticketis.app.converter;

import com.ticketis.app.model.enums.ImportStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ImportStatusConverter implements AttributeConverter<ImportStatus, String> {

    @Override
    public String convertToDatabaseColumn(ImportStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public ImportStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return ImportStatus.valueOf(dbData);
    }
}