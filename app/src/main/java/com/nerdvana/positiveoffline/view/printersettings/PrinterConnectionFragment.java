package com.nerdvana.positiveoffline.view.printersettings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.adapter.OtherPrinterAdapter;
import com.nerdvana.positiveoffline.intf.PrinterConnection;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.printer.SStarPort;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import java.util.ArrayList;
import java.util.List;

public class PrinterConnectionFragment extends Fragment implements PrinterConnection {
    private View view;

    private TextView activePrinter;
    private OtherPrinterAdapter otherPrinterAdapter;

    private List<OtherPrinterModel> otherPrinterModelList;
    private RecyclerView listOtherPrinter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_printer_connection, container, false);

        activePrinter = view.findViewById(R.id.activePrinter);
        listOtherPrinter = view.findViewById(R.id.listOtherPrinter);
        otherPrinterModelList = new ArrayList<>();


        otherPrinterAdapter = new OtherPrinterAdapter(otherPrinterModelList, this, getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listOtherPrinter.setLayoutManager(llm);
        listOtherPrinter.setAdapter(otherPrinterAdapter);
        otherPrinterAdapter.notifyDataSetChanged();

        FilterOption mFilterOption = new FilterOption();
        mFilterOption.setDeviceModel(Discovery.MODEL_ALL);
        mFilterOption.setPortType(Discovery.TYPE_ALL);
        mFilterOption.setDeviceType(Discovery.TYPE_ALL);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NONE);
        try {
            Discovery.start(getContext(), mFilterOption, mDiscoveryListener);
        } catch (Epos2Exception e) {
            Toast.makeText(getContext(), e.getErrorStatus(), Toast.LENGTH_SHORT).show();
        }

        if (!SharedPreferenceManager.getString(getContext(), AppConstants.SELECTED_PRINTER_MANUALLY).isEmpty()) {
            activePrinter.setText(SharedPreferenceManager.getString(getContext(), AppConstants.SELECTED_PRINTER_MANUALLY));
        }

        try {
            List<PortInfo> portList = StarIOPort.searchPrinter("BT:");
            for (PortInfo portInfo : portList) {
                otherPrinterModelList.add(new OtherPrinterModel("BT:"+portInfo.getMacAddress(), portInfo.getModelName(), OtherPrinterModel.STAR_PRINTER));
                if (otherPrinterAdapter != null) otherPrinterAdapter.notifyDataSetChanged();
            }
        } catch (StarIOPortException e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            Discovery.stop();
        } catch (Epos2Exception e) {
            e.printStackTrace();
        }
    }

    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    otherPrinterModelList.add(new OtherPrinterModel(deviceInfo.getTarget(), deviceInfo.getDeviceName(), OtherPrinterModel.EPSON));
                    if (otherPrinterAdapter != null) otherPrinterAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public void clicked(int position) {

        SharedPreferenceManager.saveString(getContext(), otherPrinterModelList.get(position).getHead(), AppConstants.SELECTED_PRINTER_MANUALLY);
        SharedPreferenceManager.saveString(getContext(), String.valueOf(otherPrinterModelList.get(position).getPrinterModel()), AppConstants.SELECTED_PRINTER_MODEL);


        if (String.valueOf(otherPrinterModelList.get(position).getPrinterModel()).equalsIgnoreCase(String.valueOf(OtherPrinterModel.STAR_PRINTER))) {
            SStarPort.changePort(getContext());
        }

        activePrinter.setText(otherPrinterModelList.get(position).getHead());

        if (otherPrinterAdapter != null) otherPrinterAdapter.notifyDataSetChanged();
    }
}
