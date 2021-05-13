package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName="routine_tag",
    primaryKeys = {"routine_id, tag_id"},
    foreignKeys = {
        @ForeignKey(entity = Transaction.class,
                parentColumns = "routine_id",
                childColumns = "routine_id",
                onDelete = RESTRICT,
                onUpdate = CASCADE),
        @ForeignKey(entity = Tag.class,
                parentColumns = "tag_id",
                childColumns = "tag_id",
                onDelete = RESTRICT,
                onUpdate = CASCADE)
    })
public class RoutineTagCrossRef {

    @ColumnInfo(name = "routine_id")
    public int routineId;
    @ColumnInfo(name = "tag_id")
    public int tagId;
}
