package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

/**
 * Class which represents a transaction with its information
 */
@Entity(tableName = "transaction",
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = "category_id",
                        childColumns = "transaction_category_id",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Wallet.class,
                        parentColumns = "wallet_id",
                        childColumns = "transaction_wallet_id_from",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Wallet.class,
                        parentColumns = "wallet_id",
                        childColumns = "transaction_wallet_id_to",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Payee.class,
                        parentColumns = "payee_id",
                        childColumns = "transaction_payee_id",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE)
        })
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    public int transactionId;
    @ColumnInfo(name = "transaction_amount")
    private float amount;
    @ColumnInfo(name = "transaction_description")
    private String description;
    @ColumnInfo(name = "transaction_category_id")
    private int categoryId;
    @ColumnInfo(name = "transaction_payee_id")
    private int payeeId;
    @ColumnInfo(name = "transaction_date")
    private String date;
    @ColumnInfo(name = "transaction_wallet_id_from")
    private int walletIdFrom;
    @ColumnInfo(name = "transaction_wallet_id_to")
    private int walletIdTo;
    @ColumnInfo(name = "transaction_location")
    private String location;
    @ColumnInfo(name = "transaction_note")
    private String note;
    @ColumnInfo(name = "transaction_image")
    private String image;

    public Transaction(float amount, String description, int categoryId, int payeeId, String date, int walletIdFrom, int walletIdTo,
                       String location, String note, String image) {
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.payeeId = payeeId;
        this.date = date;
        this.walletIdFrom = walletIdFrom;
        this.walletIdTo = walletIdTo;
        this.location = location;
        this.note = note;
        this.image = image;
    }

    public int getId() {
        return transactionId;
    }

    public void setId(int transactionId) {
        this.transactionId = transactionId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWalletIdFrom() {
        return walletIdFrom;
    }

    public void setWalletIdFrom(int walletIdFrom) {
        this.walletIdFrom = walletIdFrom;
    }

    public int getWalletIdTo() {
        return walletIdTo;
    }

    public void setWalletIdTo(int walletIdTo) {
        this.walletIdTo = walletIdTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
