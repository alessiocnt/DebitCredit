package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.WalletDAO;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WalletRepository {
    private WalletDAO walletDAO;
    private LiveData<List<Wallet>> walletList;

    public WalletRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        walletDAO = db.WalletDAO();
        walletList = walletDAO.getWallets();
    }

    public LiveData<List<Wallet>> getWalletList() {
        return walletList;
    }

    public void addWallet(final Wallet wallet) {
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                walletDAO.addWallet(wallet);
            }
        });
    }

    public void updateBalance(Integer walletId, Float amount) {
        DatabaseInstance.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                walletDAO.updateBalance(walletId, amount);
            }
        });
    }

    public boolean deleteWallet(Wallet wallet) {
        AtomicBoolean ret = new AtomicBoolean(true);
        DatabaseInstance.databaseWriteExecutor.execute(() -> {
            try {
                walletDAO.deleteWallet(wallet);
            } catch (Exception e) {
                ret.set(false);
            }
        });
        try {
            DatabaseInstance.databaseWriteExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret.get();
    }
}