package com.nerdvana.positiveoffline.printer;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class EJFileCreator {

    public static String shortOverString(CutOff cutOff, Context context, boolean isReprint) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);



        finalString += PrinterUtils.receiptString("", "", context, true);

        finalString += PrinterUtils.receiptString("DATE:" + cutOff.getTreg(), "", context, true);

        finalString += PrinterUtils.receiptString("", "", context, true);

        finalString += PrinterUtils.receiptString("SHORT/OVER", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getTotal_cash_amount() - (cutOff.getTotal_cash_payments() - cutOff.getTotal_change()))), context, false);

        return finalString;
    }

    public static String cutOffString(CutOff cutOff, Context context,
                                      boolean isReprint, boolean isPrintShortOver) {
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

        finalString += PrinterUtils.receiptString("POSTING DATE:", cutOff.getTreg(), context, true);
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
        if (cutOff.getTotal_payout() > 0) {
            finalString += PrinterUtils.receiptString("PAYOUT", "-"+PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getTotal_payout())), context, false);
        } else {
            finalString += PrinterUtils.receiptString("PAYOUT", "0.00", context, false);
        }

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
        if (isPrintShortOver) {
            BusProvider.getInstance().post(new PrintModel("PRINT_SHORTOVER", GsonHelper.getGson().toJson(cutOff)));
        }


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
                endOfDay.getTreg(),
                context,
                true);
        finalString += PrinterUtils.receiptString(
                new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"),
                endOfDay.getTreg(),
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DESCRIPTION               VALUE",
                endOfDay.getTreg(),
                context,
                true);
        finalString += PrinterUtils.receiptString(
                new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"),
                endOfDay.getTreg(),
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
                endOfDay.getBegOrNo(),
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "ENDING OR NO",
                endOfDay.getEndOrNo(),
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "BEG BALANCE",
                String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getBegSales())),
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "ENDING BALANCE",
                String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getEndSales())),
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

    public static String orString(TransactionCompleteDetails transactionCompleteDetails, Context context, boolean isReprint, PrintModel printModel) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);
        finalString += PrinterUtils.receiptString("", "", context, true);

        if (transactionCompleteDetails.transactions.getHas_special() == 1) {

            if (printModel.getType().equalsIgnoreCase("PRINT_RECEIPT")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(STORE COPY)", "", context, true);
                BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT_SPEC", GsonHelper.getGson().toJson(transactionCompleteDetails)));
            } else if (printModel.getType().equalsIgnoreCase("REPRINT_RECEIPT")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(STORE COPY)\nREPRINT", "", context, true);
                BusProvider.getInstance().post(new PrintModel("REPRINT_RECEIPT_SPEC", GsonHelper.getGson().toJson(transactionCompleteDetails)));
            }
            if (printModel.getType().equalsIgnoreCase("PRINT_RECEIPT_SPEC")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(CUSTOMER COPY)", "", context, true);
            } else if (printModel.getType().equalsIgnoreCase("REPRINT_RECEIPT_SPEC")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(CUSTOMER COPY)\nREPRINT", "", context, true);
            }
        } else {
            if (isReprint) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(REPRINT)", "", context, true);
            } else {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT", "", context, true);
            }
        }

        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("OR NO", transactionCompleteDetails.transactions.getReceipt_number(), context, false);
