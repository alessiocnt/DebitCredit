package com.simoale.debitcredit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.simoale.debitcredit.model.Wallet;

import java.util.List;

@Dao
public interface WalletDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addWallet(Wallet wallet);

    @Transaction
    @Query("SELECT * from wallet ORDER BY wallet_name")
    LiveData<List<Wallet>> getWallets();

    @Transaction
    @Query("UPDATE wallet SET wallet_balance =  wallet_balance + :amount WHERE wallet_id = :walletId")
    void updateBalance(Integer walletId, Float amount);

    @Delete
    void deleteWallet(Wallet wallet);
}
