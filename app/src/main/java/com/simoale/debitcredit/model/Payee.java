package com.simoale.debitcredit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a category with its information
 */
@Entity(tableName = "payee")
public class Payee {

    @PrimaryKey
    @ColumnInfo(name = "payee_name")
    @NonNull
    private String name;

    public Payee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
