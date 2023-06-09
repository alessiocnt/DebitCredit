package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "budget",
        indices = {@Index(value = {"budget_category_name"})},
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = "category_name",
                        childColumns = "budget_category_name",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE)
        })
public class Budget {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    public int budgetId;
    @ColumnInfo(name = "budget_name")
    public String name;
    @ColumnInfo(name = "budget_category_name")
    public String categoryName;
    @ColumnInfo(name = "budget_limit")
    public float limit;
    @ColumnInfo(name = "budget_date")
    public String date;
    @ColumnInfo(name = "budget_last_update")
    public String dateLastUpdate;
    @ColumnInfo(name = "budget_next_update")
    public String dateNextUpdate;
    @ColumnInfo(name = "budget_repeat_number")
    public int repeatNumber;
    @ColumnInfo(name = "budget_repeat_interval")
    public String repeatInterval;
    @ColumnInfo(name = "budget_current_amount")
    public float currentAmount;

    public Budget(String name, String categoryName, float limit, String date, String dateLastUpdate, String dateNextUpdate, int repeatNumber, String repeatInterval, float currentAmount) {
        this.name = name;
        this.categoryName = categoryName;
        this.limit = limit;
        this.date = date;
        this.dateLastUpdate = dateLastUpdate;
        this.dateNextUpdate = dateNextUpdate;
        this.repeatNumber = repeatNumber;
        this.repeatInterval = repeatInterval;
        this.currentAmount = currentAmount;
    }


    public String getDateLastUpdate() {
        return dateLastUpdate;
    }

    public void setDateLastUpdate(String dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public String getDateNextUpdate() {
        return dateNextUpdate;
    }

    public void setDateNextUpdate(String dateNextUpdate) {
        this.dateNextUpdate = dateNextUpdate;
    }

    public int getId() {
        return budgetId;
    }

    public void setId(int budgetId) {
        this.budgetId = budgetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public float getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(float currentAmount) {
        this.currentAmount = currentAmount;
    }


}



