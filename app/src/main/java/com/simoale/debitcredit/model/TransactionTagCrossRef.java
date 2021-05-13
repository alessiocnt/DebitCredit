package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName="transaction_tag",
    primaryKeys = {"transaction_id, tag_id"},
    foreignKeys = {
        @ForeignKey(entity = Transaction.class,
                parentColumns = "transaction_id",
                childColumns = "transaction_id",
                onDelete = RESTRICT,
                onUpdate = CASCADE),
        @ForeignKey(entity = Tag.class,
                parentColumns = "tag_id",
                childColumns = "tag_id",
                onDelete = RESTRICT,
                onUpdate = CASCADE)
    })
public class TransactionTagCrossRef {

    @ColumnInfo(name = "transaction_id")
    public int transactionId;
    @ColumnInfo(name = "tag_id")
    public int tagId;
}
