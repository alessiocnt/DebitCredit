package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Class which represents a wallet with its information (name, description, image)
 */
@Entity(tableName="budget")
public class Budget {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    private int id;
    @ColumnInfo(name = "budget_name")
    private String name;
    @ColumnInfo(name = "budget_category_id")
    private int categoryId;
    @ColumnInfo(name = "budget_limit")
    private float limit;
    @ColumnInfo(name = "budget_date")
    private Date date;
    @ColumnInfo(name = "budget_repeat_number")
    private int repeatNumber;
    @ColumnInfo(name = "budget_repeat_interval")
    private String repeatInterval;

    public Budget(String name, int categoryId, float limit, Date date, int repeatNumber, String repeatInterval) {
        this.name = name;
        this.categoryId = categoryId;
        this.limit = limit;
        this.date = date;
        this.repeatNumber = repeatNumber;
        this.repeatInterval = repeatInterval;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public float getLimit() {
        return limit;
    }

    public Date getDate() {
        return date;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setLimit(float limit) { this.limit = limit; }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
}
