package com.nerdvana.positiveoffline.view.printersettings;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.SimpleListAdapter;
import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.intf.PrinterSettingsContract;
import com.nerdvana.positiveoffline.model.SimpleListModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PrinterSetupFragment extends Fragment implements PrinterSettingsContract {

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
        printerSeriesList = new ArrayList<>();
        try {
            for (PrinterSeries smm : dataSyncViewModel.getPrinterSeries()) {
                printerSeriesList.add(new SimpleListModel(smm.getModel_name(), smm.getIs_selected(), smm.getId()));
            }
            SimpleListAdapter simpleListAdapter = new SimpleListAdapter(printerSeriesList, getContext(), this, AppConstants.PRINTER_SERIES);
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
        printerLanguageList = new ArrayList<>();
        try {
            for (PrinterLanguage printerLanguage : dataSyncViewModel.getPrinterLanguage()) {
                printerLanguageList.add(new SimpleListModel(printerLanguage.getLanguage_name(), printerLanguage.getIs_selected(), printerLanguage.getId()));
            }
            SimpleListAdapter simpleListAdapter = new SimpleListAdapter(printerLanguageList, getContext(), this, AppConstants.PRINTER_LANGUAGE);
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

    @Override
    public void clicked(SimpleListModel simpleListModel, int type) {
        try {
            switch (type) {
                case 10://PRINTER SERIES UPDATE
                    for (PrinterSeries printerSeries : dataSyncViewModel.getPrinterSeries()) {
                        if (printerSeries.getId() == simpleListModel.getId()) {
                            printerSeries.setIs_selected(true);
                        } else {
                            printerSeries.setIs_selected(false);
                        }
                        dataSyncViewModel.updatePrinterSeries(printerSeries);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setPrinterSeriesAdapter();
                            }
                        }, 300);
                    }
                    break;
                case 11://PRINTER LANGUAGE UPDATE
                    for (PrinterLanguage printerLanguage : dataSyncViewModel.getPrinterLanguage()) {
                        if (printerLanguage.getId() == simpleListModel.getId()) {
                            printerLanguage.setIs_selected(true);
                        } else {
                            printerLanguage.setIs_selected(false);
                        }
                        dataSyncViewModel.updatePrinterLanguage(printerLanguage);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setPrinterLanguageAdapter();
                            }
                        }, 300);

                    }
                    break;
            }
        } catch (Exception e) {

        }

    }
}
