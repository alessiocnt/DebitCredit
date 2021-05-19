package com.simoale.debitcredit.model;

public enum Interval {
    DAY(1),
    WEEK(7),
    MONTH(31),
    YEAR(365);
    public final int daysNumber;

    private Interval(int daysNumber) {
        this.daysNumber = daysNumber;
    }
}
