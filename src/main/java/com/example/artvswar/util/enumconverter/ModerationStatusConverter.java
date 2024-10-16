package com.example.artvswar.util.enumconverter;

import com.example.artvswar.model.enummodel.ModerationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ModerationStatusConverter implements AttributeConverter<ModerationStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ModerationStatus status) {
        if (status == null) {
            return null;
        }
        return status.getNumber();
    }

    @Override
    public ModerationStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return ModerationStatus.fromNumber(dbData);
    }
}
