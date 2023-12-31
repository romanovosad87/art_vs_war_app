package com.example.artvswar.model.enummodel;

public enum ModerationStatus {
    REJECTED(0), PENDING(10), APPROVED(20);

    private final int number;

    ModerationStatus(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static ModerationStatus fromNumber(int number) {
        switch (number) {
            case 0:
                return ModerationStatus.REJECTED;
            case 10:
                return ModerationStatus.PENDING;
            case 20:
                return ModerationStatus.APPROVED;
            default:
                throw new IllegalArgumentException("number '" + number + "' not supported");
        }
    }
}
