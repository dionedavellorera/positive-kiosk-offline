package com.nerdvana.positiveoffline.printer;

import android.content.Context;
import android.util.Log;

import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;

import java.util.ArrayList;
import java.util.List;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class EJFileCreator {

    public static String cutOffString(CutOff cutOff, Context context, boolean isReprint) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);


        finalString += PrinterUtils.receiptString("", "", context, true);
        if (isReprint) {
            finalString += PrinterUtils.receiptString("X READING(REPRINT)", "", context, true);
        } else {
            finalString += PrinterUtils.receiptString("X READING", "", context, true);
        }

        finalString += PrinterUtils.receiptString("", "", context, true);

        finalString += PrinterUtils.receiptString("POSTING DATE:", cutOff.getCreated_at(), context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);
        finalString += PrinterUtils.receiptString("DESCRIPTION               VALUE", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);

        finalString += PrinterUtils.receiptString("GROSS SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getGross_sales())), context, false);
        finalString += PrinterUtils.receiptString("NET SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getNet_sales())), context, false);
        finalString += PrinterUtils.receiptString("VATABLE SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVatable_sales())), context, false);
        finalString += PrinterUtils.receiptString("VAT EXEMPT SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVat_exempt_sales())), context, false);
        finalString += PrinterUtils.receiptString("VAT DISCOUNT", "0.00", context, false);
        finalString += PrinterUtils.receiptString("VAT AMOUNT", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVat_amount())), context, false);
        finalString += PrinterUtils.receiptString("ZERO RATED SALES", "0.00", context, false);
        finalString += PrinterUtils.receiptString("CASH SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getTotal_cash_payments())), context, false);
        finalString += PrinterUtils.receiptString("CARD SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getTotal_card_payments())), context, false);
        finalString += PrinterUtils.receiptString("VOID", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVoid_amount())), context, false);
        finalString += PrinterUtils.receiptString("SENIOR", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getSeniorAmount())), context, false);
        finalString += PrinterUtils.receiptString("SENIOR(COUNT)", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getSeniorCount())), context, false);
        finalString += PrinterUtils.receiptString("PWD", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getPwdAmount())), context, false);
        finalString += PrinterUtils.receiptString("PWD(COUNT)", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getPwdCount())), context, false);
        finalString += PrinterUtils.receiptString("OTHERS", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getOthersAmount())), context, false);
        finalString += PrinterUtils.receiptString("OTHERS(COUNT)", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getOthersCount())), context, false);
        finalString += PrinterUtils.receiptString("", "", context, true);

        finalString += PrinterUtils.receiptString(
                "POS PROVIDER : NERDVANA CORP.",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ADDRESS : 1 CANLEY ROAD BRGY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "BAGONG ILOG PASIG CITY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VAT REG TIN: 009-772-500-00000",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:**********************",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO: ********-***-*******-*****",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "THIS RECEIPT SHALL BE VALID FOR",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "FIVE(5) YEARS FROM THE DATE OF",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "THE PERMIT TO USE",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);



        return finalString;
    }

    public static String endOfDayString(EndOfDay endOfDay, Context context, boolean isReprint) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);
        if (isReprint) {
            finalString += PrinterUtils.receiptString(
                    "Z READING(REPRINT)",
                    "",
                    context,
                    true);
        } else {
            finalString += PrinterUtils.receiptString(
                    "Z READING",
                    "",
                    context,
                    true);
        }

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "POSTING DATE:",
                endOfDay.getCreated_at(),
                context,
                true);
        finalString += PrinterUtils.receiptString(
                new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"),
                endOfDay.getCreated_at(),
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DESCRIPTION               VALUE",
                endOfDay.getCreated_at(),
                context,
                true);
        finalString += PrinterUtils.receiptString(
                new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"),
                endOfDay.getCreated_at(),
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "GROSS SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getGross_sales())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "NET SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getNet_sales())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VATABLE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVatable_sales())),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "VAT EXEMPT SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVat_exempt_sales())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VAT DISCOUNT",
                PrinterUtils.returnWithTwoDecimal("0.00"),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VAT AMOUNT",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVat_amount())),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "ZERO RATED SALES",
                "0.00",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "CASH SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_cash_payments())),
                context,
                false);



        finalString += PrinterUtils.receiptString(
                "CARD SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_card_payments())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VOID",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVoid_amount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "SENIOR",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getSeniorAmount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "SENIOR(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getSeniorCount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "PWD",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getPwdAmount())),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "PWD(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getPwdCount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "OTHERS",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getOthersAmount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "OTHERS(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getOthersCount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"),
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "BEG OR NO",
                "--",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "ENDING OR NO",
                "--",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "BEG BALANCE",
                "--",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "ENDING BALANCE",
                "--",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"),
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "Z COUNTER:" + endOfDay.getId(),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "POS PROVIDER : NERDVANA CORP.",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ADDRESS : 1 CANLEY ROAD BRGY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "BAGONG ILOG PASIG CITY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VAT REG TIN: 009-772-500-00000",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:**********************",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO: ********-***-*******-*****",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "THIS RECEIPT SHALL BE VALID FOR",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "FIVE(5) YEARS FROM THE DATE OF",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "THE PERMIT TO USE",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);



        return finalString;
    }

    public static String orString(TransactionCompleteDetails transactionCompleteDetails, Context context, boolean isReprint) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);
        finalString += PrinterUtils.receiptString("", "", context, true);
        if (isReprint) {
            finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(REPRINT)", "", context, true);
        } else {
            finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT", "", context, true);
        }

        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("OR NO", transactionCompleteDetails.transactions.getReceipt_number(), context, false);
        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getCreated_at(), context, false);
        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);
        finalString += PrinterUtils.receiptString("QTY  DESCRIPTION          AMOUNT", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);

        Double amountDue = 0.00;
        for (Orders orders : transactionCompleteDetails.ordersList) {

            String qty = "";

            qty += orders.getQty();

            if (String.valueOf(orders.getQty()).length() < 4) {
                for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
                    qty += " ";
                }
            }

            finalString += PrinterUtils.receiptString(qty +  " " + orders.getName(), PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount())), context, false);


            if (orders.getVatExempt() <= 0) {
                amountDue += orders.getOriginal_amount();
            } else {
                amountDue += orders.getAmount();
            }
        }



        if (transactionCompleteDetails.postedDiscountsList.size() > 0) {
            finalString += PrinterUtils.receiptString("", "", context, false);
            finalString += PrinterUtils.receiptString("DISCOUNT LIST", "", context, false);

            for (PostedDiscounts postedDiscounts : transactionCompleteDetails.postedDiscountsList) {
                finalString += PrinterUtils.receiptString(
                        postedDiscounts.getDiscount_name(),
                        postedDiscounts.getCard_number(),
                        context,
                        false);

            }


        }

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VATABLE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVatable_sales()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VAT AMOUNT",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVat_amount()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VAT-EXEMPT SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVat_exempt_sales()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "ZERO-RATED SALES",
                "0.00",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "SUB TOTAL",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getGross_sales()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "AMOUNT DUE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(amountDue)),
                context,
                false);

        Double payments = 0.00;
        List<Integer> tmpArray = new ArrayList<>();
        String pymntType = "";
        for (Payments pym : transactionCompleteDetails.paymentsList) {
            if (!tmpArray.contains(pym.getCore_id())) {
                tmpArray.add(pym.getCore_id());
                pymntType = pym.getName();
            }
            payments += Utils.roundedOffTwoDecimal(pym.getAmount());
        }

        finalString += PrinterUtils.receiptString(
                "TENDERED",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(payments))),
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "CHANGE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getChange()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "PAYMENT TYPE",
                tmpArray.size() > 1 ? "MULTIPLE" : pymntType,
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "SOLD TO",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "NAME:___________________________",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "ADDRESS:________________________",
                "",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "TIN#:___________________________",
                "",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "BUSINESS STYLE:_________________",
                "",
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "THIS SERVES AS YOUR",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "OFFICIAL RECEIPT",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "THANK YOU COME AGAIN",
                "",
                context,
                true);


        finalString += PrinterUtils.receiptString(
                "POS PROVIDER : NERDVANA CORP.",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ADDRESS : 1 CANLEY ROAD BRGY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "BAGONG ILOG PASIG CITY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VAT REG TIN: 009-772-500-00000",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:**********************",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO: ********-***-*******-*****",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + "TODAY",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "THIS RECEIPT SHALL BE VALID FOR",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "FIVE(5) YEARS FROM THE DATE OF",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "THE PERMIT TO USE",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                true);
        return finalString;
    }

}
