package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.model.PrintingListModel;

public interface PrintoutSelectionIntf {
    void clicked(int position, PrintingListModel printingListModel);
    void closeClicked(int position, int innerPosition);
}
