package com.simoale.debitcredit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

// Mapping della relazione one-to-many per cui ad una categoria possono essere associati diversi budget
public class CategoryWithBudgets {
    @Embedded public Category category;
    @Relation(
            parentColumn = "id",
            entityColumn = "categoryId"
    )
    public List<Budget> budgets;
}
