package com.nerdvana.positiveoffline.printer;

import android.content.Context;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;

public class SPrinter {
    private static Printer printer;
    public SPrinter(int printerModel, int printerLanguage, Context context) {
        try {
            printer = new Printer(printerModel, printerLanguage, context);
        } catch (Epos2Exception e) {
            e.printStackTrace();
        }
    }

    public static Printer getPrinter() {
        return printer;
    }
}


