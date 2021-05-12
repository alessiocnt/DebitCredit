package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Class which represents a wallet with its information (name, description, image)
 */
@Entity(tableName="routine")
public class Routine {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "routine_id")
    private int id;
    @ColumnInfo(name = "routine_name")
    private String name;
    @ColumnInfo(name = "routine_amount")
    private float amount;
    @ColumnInfo(name = "routine_wallet_id")
    private int walletId;
    @ColumnInfo(name = "routine_category_id")
    private int categoryId;
    @ColumnInfo(name = "routine_date")
    private Date date;
    @ColumnInfo(name = "routine_repeat_number")
    private int repeatNumber;
    @ColumnInfo(name = "routine_repeat_interval")
    private String repeatInterval;

    public Routine(String name, float amount, int walletId, int categoryId, Date date, int repeatNumber, String repeatInterval) {
        this.name = name;
        this.amount = amount;
        this.walletId = walletId;
        this.categoryId = categoryId;
        this.date = date;
        this.repeatNumber = repeatNumber;
        this.repeatInterval = repeatInterval;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public int getWalletId() {
        return walletId;
    }

    public int getCategoryId() {
        return categoryId;
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

    public void setAmount(float amount) { this.amount = amount; }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

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
