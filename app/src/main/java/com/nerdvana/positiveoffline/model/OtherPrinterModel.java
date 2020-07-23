package com.nerdvana.positiveoffline.model;

public class OtherPrinterModel {
    private String head;
    private String sub;
    private int printerModel;
    public static final int EPSON = 100;
    public static final int STAR_PRINTER = 101;
    public static final int SUNMI_PRINTER = 102;
    public OtherPrinterModel(String head, String sub, int printer_model) {
        this.head = head;
        this.sub = sub;
        this.printerModel = printer_model;
    }

    public int getPrinterModel() {
        return printerModel;
    }

    public String getHead() {
        return head;
    }

    public String getSub() {
        return sub;
    }
}

