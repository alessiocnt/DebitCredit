package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

/**
 * Class which represents a transaction with its information
 */
@Entity(tableName="transaction",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = RESTRICT,
                onUpdate = CASCADE))
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    private int transactionId;
    @ColumnInfo(name = "transaction_amount")
    private float amount;
    @ColumnInfo(name = "transaction_description")
    private String description;
    @ColumnInfo(name = "transaction_category_id")
    private int categoryId;
    @ColumnInfo(name = "transaction_payee_id")
    private int payeeId;
    @ColumnInfo(name = "transaction_date")
    private Date date;
    @ColumnInfo(name = "transaction_wallet_id_from")
    private int walletIdFrom;
    @ColumnInfo(name = "transaction_wallet_id_to")
    private int walletIdTo;
    @ColumnInfo(name = "transaction_tag_id")
    private int tagId;
    @ColumnInfo(name = "transaction_location")
    private String location;
    @ColumnInfo(name = "transaction_note")
    private String note;
    @ColumnInfo(name = "transaction_image")
    private String image;

    public Transaction(float amount, String description, int categoryId, int payeeId, Date date, int walletIdFrom, int walletIdTo,
                       int tagId, String location, String note, String image) {
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.payeeId = payeeId;
        this.date = date;
        this.walletIdFrom = walletIdFrom;
        this.walletIdTo = walletIdTo;
        this.tagId = tagId;
        this.location = location;
        this.note = note;
        this.image = image;
    }

    public int getId() {
        return transactionId;
    }

    public float getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public Date getDate() {
        return date;
    }

    public int getWalletIdFrom() {
        return walletIdFrom;
    }

    public int getWalletIdTo() {
        return walletIdTo;
    }

    public int getTagId() {
        return tagId;
    }

    public String getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }

    public String getImage() {
        return image;
    }

    public void setId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWalletIdFrom(int walletIdFrom) {
        this.walletIdFrom = walletIdFrom;
    }

    public void setWalletIdTo(int walletIdTo) {
        this.walletIdTo = walletIdTo;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
