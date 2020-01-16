package com.nerdvana.positiveoffline.view.posmenu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.adapter.ButtonsAdapter;
import com.nerdvana.positiveoffline.background.ButtonsAsync;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.AsyncContract;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.RefreshViewModel;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
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

        setButtonsAdapter();

    }


    @Subscribe
    public void refreshView(RefreshViewModel refreshViewModel) {
        setButtonsAdapter();
    }

    private void initializeViews() {
        listButtons = view.findViewById(R.id.listButtons);
    }

    private void setButtonsAdapter() {
        buttonsAdapter = new ButtonsAdapter(new ArrayList<ButtonsModel>(), this, getContext());
        listButtons.setLayoutManager(new GridLayoutManager(getContext(),2,  GridLayoutManager.HORIZONTAL, false));
        listButtons.setAdapter(buttonsAdapter);
        new ButtonsAsync(BottomFrameFragment.this, getContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                break;
        }
    }

}
