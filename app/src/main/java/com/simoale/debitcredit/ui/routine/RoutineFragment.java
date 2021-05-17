package com.simoale.debitcredit.ui.routine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simoale.debitcredit.R;
import com.simoale.debitcredit.recyclerView.OnItemListener;
import com.simoale.debitcredit.recyclerView.RoutineCardAdapter;

public class RoutineFragment extends Fragment implements OnItemListener {

    private static final String LOG = "Routine-Fragment_SIMOALE";
    private View view;

    private RoutineCardAdapter routineAdapter;
    private RoutineViewModel routineViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_routine, container, false);
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            setRecyclerView(activity);
            routineViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(RoutineViewModel.class);
            routineViewModel.getRoutineList().observe((LifecycleOwner) activity, (routines -> routineAdapter.setData(routines)));
            FloatingActionButton fab = view.findViewById(R.id.fab_add);
            fab.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_nav_routine_to_newRoutineFragment);
            });
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.routine_recycler_view);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;
        routineAdapter = new RoutineCardAdapter(activity, listener);
        recyclerView.setAdapter(routineAdapter);
    }

    public void onItemClick(int position) {
        // Handle the action on a click on a recyclerView element
        // Saves the current tapped item in the viewModel
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            routineViewModel.select(routineAdapter.getRoutine(position));
            Navigation.findNavController(view).navigate(R.id.action_walletFragment_to_walletDetailsFragment);
        }
    }
}