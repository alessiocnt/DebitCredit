package com.simoale.debitcredit.ui.wallet;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simoale.debitcredit.database.repository.WalletRepository;
import com.simoale.debitcredit.model.Wallet;

import java.util.List;

public class WalletViewModel extends AndroidViewModel {

    private WalletRepository repository;

    // The wallet list
    private LiveData<List<Wallet>> walletList;
    // The wallet currently selected
    private final MutableLiveData<Wallet> walletSelected = new MutableLiveData<>();

    public WalletViewModel(Application application) {
        super(application);
        repository = new WalletRepository(application);
        walletList = repository.getWalletList();
    }

    public void addWallet(Wallet wallet) {
        repository.addWallet(wallet);
    }

    public LiveData<List<Wallet>> getWalletList() {
        return walletList;
    }

    public Wallet getWallet(int position) {
        return walletList.getValue() == null ? null : walletList.getValue().get(position);
    }

    public void select(Wallet wallet) {
        walletSelected.setValue(wallet);
    }

    public LiveData<Wallet> getSelected() {
        return walletSelected;
    }

    public LiveData<Wallet> getWalletFromName(String walletName) {
        //return repository.getWalletFromName(walletName);
        MutableLiveData<Wallet> selected = new MutableLiveData<>();
        this.walletList.getValue().forEach(w -> {
            if (w.getName().equals(walletName)) {
                selected.setValue(w);
            }
        });
        return selected;
    }

    public void updateBalance(Integer walletId, Float amount) {
        repository.updateBalance(walletId, amount);
    }

}






