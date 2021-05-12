package com.simoale.debitcredit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

// Mapping della relazione one-to-many per cui ad una categoria possono essere associate diverse routine
public class WalletWithRoutines {
    @Embedded public Category category;
    @Relation(
            parentColumn = "id",
            entityColumn = "walletId"
    )
    public List<Routine> routines;
}
