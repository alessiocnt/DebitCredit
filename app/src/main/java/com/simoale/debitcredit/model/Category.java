package com.simoale.debitcredit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a category with its information
 */
@Entity(tableName = "category")
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "category_name")
    @NonNull
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
