package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.model.SimpleListModel;

public interface PrinterSettingsContract {
    void clicked(SimpleListModel simpleListModel, int type);
}
