package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a category with its information
 */
@Entity(tableName="category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private int id;
    @ColumnInfo(name = "category_name")
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
