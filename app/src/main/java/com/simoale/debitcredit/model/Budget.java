package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName="budget",
    foreignKeys = {
        @ForeignKey(entity = Category.class,
                parentColumns = "category_id",
                childColumns = "budget_category_id",
                onDelete = RESTRICT,
                onUpdate = CASCADE)
    })
public class Budget {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    public int budgetId;
    @ColumnInfo(name = "budget_name")
    public String name;
    @ColumnInfo(name = "budget_category_id")
    public int categoryId;
    @ColumnInfo(name = "budget_limit")
    public float limit;
    @ColumnInfo(name = "budget_date")
    public String date;
    @ColumnInfo(name = "budget_repeat_number")
    public int repeatNumber;
    @ColumnInfo(name = "budget_repeat_interval")
    public String repeatInterval;

    public Budget(String name, int categoryId, float limit, String date, int repeatNumber, String repeatInterval) {
        this.name = name;
        this.categoryId = categoryId;
        this.limit = limit;
        this.date = date;
        this.repeatNumber = repeatNumber;
        this.repeatInterval = repeatInterval;
    }

    public int getId() { return budgetId; }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public float getLimit() {
        return limit;
    }

    public String getDate() {
        return date;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setLimit(float limit) { this.limit = limit; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
}
