package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a category with its information
 */
@Entity(tableName="payee")
public class Payee {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payee_id")
    private int payeeId;
    @ColumnInfo(name = "payee_name")
    private String name;

    public Payee(String name) {
        this.name = name;
    }

    public int getId() { return payeeId; }

    public String getName() {
        return name;
    }

    public void setId(int payeeId) {
        this.payeeId = payeeId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
