package com.simoale.debitcredit.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.simoale.debitcredit.model.Budget;
import com.simoale.debitcredit.model.Category;
import com.simoale.debitcredit.model.Payee;
import com.simoale.debitcredit.model.Routine;
import com.simoale.debitcredit.model.Tag;
import com.simoale.debitcredit.model.Transaction;
import com.simoale.debitcredit.model.Wallet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.Volatile;

@Database(entities = {Budget.class, Category.class, Payee.class, Routine.class, Tag.class, Transaction.class, Wallet.class}, version = 1, exportSchema = true)
public abstract class DatabaseInstance extends RoomDatabase {
    public abstract BudgetDAO BudgetDAO();

    public abstract CategoryDAO CategoryDAO();

    public abstract PayeeDAO PayeeDAO();

    public abstract RoutineDAO RoutineDAO();

    public abstract TagDAO TagDAO();

    public abstract TransactionDAO TransactionDAO();

    public abstract WalletDAO WalletDAO();
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile DatabaseInstance INSTANCE;

        public static DatabaseInstance getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseInstance.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseInstance.class, "debit_credit")

                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
