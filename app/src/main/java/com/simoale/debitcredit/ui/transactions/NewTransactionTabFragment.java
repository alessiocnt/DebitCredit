package com.simoale.debitcredit.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.simoale.debitcredit.R;

import java.util.ArrayList;
import java.util.List;

public class NewTransactionTabFragment extends Fragment {

    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_transaction_tab, container, false);
        this.view = root;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpTabPager(view);
    }

    private void setUpTabPager(View view) {
        ViewPager viewPager = getActivity().findViewById(R.id.transaction_viewPager_holder);
        PagerAdapter pageAdapter = new PagerAdapter(requireActivity().getSupportFragmentManager(), getTabList());
        viewPager.setAdapter(pageAdapter);
        // Tab switching
        TabLayout tl = view.findViewById(R.id.tabLayout);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    // Used to create a TabList in an odd way
    private List<Fragment> getTabList() {
        List<Fragment> tabList = new ArrayList<>();
        tabList.add(new NewTransactionFragmentOut(TransactionType.OUT));
        tabList.add(new NewTransactionFragmentIn(TransactionType.IN));
        tabList.add(new NewExchangeTransactionFragment());
        return tabList;
    }

}