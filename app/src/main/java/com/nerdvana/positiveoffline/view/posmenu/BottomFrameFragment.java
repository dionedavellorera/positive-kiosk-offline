package com.nerdvana.positiveoffline.view.posmenu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.adapter.ButtonsAdapter;
import com.nerdvana.positiveoffline.background.ButtonsAsync;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.AsyncContract;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.RefreshViewModel;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomFrameFragment extends Fragment implements ButtonsContract, AsyncContract {

    private View view;
    private RecyclerView listButtons;
    private ButtonsAdapter buttonsAdapter;
    private UserViewModel userViewModel;
    private DataSyncViewModel dataSyncViewModel;

    private boolean isDarkMode = false;
    private ConstraintLayout rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_frame, container, false);
        BusProvider.getInstance().register(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews();
        initUserViewModel();
        initDataSyncViewModel();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initThemeSelectionListener();
        setButtonsAdapter(isDarkMode);


    }

    private void initThemeSelectionListener() {
        dataSyncViewModel.getThemeSelectionLiveData().observe(this, new Observer<List<ThemeSelection>>() {
            @Override
            public void onChanged(List<ThemeSelection> themeSelectionList) {
                for (ThemeSelection tsl : themeSelectionList) {
                    if (tsl.getIs_selected()) {
                        if (tsl.getTheme_id() == 100) { // LIGHT MODE
                            isDarkMode = false;
                            listButtons.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            rootView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        } else { // DARK MODE
                            isDarkMode = true;
                            listButtons.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            rootView.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                        }
                    }
                }
                setButtonsAdapter(isDarkMode);
            }

        });
    }

    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    @Subscribe
    public void refreshView(RefreshViewModel refreshViewModel) {
        setButtonsAdapter(isDarkMode);
    }

    private void initializeViews() {
        listButtons = view.findViewById(R.id.listButtons);
        rootView = view.findViewById(R.id.rootView);
    }

    private void setButtonsAdapter(boolean isDarkMode) {
        buttonsAdapter = new ButtonsAdapter(new ArrayList<ButtonsModel>(), this, getContext(), isDarkMode);
        listButtons.setLayoutManager(new GridLayoutManager(getContext(),2,  GridLayoutManager.HORIZONTAL, false));
//        listButtons.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        listButtons.setAdapter(buttonsAdapter);
        new ButtonsAsync(BottomFrameFragment.this, getContext(), userViewModel).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    @Override
    public void clicked(ButtonsModel buttonsModel) {
        BusProvider.getInstance().post(buttonsModel);
    }

    @Override
    public void doneLoading(List list, String isFor) {
        switch (isFor) {
            case "buttons":
                buttonsAdapter.addItems(list);
                if (buttonsAdapter != null) buttonsAdapter.notifyDataSetChanged();
                break;
        }
    }

}
