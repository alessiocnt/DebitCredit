package com.simoale.debitcredit.ui.transactions;

public enum TransactionType {
    IN(1),
    OUT(-1);
    public final int type;

    private TransactionType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
