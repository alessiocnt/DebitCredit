package com.simoale.debitcredit.model;

import android.util.Log;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.simoale.debitcredit.utils.Utilities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName="budget",
    foreignKeys = {
        @ForeignKey(entity = Category.class,
                parentColumns = "category_id",
                childColumns = "budget_category_id",
                onDelete = RESTRICT,
                onUpdate = CASCADE)
    })
public class Budget {
    public enum Interval {
        DAY(1),
        WEEK(7),
        MONTH(31);
        public final int daysNumber;
        private Interval(int daysNumber) {
            this.daysNumber = daysNumber;
        }
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    public int budgetId;
    @ColumnInfo(name = "budget_name")
    public String name;
    @ColumnInfo(name = "budget_category_id")
    public int categoryId;
    @ColumnInfo(name = "budget_limit")
    public float limit;
    @ColumnInfo(name = "budget_date")
    public String date;
    @ColumnInfo(name = "budget_last_update")
    public String dateLastUpdate;
    @ColumnInfo(name = "budget_next_update")
    public String dateNextUpdate;
    @ColumnInfo(name = "budget_repeat_number")
    public int repeatNumber;
    @ColumnInfo(name = "budget_repeat_interval")
    public String repeatInterval;

    public Budget(String name, int categoryId, float limit, String date, String dateLastUpdate, String dateNextUpdate, int repeatNumber, String repeatInterval) {
        this.name = name;
        this.categoryId = categoryId;
        this.limit = limit;
        this.date = date;
        this.dateLastUpdate = dateLastUpdate;
        this.dateNextUpdate = dateNextUpdate;
        this.repeatNumber = repeatNumber;
        this.repeatInterval = repeatInterval;
        updateDates();
    }

    private void updateDates() {
        Log.e("AAA", "dentro");
        long today = Calendar.getInstance().getTime().getTime();
        long last = Utilities.getDateFromString(dateLastUpdate).getTime();
        long diff = today - last;
        long dayDiff = diff * 1000 * 60 * 60 * 24;
        // devo aggiornare ogni repeatNumber*repeatInterval
        int daysBetweenUpdates = repeatNumber * Interval.valueOf(repeatInterval).daysNumber;
        long numberOfUpdates = dayDiff / daysBetweenUpdates;
        if(numberOfUpdates != 0){
            Calendar lastUpdate = Calendar.getInstance();
            lastUpdate.setTime(Utilities.getDateFromString(dateLastUpdate));
            lastUpdate.add(Calendar.DAY_OF_MONTH, (int) (numberOfUpdates * daysBetweenUpdates));
            this.dateLastUpdate = Utilities.getStringFromDate(lastUpdate.getTime());
            Log.e("LastUpdate", this.dateLastUpdate);
            Calendar nextUpdate = Calendar.getInstance();
            nextUpdate.setTime(Utilities.getDateFromString(dateLastUpdate));
            nextUpdate.add(Calendar.DAY_OF_MONTH, daysBetweenUpdates);
            this.dateNextUpdate = Utilities.getStringFromDate(lastUpdate.getTime());
            Log.e("NextUpdate", this.dateNextUpdate);
        }
    }

    public String getDateLastUpdate() {
        return dateLastUpdate;
    }

    public void setDateLastUpdate(String dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public String getDateNextUpdate() {
        return dateNextUpdate;
    }

    public void setDateNextUpdate(String dateNextUpdate) {
        this.dateNextUpdate = dateNextUpdate;
    }

    public int getId() { return budgetId; }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public float getLimit() {
        return limit;
    }

    public String getDate() {
        return date;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setLimit(float limit) { this.limit = limit; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

}



