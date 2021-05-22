package com.simoale.debitcredit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a category with its information
 */
@Entity(tableName = "tag")
public class Tag {

    @PrimaryKey
    @ColumnInfo(name = "tag_name")
    @NonNull
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
