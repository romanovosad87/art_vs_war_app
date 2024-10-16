package com.example.artvswar.util.enumconverter;

import com.example.artvswar.model.enummodel.PaymentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PaymentStatus status) {
        if (status == null) {
            return null;
        }
        return status.getNumber();
    }

    @Override
    public PaymentStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return PaymentStatus.fromNumber(dbData);
    }
}
