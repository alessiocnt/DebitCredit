package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

/**
 * Class which represents a wallet with its information (name, description, image)
 */
@Entity(tableName = "routine",
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = "category_id",
                        childColumns = "routine_category_id",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Wallet.class,
                        parentColumns = "wallet_id",
                        childColumns = "routine_wallet_id",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE)
        })
public class Routine {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "routine_id")
    public int routineId;
    @ColumnInfo(name = "routine_name")
    private String name;
    @ColumnInfo(name = "routine_amount")
    private float amount;
    @ColumnInfo(name = "routine_wallet_id")
    private int walletId;
    @ColumnInfo(name = "routine_category_id")
    private int categoryId;
    @ColumnInfo(name = "routine_date")
    private String date;
    @ColumnInfo(name = "routine_last_update")
    public String dateLastUpdate;
    @ColumnInfo(name = "routine_next_update")
    public String dateNextUpdate;
    @ColumnInfo(name = "routine_repeat_number")
    private int repeatNumber;
    @ColumnInfo(name = "routine_repeat_interval")
    private String repeatInterval;

    public Routine(String name, float amount, int walletId, int categoryId, String date, int repeatNumber, String repeatInterval) {
        this.name = name;
        this.amount = amount;
        this.walletId = walletId;
        this.categoryId = categoryId;
        this.date = date;
        this.repeatNumber = repeatNumber;
        this.repeatInterval = repeatInterval;
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
        return routineId;
    }

    public void setId(int routineId) {
        this.routineId = routineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
}
