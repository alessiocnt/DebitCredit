package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName="transaction_tag", primaryKeys = {"transaction_id, tag_id"})
public class TransactionTagCrossRef {

    @ColumnInfo(name = "transaction_id")
    public int transactionId;
    @ColumnInfo(name = "tag_id")
    public int tagId;
}
