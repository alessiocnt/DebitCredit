package com.simoale.debitcredit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "routine_tag",
        indices = {@Index(value = {"tag_name"})},
        primaryKeys = {"routine_id", "tag_name"},
        foreignKeys = {
                @ForeignKey(entity = Routine.class,
                        parentColumns = "routine_id",
                        childColumns = "routine_id",
                        onDelete = CASCADE,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "tag_name",
                        childColumns = "tag_name",
                        onDelete = RESTRICT,
                        onUpdate = CASCADE)
        })

public class RoutineTagCrossRef {

    public RoutineTagCrossRef(int routineId, @NotNull String tagName) {
        this.routineId = routineId;
        this.tagName = tagName;
    }

    @ColumnInfo(name = "routine_id")
    public int routineId;

    @NotNull
    @ColumnInfo(name = "tag_name")
    public String tagName;
}
