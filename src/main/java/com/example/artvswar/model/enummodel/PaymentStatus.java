package com.example.artvswar.model.enummodel;

public enum PaymentStatus {
    AVAILABLE(0), PROCESSING(10), SOLD(20);

    private final int number;

    PaymentStatus(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static PaymentStatus fromNumber(int number) {
        switch (number) {
            case 0:
                return PaymentStatus.AVAILABLE;
            case 10:
                return PaymentStatus.PROCESSING;
            case 20:
                return PaymentStatus.SOLD;
            default:
                throw new IllegalArgumentException("number '" + number + "' not supported");
        }
    }
}
