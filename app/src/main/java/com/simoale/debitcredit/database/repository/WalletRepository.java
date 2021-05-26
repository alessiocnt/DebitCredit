package com.simoale.debitcredit.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import com.simoale.debitcredit.database.DatabaseInstance;
import com.simoale.debitcredit.database.WalletDAO;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

public class WalletRepository {
    private WalletDAO walletDAO;
    private LiveData<List<Wallet>> walletList;

    public WalletRepository(Application application) {
        DatabaseInstance db = DatabaseInstance.getDatabase(application);
        walletDAO = db.WalletDAO();
        walletList = walletDAO.getWallets();
    }

    public LiveData<List<Wallet>> getWalletList(){
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
}