//        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getTreg(), context, false);
        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);
        finalString += PrinterUtils.receiptString("QTY  DESCRIPTION          AMOUNT", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);

        Double amountDue = 0.00;
        for (Orders orders : transactionCompleteDetails.ordersList) {
            if (!orders.getIs_void()) {
                String qty = "";

                qty += orders.getQty();

                if (String.valueOf(orders.getQty()).length() < 4) {
                    for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
                        qty += " ";
                    }
                }

                if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                    finalString += PrinterUtils.receiptString("-" + qty +  " " + orders.getName(), "0.00", context, false);
                } else {
                    finalString += PrinterUtils.receiptString(qty +  " " + orders.getName(), PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount())), context, false);
                }

                if (orders.getDiscountAmount() > 0) {
                     finalString += PrinterUtils.receiptString("LESS", PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount())), context, false);
                }


                if (orders.getVatExempt() <= 0) {
                    amountDue += orders.getOriginal_amount();
                } else {
                    amountDue += orders.getAmount();
                }
            }

        }



        if (transactionCompleteDetails.postedDiscountsList.size() > 0) {
            finalString += PrinterUtils.receiptString("", "", context, false);
            finalString += PrinterUtils.receiptString("DISCOUNT LIST", "", context, false);

            for (PostedDiscounts postedDiscounts : transactionCompleteDetails.postedDiscountsList) {
                if (!postedDiscounts.getIs_void()) {
                    finalString += PrinterUtils.receiptString(
                            postedDiscounts.getDiscount_name(),
                            postedDiscounts.getCard_number(),
                            context,
                            false);
                }
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
        if (transactionCompleteDetails.transactions.getService_charge_value() > 0) {
            finalString += PrinterUtils.receiptString(
                    "SERVICE CHARGE",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(transactionCompleteDetails.transactions.getService_charge_value())),
                    context,
                    false);
        } else {
            finalString += PrinterUtils.receiptString(
                    "SERVICE CHARGE",
                    "0.00",
                    context,
                    false);
        }


        finalString += PrinterUtils.receiptString(
                "",
                "",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "SUB TOTAL",
                PrinterUtils.returnWithTwoDecimal(
                        String.valueOf(
                                Utils.roundedOffTwoDecimal(
                                        transactionCompleteDetails.transactions.getGross_sales()
                                ) +
                                Utils.roundedOffTwoDecimal(
                                        transactionCompleteDetails.transactions.getService_charge_value()
                                )
                        )),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "AMOUNT DUE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(amountDue)
                        +
                        Utils.roundedOffTwoDecimal(
                                transactionCompleteDetails.transactions.getService_charge_value()
                        )),
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

        if (transactionCompleteDetails.orDetails != null) {
            finalString += PrinterUtils.receiptString(
                    "NAME:" + transactionCompleteDetails.orDetails.getName(),
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:" + transactionCompleteDetails.orDetails.getAddress(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:" + transactionCompleteDetails.orDetails.getTin_number(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:" + transactionCompleteDetails.orDetails.getBusiness_style(),
                    "",
                    context,
                    true);

        } else {
            finalString += PrinterUtils.receiptString(
                    "NAME:___________________________",
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:___________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:_________________",
                    "",
                    context,
                    true);
        }



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

    public static String backoutString(TransactionCompleteDetails transactionCompleteDetails, Context context, boolean isReprint, PrintModel printModel) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);
        finalString += PrinterUtils.receiptString("", "", context, true);


        finalString += PrinterUtils.receiptString("BACKOUT", "", context, true);
        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getIs_backed_out_at(), context, false);
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
                if (!postedDiscounts.getIs_void()) {
                    finalString += PrinterUtils.receiptString(
                            postedDiscounts.getDiscount_name(),
                            postedDiscounts.getCard_number(),
                            context,
                            false);
                }
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

        if (transactionCompleteDetails.orDetails != null) {
            finalString += PrinterUtils.receiptString(
                    "NAME:" + transactionCompleteDetails.orDetails.getName(),
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:" + transactionCompleteDetails.orDetails.getAddress(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:" + transactionCompleteDetails.orDetails.getTin_number(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:" + transactionCompleteDetails.orDetails.getBusiness_style(),
                    "",
                    context,
                    true);

        } else {
            finalString += PrinterUtils.receiptString(
                    "NAME:___________________________",
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:___________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:_________________",
                    "",
                    context,
                    true);
        }



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

    public static String payoutString(Payout payout, Context context) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("PAYOUT SLIP", "", context, true);

        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("AMOUNT", "-" + PrinterUtils.returnWithTwoDecimal(String.valueOf(payout.getAmount())), context, false);
        finalString += PrinterUtils.receiptString("CASHIER", payout.getUsername(), context, false);
        finalString += PrinterUtils.receiptString("MANAGER", payout.getManager_username(), context, false);
        finalString += PrinterUtils.receiptString("DATE", payout.getTreg(), context, false);
        finalString += PrinterUtils.receiptString("", "", context, true);

        return finalString;
    }

    public static String postVoidString(TransactionCompleteDetails transactionCompleteDetails, Context context, boolean isReprint, PrintModel printModel) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);
        finalString += PrinterUtils.receiptString("", "", context, true);

        finalString += PrinterUtils.receiptString("VOID", "", context, true);

        if (transactionCompleteDetails.transactions.getHas_special() == 1) {

            if (printModel.getType().equalsIgnoreCase("PRINT_RECEIPT")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(STORE COPY)", "", context, true);
                BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT_SPEC", GsonHelper.getGson().toJson(transactionCompleteDetails)));
            } else if (printModel.getType().equalsIgnoreCase("REPRINT_RECEIPT")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(STORE COPY)\nREPRINT", "", context, true);
                BusProvider.getInstance().post(new PrintModel("REPRINT_RECEIPT_SPEC", GsonHelper.getGson().toJson(transactionCompleteDetails)));
            }
            if (printModel.getType().equalsIgnoreCase("PRINT_RECEIPT_SPEC")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(CUSTOMER COPY)", "", context, true);
            } else if (printModel.getType().equalsIgnoreCase("REPRINT_RECEIPT_SPEC")) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(CUSTOMER COPY)\nREPRINT", "", context, true);
            }
        } else {
            if (isReprint) {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT(REPRINT)", "", context, true);
            } else {
                finalString += PrinterUtils.receiptString("OFFICIAL RECEIPT", "", context, true);
            }
        }

        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("OR NO", transactionCompleteDetails.transactions.getReceipt_number(), context, false);
        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getTreg(), context, false);
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
                if (!postedDiscounts.getIs_void()) {
                    finalString += PrinterUtils.receiptString(
                            postedDiscounts.getDiscount_name(),
                            postedDiscounts.getCard_number(),
                            context,
                            false);
                }
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

        if (transactionCompleteDetails.orDetails != null) {
            finalString += PrinterUtils.receiptString(
                    "NAME:" + transactionCompleteDetails.orDetails.getName(),
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:" + transactionCompleteDetails.orDetails.getAddress(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:" + transactionCompleteDetails.orDetails.getTin_number(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:" + transactionCompleteDetails.orDetails.getBusiness_style(),
                    "",
                    context,
                    true);

        } else {
            finalString += PrinterUtils.receiptString(
                    "NAME:___________________________",
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:___________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:_________________",
                    "",
                    context,
                    true);
        }



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

    private static String intransitReceipt(List<String> details, Context ctx) {
        String finalString = "";
        float maxColumn = Float.valueOf(SharedPreferenceManager.getString(ctx, AppConstants.MAX_COLUMN_COUNT));
        int perColumn = (int)maxColumn / details.size();

        for (int i = 0; i < details.size(); i++) {
            if (details.size() >= perColumn) {
                finalString += details.get(i);
            } else {
                finalString += details.get(i);
                float temp = perColumn - details.get(i).length();
                for (int j = 0; j < temp; j++) {
                    finalString += " ";
                }
            }
        }
        return finalString;
    }


    public static String intransitString(List<TransactionWithOrders> data, Context ctx){
        String finalString = "";

        List<TransactionWithOrders> transactions = data;


        finalString += "INTRANSIT SLIP" + "\n";
        List<String> t = new ArrayList<>();
        t.add("I");
        t.add("II");
        t.add("III");
        t.add("IV");
        t.add("V");

        finalString += intransitReceipt(t, ctx) + "\n";

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        for (TransactionWithOrders tr : transactions) {
            List<String> temp = new ArrayList<>();

            if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
                DateTime dt = formatter.parseDateTime(tr.transactions.getSaved_at());
                temp.add(tr.transactions.getTrans_name());
                temp.add(String.valueOf(tr.ordersList.size()));
                String dateONly = tr.transactions.getSaved_at().split(" ")[0];
                temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                temp.add(String.valueOf(tr.transactions.getGross_sales()));
                temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");
            } else {
                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                    DateTime dt = formatter.parseDateTime(tr.transactions.getSaved_at());
                    temp.add(tr.transactions.getTrans_name());
                    temp.add(String.valueOf(tr.ordersList.size()));
                    String dateONly = tr.transactions.getSaved_at().split(" ")[0];
                    temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                    temp.add(String.valueOf(tr.transactions.getGross_sales()));
                    temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                    //
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {

                    DateTime dt = formatter.parseDateTime(tr.transactions.getCheck_in_time());
                    temp.add(tr.transactions.getTrans_name());
                    temp.add(String.valueOf(tr.ordersList.size()));
                    String dateONly = tr.transactions.getCheck_in_time().split(" ")[0];
                    temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                    temp.add(String.valueOf(tr.transactions.getGross_sales()));
                    temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");


                }
            }

//            DateTime dt = formatter.parseDateTime(tr.transactions.getSaved_at());
//
//
//            temp.add(tr.transactions.getTrans_name());
//            temp.add(String.valueOf(tr.ordersList.size()));
//            String dateONly = tr.transactions.getSaved_at().split(" ")[0];
//            temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
//
//            temp.add(String.valueOf(tr.transactions.getGross_sales()));
//            temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");

            finalString += intransitReceipt(temp, ctx) + "\n";
        }
        finalString += "\n";
        return finalString;

    }

    public static String soaString(TransactionCompleteDetails transactionCompleteDetails, Context context, boolean isReprint, PrintModel printModel) {
        String finalString = "";

        finalString += PrinterUtils.receiptString("ABC COMPANY", "", context, true);
        finalString += PrinterUtils.receiptString("1 ABC ST. DE AVE", "", context, true);
        finalString += PrinterUtils.receiptString("PASIG CITY 1600", "", context, true);
        finalString += PrinterUtils.receiptString("TEL NO: 8123-4567", "", context, true);
        finalString += PrinterUtils.receiptString("MIN NO: *****************", "", context, true);
        finalString += PrinterUtils.receiptString("SERIAL NO: ********", "", context, true);
        finalString += PrinterUtils.receiptString("", "", context, true);

        finalString += PrinterUtils.receiptString("STATEMENT OF ACCOUNT", "", context, true);


        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("OR NO", "NA", context, false);
        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getTreg(), context, false);
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
                if (!postedDiscounts.getIs_void()) {
                    finalString += PrinterUtils.receiptString(
                            postedDiscounts.getDiscount_name(),
                            postedDiscounts.getCard_number(),
                            context,
                            false);
                }
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

        if (transactionCompleteDetails.orDetails != null) {
            finalString += PrinterUtils.receiptString(
                    "NAME:" + transactionCompleteDetails.orDetails.getName(),
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:" + transactionCompleteDetails.orDetails.getAddress(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:" + transactionCompleteDetails.orDetails.getTin_number(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:" + transactionCompleteDetails.orDetails.getBusiness_style(),
                    "",
                    context,
                    true);

        } else {
            finalString += PrinterUtils.receiptString(
                    "NAME:___________________________",
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "ADDRESS:________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "TIN#:___________________________",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "BUSINESS STYLE:_________________",
                    "",
                    context,
                    true);
        }



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

}
