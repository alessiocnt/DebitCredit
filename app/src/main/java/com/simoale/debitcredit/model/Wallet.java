package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents a wallet with its information (name, description, image)
 */
@Entity(tableName="wallet")
public class Wallet {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    private int walletId;
    @ColumnInfo(name = "wallet_name")
    private String name;
    @ColumnInfo(name = "wallet_description")
    private String description;
    @ColumnInfo(name = "wallet_balance")
    private int balance;
    @ColumnInfo(name = "wallet_image")
    private String image;

    public Wallet(String name, String description, int balance, String image) {
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.image = image;
    }

    public int getId() { return walletId; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBalance() {
        return balance;
    }

    public String getImage() {
        return image;
    }

    public void setId(int walletId) {
        this.walletId = walletId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
