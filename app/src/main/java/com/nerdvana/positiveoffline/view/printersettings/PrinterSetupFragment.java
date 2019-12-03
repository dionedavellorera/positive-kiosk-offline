package com.nerdvana.positiveoffline.view.printersettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.SimpleListAdapter;
import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.model.SimpleListModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PrinterSetupFragment extends Fragment {

    private View view;


    private RecyclerView rvPrinterSetup;
    private RecyclerView rvPrinterLanguage;
    private DataSyncViewModel dataSyncViewModel;
    private List<SimpleListModel> printerSeriesList = new ArrayList<>();
    private List<SimpleListModel> printerLanguageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_printer_setup, container, false);
        initViews(view);
        initDataSyncViewModel();
        setPrinterSeriesAdapter();
        setPrinterLanguageAdapter();
        return view;
    }

    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void setPrinterSeriesAdapter() {
        try {
            for (PrinterSeries smm : dataSyncViewModel.getPrinterSeries()) {
                printerSeriesList.add(new SimpleListModel(smm.getModel_name(), smm.getIs_selected()));
            }
            SimpleListAdapter simpleListAdapter = new SimpleListAdapter(printerSeriesList, getContext());
            rvPrinterSetup.setAdapter(simpleListAdapter);
            rvPrinterSetup.setLayoutManager(new LinearLayoutManager(getContext()));
            simpleListAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void setPrinterLanguageAdapter() {
        try {
            for (PrinterLanguage printerLanguage : dataSyncViewModel.getPrinterLanguage()) {
                printerLanguageList.add(new SimpleListModel(printerLanguage.getLanguage_name(), printerLanguage.getIs_selected()));
            }
            SimpleListAdapter simpleListAdapter = new SimpleListAdapter(printerLanguageList, getContext());
            rvPrinterLanguage.setAdapter(simpleListAdapter);
            rvPrinterLanguage.setLayoutManager(new LinearLayoutManager(getContext()));
            simpleListAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        rvPrinterSetup = view.findViewById(R.id.rvPrinterSetup);
        rvPrinterLanguage = view.findViewById(R.id.rvPrinterLanguage);
    }

}
