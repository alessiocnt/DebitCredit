package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a wallet with its information (name, description, image)
 */
@Entity(tableName = "wallet")
public class Wallet {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    public int walletId;
    @ColumnInfo(name = "wallet_name")
    private String name;
    @ColumnInfo(name = "wallet_description")
    private String description;
    @ColumnInfo(name = "wallet_balance")
    private float balance;
    @ColumnInfo(name = "wallet_image")
    private String image;

    public Wallet(String name, String description, float balance, String image) {
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.image = image;
    }

    public int getId() {
        return walletId;
    }

    public void setId(int walletId) {
        this.walletId = walletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
