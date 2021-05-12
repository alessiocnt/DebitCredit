package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a category with its information
 */
@Entity(tableName="tag")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    private int tagId;
    @ColumnInfo(name = "tag_name")
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public int getId() { return tagId; }

    public String getName() {
        return name;
    }

    public void setId(int tagId) {
        this.tagId = tagId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
