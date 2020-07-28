package com.nerdvana.positiveoffline.printer;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PrinterUtils {

    public static void addTextToPrinter(Printer printer, String text,
                                        int isBold, int isUnderlined,
                                        int alignment, int feedLine,
                                        int textSizeWidth, int textSizeHeight) {

        if (printer != null) {
            StringBuilder textData = new StringBuilder();
            try {
//                printer.addFeedLine(feedLine);
                printer.addTextSize(textSizeWidth, textSizeHeight);
                printer.addTextAlign(alignment);
                printer.addTextStyle(Printer.PARAM_DEFAULT, isUnderlined, isBold, Printer.PARAM_DEFAULT);
//                printer.addTextSmooth(Printer.TRUE);
                printer.addText(textData.toString());
                textData.append(text);
                printer.addText(textData.toString());
                textData.delete(0, textData.length());
                printer.addFeedLine(feedLine);
            } catch (Epos2Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static StarIOPort getStarPort(Context context) throws StarIOPortException {
        return StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
    }

    public static void connect(Context context, Printer printer) {
        if (!TextUtils.isEmpty(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY))) {
            try {
                if (printer != null) {
                    printer.connect(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), Printer.PARAM_DEFAULT);
                }
            } catch (Epos2Exception e) {
                try {
                    printer.disconnect();
                } catch (Epos2Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public static String twoColumns(String partOne, String partTwo, int maxTextCountPerLine, int columns, Context context) {
        String finalString = "";
        int filler = 0;
        int maxColumnDivideTwo = (Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT)) / 2);
//        int maxColumnDivideTwo = (40 / 2);
        if (partOne != null) {
            if (partOne.length() < maxColumnDivideTwo) {
                filler += (maxColumnDivideTwo - partOne.length());
            }
        }
        if (partTwo != null) {
            if (partTwo.length() < maxColumnDivideTwo) {
                filler += (maxColumnDivideTwo - partTwo.length());
            }
        }

        finalString = (partOne.length() >= maxColumnDivideTwo ? partOne.substring(0, maxColumnDivideTwo) : partOne) + repeat(" ", filler) + (partTwo.length() >= maxColumnDivideTwo ? partTwo.substring(0, maxColumnDivideTwo) : partTwo);

        return finalString;
    }


    private static String repeat(String str, int i){
        return new String(new char[i]).replace("\0", str);
    }



    public static void addPrinterSpace(int count, Printer printer) {
        try {
            if (printer != null) {
                printer.addFeedLine(count);
            }

        } catch (Epos2Exception e) {
            e.printStackTrace();
        }
    }

    public static String returnWithTwoDecimal(String amount) {
        String finalValue = "";
        if (amount.contains(".")) {
            String[] tempArray = amount.split("\\.");
            if (tempArray[1].length() > 2) {
                finalValue = tempArray[0] + "." + tempArray[1].substring(0,2);
            } else {
//                finalValue = tempArray[0] + "." + tempArray[1];
                finalValue = tempArray[0] + "." + (tempArray[1].length() == 1 ? tempArray[1] +  "0" : tempArray[1]);
            }
        } else {
            finalValue = amount;
        }

        return finalValue;

    }

    public static void addHeader(PrintModel printModel, Printer printer) {
        addTextToPrinter(printer, "ABC COMPANY", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, "1 ABC ST. DE AVE", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, "PASIG CITY 1600", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer," TEL NO: 8123-4567", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, "VAT REG TIN NO: 009-123-456-00000" , Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, "MIN NO: *****************", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1,1 ,1 );
        addTextToPrinter(printer, "SERIAL NO: ********", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1,1);

    }

    public static void addFooterToPrinter(Printer printer) {

        if (printer != null) {
            addPrinterSpace(1, printer);
//            addTextToPrinter(printer, "----- SYSTEM PROVIDER DETAILS -----", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "POS PROVIDER : NERDVANA CORP.", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "ADDRESS : 1 CANLEY ROAD BRGY", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "BAGONG ILOG PASIG CITY", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "VAT REG TIN: 009-772-500-00000", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "ACCRED NO:" + SharedPreferenceManager.getString(null, AppConstants.ACCRED_NO), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "DATE ISSUED : " + Utils.getDateTimeToday(), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "VALID UNTIL : " + Utils.getDateTimeToday(), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Date Issued : " + Utils.birDateTimeFormat(currentDate), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Valid Until : " + Utils.birDateTimeFormat(currentDatePlus5), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

            addTextToPrinter(printer, "PERMIT NO:" +SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO) , Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1,1 ,1 );
            addTextToPrinter(printer, "DATE ISSUED : " + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "VALID UNTIL : " + SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Date Issued : " + Utils.birDateTimeFormat(currentDate), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Valid Until : " + Utils.birDateTimeFormat(currentDatePlus5), Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

            addPrinterSpace(1, printer);
            addTextToPrinter(printer, "THIS INVOICE/RECEIPT SHALL BE VALID FOR", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "FIVE(5) YEARS FROM THE DATE OF", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "THE PERMIT TO USE", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addPrinterSpace(1, printer);


//            addTextToPrinter(printer, "------------", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1,1,1);
//            addTextToPrinter(printer, "PRINTED DATE" , Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, currentDateTime , Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "PRINTED BY: " + userModel.getUsername(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

//            addTextToPrinter(printer, "THIS DOCUMENT IS NOT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "VALID FOR CLAIM", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "OF INPUT TAX", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addPrinterSpace(1);
        }
    }

    public static String receiptString(String partOne, String partTwo, Context context, boolean isCenter) {
        String finalString = "";
        int filler = 0;
        int maxColumnDivideTwo = (Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT)) / 2);

        if (isCenter) {
            if (partOne.length() > Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))) {
                finalString = partOne.substring(0, Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT)));
            } else {
                int custFillter = (Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT)) - partOne.length()) / 2;
                finalString = repeat(" ", custFillter) + partOne + repeat(" ", custFillter);
            }

        } else {
            if (!TextUtils.isEmpty(partOne)) {
                if (partOne.length() < maxColumnDivideTwo) {
                    filler += (maxColumnDivideTwo - partOne.length());
                }
            }
            String part2Adder = "";
            if (!TextUtils.isEmpty(partTwo)) {
                if (partTwo.length() < maxColumnDivideTwo) {
                    filler += (maxColumnDivideTwo - partTwo.length());
                }

                part2Adder = partTwo.length() >= maxColumnDivideTwo ? partTwo.substring(0, maxColumnDivideTwo) : partTwo;
            } else {
                filler += maxColumnDivideTwo;
            }




            finalString = (partOne.length() >= maxColumnDivideTwo ? partOne.substring(0, maxColumnDivideTwo) : partOne)
                    + repeat(" ", filler)
                    + (part2Adder);

        }

        return finalString + "\n";
    }



    public static String yearPlusFive(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String res = "";
        try {
            DateTime jodatime = dtf.parseDateTime(date);
//            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMM d h:m a");
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            res = dtfOut.print(jodatime.plusYears(5));
        } catch (Exception e) {
            res  = "NA";
        }
        return res.toUpperCase();
    }

}
