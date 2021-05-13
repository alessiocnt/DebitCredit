package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName="transaction_tag",
    primaryKeys = {"transaction_id, tag_id"},
    foreignKeys = {
        @ForeignKey(entity = Transaction.class,
                parentColumns = "transactionId",
                childColumns = "transactionId",
                onDelete = RESTRICT,
                onUpdate = CASCADE),
        @ForeignKey(entity = Tag.class,
                parentColumns = "tagId",
                childColumns = "tagId",
                onDelete = RESTRICT,
                onUpdate = CASCADE)
    })
public class TransactionTagCrossRef {

    @ColumnInfo(name = "transaction_id")
    public int transactionId;
    @ColumnInfo(name = "tag_id")
    public int tagId;
}
