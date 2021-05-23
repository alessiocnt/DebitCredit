package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "transaction_tag",
        primaryKeys = {"cr_transaction_id", "cr_tag_name"},
        foreignKeys = {
                @ForeignKey(entity = Transaction.class,
                        parentColumns = "transaction_id",
                        childColumns = "cr_transaction_id",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "tag_name",
                        childColumns = "cr_tag_name",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE)
        })
public class TransactionTagCrossRef {

    @ColumnInfo(name = "cr_transaction_id")
    public int crTransactionId;

    @NotNull
    @ColumnInfo(name = "cr_tag_name")
    public String crTagName;
}
