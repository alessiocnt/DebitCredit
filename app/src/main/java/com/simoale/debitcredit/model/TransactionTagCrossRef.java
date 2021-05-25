package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "transaction_tag",
        indices = {@Index(value = {"tag_name"})},
        primaryKeys = {"transaction_id", "tag_name"},
        foreignKeys = {
                @ForeignKey(entity = Transaction.class,
                        parentColumns = "transaction_id",
                        childColumns = "transaction_id",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "tag_name",
                        childColumns = "tag_name",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE)
        })
public class TransactionTagCrossRef {

    @ColumnInfo(name = "transaction_id")
    public int crTransactionId;

    @NotNull
    @ColumnInfo(name = "tag_name")
    public String crTagName;

    public TransactionTagCrossRef(int crTransactionId, @NotNull String crTagName) {
        this.crTransactionId = crTransactionId;
        this.crTagName = crTagName;
    }

}
