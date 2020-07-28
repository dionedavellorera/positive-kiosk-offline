package com.nerdvana.positiveoffline.printer;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

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
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        finalString += PrinterUtils.receiptString("SHORT/OVER", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getTotal_cash_amount() - (cutOff.getTotal_cash_payments() - cutOff.getTotal_change())))), context, false);

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

        finalString += PrinterUtils.receiptString("POSTING DATE:", cutOff.getTreg(), context, false);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);
        finalString += PrinterUtils.receiptString("DESCRIPTION               VALUE", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);



        finalString += PrinterUtils.receiptString("GROSS SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getGross_sales() + cutOff.getDiscount_amount()))), context, false);
        finalString += PrinterUtils.receiptString("NET SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getNet_sales()))), context, false);
        finalString += PrinterUtils.receiptString("VATABLE SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getVatable_sales()))), context, false);
        finalString += PrinterUtils.receiptString("VAT EXEMPT SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getVat_exempt_sales()))), context, false);
        finalString += PrinterUtils.receiptString("VAT DISCOUNT", "0.00", context, false);
        finalString += PrinterUtils.receiptString("VAT AMOUNT", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getVat_amount()))), context, false);
        finalString += PrinterUtils.receiptString("ZERO RATED SALES", "0.00", context, false);
        finalString += PrinterUtils.receiptString("CASH SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getTotal_cash_payments() - cutOff.getTotal_change()))), context, false);
        finalString += PrinterUtils.receiptString("CARD SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getTotal_card_payments()))), context, false);
        finalString += PrinterUtils.receiptString("ONLINE SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getTotal_online_payments()))), context, false);
        finalString += PrinterUtils.receiptString("MOBILE SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getTotal_mobile_payments()))), context, false);
        finalString += PrinterUtils.receiptString("VOID", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getVoid_amount()))), context, false);
        finalString += PrinterUtils.receiptString("SENIOR", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getSeniorAmount()))), context, false);
        finalString += PrinterUtils.receiptString("SENIOR(COUNT)", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getSeniorCount())), context, false);
        finalString += PrinterUtils.receiptString("PWD", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getPwdAmount()))), context, false);
        finalString += PrinterUtils.receiptString("PWD(COUNT)", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getPwdCount())), context, false);
        finalString += PrinterUtils.receiptString("OTHERS", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getOthersAmount()))), context, false);
        finalString += PrinterUtils.receiptString("OTHERS(COUNT)", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getOthersCount())), context, false);
        if (cutOff.getTotal_payout() > 0) {
            finalString += PrinterUtils.receiptString("PAYOUT", "-"+PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(cutOff.getTotal_payout()))), context, false);
        } else {
            finalString += PrinterUtils.receiptString("PAYOUT", "0.00", context, false);
        }

//        finalString += PrinterUtils.receiptString("SALES REFLECTED BELOW ARE AR FROM PREVIOUS SHIFT", "", context, true);
//        finalString += PrinterUtils.receiptString("AR CASH SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getCash_redeemed_from_prev_ar())), context, false);
//        finalString += PrinterUtils.receiptString("AR CARD SALES", PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getCard_redeemed_from_prev_ar())), context, false);

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
                "",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:" + SharedPreferenceManager.getString(context, AppConstants.ACCRED_NO),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + cutOff.getTreg().split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + PrinterUtils.yearPlusFive(cutOff.getTreg()).split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO:" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY),
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
                false);
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
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getGross_sales() + endOfDay.getDiscount_amount()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "NET SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getNet_sales()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VATABLE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getVatable_sales()))),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "VAT EXEMPT SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getVat_exempt_sales()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VAT DISCOUNT",
                PrinterUtils.returnWithTwoDecimal("0.00"),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "VAT AMOUNT",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getVat_amount()))),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "ZERO RATED SALES",
                "0.00",
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "CASH SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getTotal_cash_payments() - endOfDay.getTotal_change()))),
                context,
                false);



        finalString += PrinterUtils.receiptString(
                "CARD SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getTotal_card_payments()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "ONLINE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getTotal_online_payments()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "MOBILE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getTotal_mobile_payments()))),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "VOID",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getVoid_amount()))),
                context,
                false);
//        finalString += PrinterUtils.receiptString(
//                "LATE CASH REDEEM",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getCash_redeemed_from_prev_ar())),
//                context,
//                false);
//        finalString += PrinterUtils.receiptString(
//                "LATE CARD REDEEM",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getCard_redeemed_from_prev_ar())),
//                context,
//                false);


        finalString += PrinterUtils.receiptString(
                "SENIOR",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getSeniorAmount()))),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "SENIOR(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getSeniorCount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "PWD",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getPwdAmount()))),
                context,
                false);


        finalString += PrinterUtils.receiptString(
                "PWD(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getPwdCount())),
                context,
                false);

        finalString += PrinterUtils.receiptString(
                "OTHERS",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getOthersAmount()))),
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
                String.valueOf(Utils.roundedOffTwoDecimal(Utils.roundedOffTwoDecimal(endOfDay.getBegSales()))),
                context,
                false);
        finalString += PrinterUtils.receiptString(
                "ENDING BALANCE",
                String.valueOf(Utils.roundedOffTwoDecimal(Utils.roundedOffTwoDecimal(endOfDay.getEndSales()))),
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
                "",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:" + SharedPreferenceManager.getString(context, AppConstants.ACCRED_NO),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + endOfDay.getTreg().split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + PrinterUtils.yearPlusFive(endOfDay.getTreg()).split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO:" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL :" +SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY),
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


        if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
            if (transactionCompleteDetails.transactions.getTransaction_type().equalsIgnoreCase("takeout")) {
                finalString += PrinterUtils.receiptString("TAKEOUT", "", context, true);
            } else if (transactionCompleteDetails.transactions.getTransaction_type().equalsIgnoreCase("delivery")) {
                finalString += PrinterUtils.receiptString("DELIVERY", "", context, true);
            } else {
                finalString += PrinterUtils.receiptString("DINE IN", "", context, true);
            }
        }




        finalString += PrinterUtils.receiptString("", "", context, true);
//        if (!TextUtils.isEmpty(transactionCompleteDetails.transactions.getTo_control_number())) {
//            finalString += PrinterUtils.receiptString("TO CTRL NO", transactionCompleteDetails.transactions.getTo_control_number(), context, false);
//        }
        finalString += PrinterUtils.receiptString("OR NO", transactionCompleteDetails.transactions.getReceipt_number(), context, false);
        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getTreg(), context, false);
        finalString += PrinterUtils.receiptString("CASHIER", transactionCompleteDetails.transactions.getCashierName(), context, false);
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
                String myString = "";



                if (orders.getDiscountAmount() > 0) {
                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                        myString = "("+qty.trim()+")" +  "  " + Html.fromHtml(orders.getName_initials());
                    } else {
                        myString = "("+qty.trim()+")" +  " " + Html.fromHtml(orders.getName_initials());
                    }
                } else {
                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                        myString = qty +  "  " + Html.fromHtml(orders.getName_initials());
                    } else {
                        myString = qty +  " " + Html.fromHtml(orders.getName_initials());
                    }
                }

                finalString += PrinterUtils.receiptString(
                        myString,
                        PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(orders.getOriginal_amount() * orders.getQty()))),
                        context, false);



//                if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
//                    finalString += PrinterUtils.receiptString(
//                            myString,
//                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                            context, false);
//                } else {
//                    finalString += PrinterUtils.receiptString(
//                            myString,
//                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                            context, false);
//                }

                if (orders.getQty() > 1) {
                    finalString += PrinterUtils.receiptString("("+String.valueOf(Utils.roundedOffFourDecimal(orders.getAmount())) + ")", "", context, false);
                }


//                if (orders.getDiscountAmount() > 0) {
//                     finalString += PrinterUtils.receiptString("LESS", "-" +PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount())), context, false);
//                }

                amountDue += orders.getOriginal_amount()  * orders.getQty();

//                if (orders.getVatExempt() > 0) {
//                    amountDue += orders.getOriginal_amount()  * orders.getQty();
//                } else {
//                    amountDue += orders.getAmount()  * orders.getQty();
//                }
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

                    if (postedDiscounts.getDiscount_name().equalsIgnoreCase("PWD") ||
                            postedDiscounts.getDiscount_name().equalsIgnoreCase("SENIOR CITIZEN")) {

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
                                "SIGNATURE:________________________",
                                "",
                                context,
                                true);

                    }


                    finalString += PrinterUtils.receiptString(
                            "LESS",
                            "",
                            context,
                            false);
                    finalString += PrinterUtils.receiptString(
                            (postedDiscounts.getIs_percentage() ? postedDiscounts.getDiscount_value() + "%" : String.valueOf((Utils.roundedOffTwoDecimal(postedDiscounts.getDiscount_value())))),
                            String.valueOf((Utils.roundedOffTwoDecimal(postedDiscounts.getAmount()))),
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

//        finalString += PrinterUtils.receiptString(
//                "VATABLE SALES",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVatable_sales()))),
//                context,
//                false);
//
//        finalString += PrinterUtils.receiptString(
//                "VAT AMOUNT",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVat_amount()))),
//                context,
//                false);

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
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getService_charge_value()))),
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


        if (transactionCompleteDetails.transactions.getHas_special() == 1) {
            finalString += PrinterUtils.receiptString(
                    "SUB TOTAL",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(
                            Utils.roundedOffTwoDecimal(
                                    ((transactionCompleteDetails.transactions.getGross_sales() + transactionCompleteDetails.transactions.getDiscount_amount()) * 1.12) +
                            transactionCompleteDetails.transactions.getService_charge_value()))),
                    context,
                    false);
        } else {
            finalString += PrinterUtils.receiptString(
                    "SUB TOTAL",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(                transactionCompleteDetails.transactions.getGross_sales()
                            +
                            transactionCompleteDetails.transactions.getService_charge_value()
                            +
                            transactionCompleteDetails.transactions.getDiscount_amount()))),
                    context,
                    false);
        }



//        if (transactionCompleteDetails.transactions.getHas_special() == 0) {
//            finalString += PrinterUtils.receiptString(
//                    "SUB TOTAL",
//                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(                transactionCompleteDetails.transactions.getGross_sales()
//                            +
//                            transactionCompleteDetails.transactions.getService_charge_value()
//                            +
//                            transactionCompleteDetails.transactions.getDiscount_amount()))),
//                    context,
//                    false);
//        } else {
//            finalString += PrinterUtils.receiptString(
//                    "SUB TOTAL",
//                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(                transactionCompleteDetails.transactions.getGross_sales()
//                            +
//                            transactionCompleteDetails.transactions.getService_charge_value()))),
//                    context,
//                    false);
//        }

//                PrinterUtils.returnWithTwoDecimal(
//                        String.valueOf(
//                                Utils.roundedOffTwoDecimal(
//                                        transactionCompleteDetails.transactions.getGross_sales()
//                                ) +
//                                        Utils.roundedOffTwoDecimal(
//                                                transactionCompleteDetails.transactions.getService_charge_value()
//                                        ) +
//                                        Utils.roundedOffTwoDecimal(
//                                                transactionCompleteDetails.transactions.getDiscount_amount()
//                                        )
//                        )),
//                context,
//                false);

        finalString += PrinterUtils.receiptString(
                "AMOUNT DUE",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(amountDue - transactionCompleteDetails.transactions.getDiscount_amount() + transactionCompleteDetails.transactions.getService_charge_value()))),
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getNet_sales()))),
                context,
                false);

        Double payments = 0.00;
        List<Integer> tmpArray = new ArrayList<>();
        String pymntType = "";
        String cardType = "";
        String cardNumber = "";
        for (Payments pym : transactionCompleteDetails.paymentsList) {
            if (!pym.getIs_void()) {
                if (!tmpArray.contains(pym.getCore_id())) {
                    tmpArray.add(pym.getCore_id());
                    pymntType = pym.getName();
                }

                if (pym.getCore_id() != 2) {
                    payments += Utils.roundedOffTwoDecimal(pym.getAmount());
                }

                if (pym.getCore_id() == 2) {
                    try {
                        JSONObject jsonObject = new JSONObject(pym.getOther_data());
                        cardType = jsonObject.getString("card_type");
                        cardNumber = jsonObject.getString("card_number");

                        int starCount = 0;
                        if (cardNumber.length() < 3) {
                            cardNumber = jsonObject.getString("card_number");
                        } else {
                            starCount = cardNumber.length() - 3;
                            String starString = new String(new char[starCount]).replace("\0", "*");
                            cardNumber = cardNumber.substring(0, cardNumber.length() - starCount) + starString;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
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
        if (tmpArray.size() > 1) {
            finalString += PrinterUtils.receiptString(
                    "PAYMENT TYPE",
                    "MULTIPLE",
                    context,
                    false);
        } else {
            finalString += PrinterUtils.receiptString(
                    "PAYMENT TYPE",
                    pymntType,
                    context,
                    false);
            if (pymntType.equalsIgnoreCase("card")) {
                finalString += PrinterUtils.receiptString(
                        cardType,
                        cardNumber,
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

        if (transactionCompleteDetails.transactions.getTransaction_type().equalsIgnoreCase("delivery")) {
            finalString += PrinterUtils.receiptString(
                    "CONTROL#",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    transactionCompleteDetails.transactions.getControl_number(),
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "DELIVERY FOR",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    transactionCompleteDetails.transactions.getDelivery_to(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "DELIVERY ADDRESS",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    transactionCompleteDetails.transactions.getDelivery_address(),
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
                "",
                "",
                context,
                true);


        finalString += PrinterUtils.receiptString(
                "ACCRED NO:" + SharedPreferenceManager.getString(context, AppConstants.ACCRED_NO),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + transactionCompleteDetails.transactions.getTreg().split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + PrinterUtils.yearPlusFive(transactionCompleteDetails.transactions.getTreg()).split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO:" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY),
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
                "",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:" + SharedPreferenceManager.getString(context, AppConstants.ACCRED_NO),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + transactionCompleteDetails.transactions.getTreg().split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + PrinterUtils.yearPlusFive(transactionCompleteDetails.transactions.getTreg()).split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO:" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED),
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

    public static String itemCancelledString(List<Orders> ordersList, Context context) {


        String finalString = "";
        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("ITEM CANCELLED", "", context, true);

        for (Orders orders : ordersList) {
            if (!orders.getIs_void()) {
                String qty = "";

                qty += orders.getQty();

                if (String.valueOf(orders.getQty()).length() < 4) {
                    for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
                        qty += " ";
                    }
                }

                if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                    finalString += PrinterUtils.receiptString(qty +  "  " + Html.fromHtml(orders.getName()), PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())), context, false);
                } else {
                    finalString += PrinterUtils.receiptString(qty +  " " + Html.fromHtml(orders.getName()), PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())), context, false);
                }
            }
        }

        finalString += PrinterUtils.receiptString("", "", context, true);
        return finalString;

    }

    public static String fosString(List<Orders> ordersList, Context context, String controlNumber) {


        String finalString = "";
        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("ORDER SLIP", "", context, true);

        finalString += PrinterUtils.receiptString(controlNumber, "", context, true);


        double totalAmount = 0.00;

        finalString += PrinterUtils.receiptString("QTY  DESCRIPTION          AMOUNT", "", context, true);
        finalString += PrinterUtils.receiptString(new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), "", context, true);


        for (Orders orders : ordersList) {
            if (!orders.getIs_void()) {
                String qty = "";

                qty += orders.getQty();

                if (String.valueOf(orders.getQty()).length() < 4) {
                    for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
                        qty += " ";
                    }
                }
                String myString = "";



                if (orders.getDiscountAmount() > 0) {
                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                        myString = "("+qty.trim()+")" +  "  " + Html.fromHtml(orders.getName());
                    } else {
                        myString = "("+qty.trim()+")" +  " " + Html.fromHtml(orders.getName());
                    }
                } else {
                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                        myString = qty +  "  " + Html.fromHtml(orders.getName());
                    } else {
                        myString = qty +  " " + Html.fromHtml(orders.getName());
                    }
                }

                finalString += PrinterUtils.receiptString(
                        myString,
                        PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
                        context, false);


                if (orders.getQty() > 1) {
                    finalString += PrinterUtils.receiptString("("+String.valueOf(Utils.roundedOffTwoDecimal(orders.getOriginal_amount())) + ")", "", context, false);
                }


                totalAmount += orders.getOriginal_amount()  * orders.getQty();


            }
//            if (!orders.getIs_void()) {
//                totalAmount += orders.getQty() * orders.getAmount();
//                String qty = "";
//
//                qty += orders.getQty();
//
//                if (String.valueOf(orders.getQty()).length() < 4) {
//                    for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
//                        qty += " ";
//                    }
//                }
//
//                if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
//                    finalString += PrinterUtils.receiptString(qty +  "  " + Html.fromHtml(orders.getName()), PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())), context, false);
//                } else {
//                    finalString += PrinterUtils.receiptString(qty +  " " + Html.fromHtml(orders.getName()), PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())), context, false);
//                }
//
//                if (orders.getQty() > 1) {
//                    finalString += PrinterUtils.receiptString("("+String.valueOf(Utils.roundedOffTwoDecimal(orders.getAmount())) + ")", "", context, false);
//                }
//
//
//                if (orders.getDiscountAmount() > 0) {
//                    finalString += PrinterUtils.receiptString("LESS", PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount() )), context, false);
//
//                }
//
//
//            }
        }

        finalString += PrinterUtils.receiptString("", "", context, true);
        finalString += PrinterUtils.receiptString("TOTAL", PrinterUtils.returnWithTwoDecimal(String.valueOf(totalAmount)), context, false);

        finalString += PrinterUtils.receiptString("PRINTED DATE", Utils.getDateTimeToday(), context, false);

        finalString += PrinterUtils.receiptString("", "", context, true);
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
//        if (!TextUtils.isEmpty(transactionCompleteDetails.transactions.getTo_control_number())) {
//            finalString += PrinterUtils.receiptString("TO CTRL NO", transactionCompleteDetails.transactions.getTo_control_number(), context, false);
//        }
        finalString += PrinterUtils.receiptString("OR NO", transactionCompleteDetails.transactions.getReceipt_number(), context, false);
        finalString += PrinterUtils.receiptString("DATE", transactionCompleteDetails.transactions.getTreg(), context, false);
        finalString += PrinterUtils.receiptString("CASHIER", transactionCompleteDetails.transactions.getCashierName(), context, false);
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
                String myString = "";



                if (orders.getDiscountAmount() > 0) {
                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                        myString = "("+qty.trim()+")" +  "  " + Html.fromHtml(orders.getName_initials());
                    } else {
                        myString = "("+qty.trim()+")" +  " " + Html.fromHtml(orders.getName_initials());
                    }
                } else {
                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                        myString = qty +  "  " + Html.fromHtml(orders.getName_initials());
                    } else {
                        myString = qty +  " " + Html.fromHtml(orders.getName_initials());
                    }
                }

                finalString += PrinterUtils.receiptString(
                        myString,
                        PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(orders.getOriginal_amount() * orders.getQty()))),
                        context, false);



//                if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
//                    finalString += PrinterUtils.receiptString(
//                            myString,
//                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                            context, false);
//                } else {
//                    finalString += PrinterUtils.receiptString(
//                            myString,
//                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                            context, false);
//                }

                if (orders.getQty() > 1) {
                    finalString += PrinterUtils.receiptString("("+String.valueOf(Utils.roundedOffFourDecimal(orders.getAmount())) + ")", "", context, false);
                }


//                if (orders.getDiscountAmount() > 0) {
//                     finalString += PrinterUtils.receiptString("LESS", "-" +PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount())), context, false);
//                }

                amountDue += orders.getOriginal_amount()  * orders.getQty();

//                if (orders.getVatExempt() > 0) {
//                    amountDue += orders.getOriginal_amount()  * orders.getQty();
//                } else {
//                    amountDue += orders.getAmount()  * orders.getQty();
//                }
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

                    if (postedDiscounts.getDiscount_name().equalsIgnoreCase("PWD") ||
                            postedDiscounts.getDiscount_name().equalsIgnoreCase("SENIOR CITIZEN")) {

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
                                "SIGNATURE:________________________",
                                "",
                                context,
                                true);

                    }


                    finalString += PrinterUtils.receiptString(
                            "LESS",
                            "",
                            context,
                            false);
                    finalString += PrinterUtils.receiptString(
                            (postedDiscounts.getIs_percentage() ? postedDiscounts.getDiscount_value() + "%" : String.valueOf((Utils.roundedOffTwoDecimal(postedDiscounts.getDiscount_value())))),
                            String.valueOf((Utils.roundedOffTwoDecimal(postedDiscounts.getAmount()))),
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

//        finalString += PrinterUtils.receiptString(
//                "VATABLE SALES",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVatable_sales()))),
//                context,
//                false);
//
//        finalString += PrinterUtils.receiptString(
//                "VAT AMOUNT",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVat_amount()))),
//                context,
//                false);

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
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getService_charge_value()))),
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


        if (transactionCompleteDetails.transactions.getHas_special() == 1) {
            finalString += PrinterUtils.receiptString(
                    "SUB TOTAL",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(
                            Utils.roundedOffTwoDecimal(
                                    ((transactionCompleteDetails.transactions.getGross_sales() + transactionCompleteDetails.transactions.getDiscount_amount()) * 1.12) +
                                            transactionCompleteDetails.transactions.getService_charge_value()))),
                    context,
                    false);
        } else {
            finalString += PrinterUtils.receiptString(
                    "SUB TOTAL",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(                transactionCompleteDetails.transactions.getGross_sales()
                            +
                            transactionCompleteDetails.transactions.getService_charge_value()
                            +
                            transactionCompleteDetails.transactions.getDiscount_amount()))),
                    context,
                    false);
        }



//        if (transactionCompleteDetails.transactions.getHas_special() == 0) {
//            finalString += PrinterUtils.receiptString(
//                    "SUB TOTAL",
//                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(                transactionCompleteDetails.transactions.getGross_sales()
//                            +
//                            transactionCompleteDetails.transactions.getService_charge_value()
//                            +
//                            transactionCompleteDetails.transactions.getDiscount_amount()))),
//                    context,
//                    false);
//        } else {
//            finalString += PrinterUtils.receiptString(
//                    "SUB TOTAL",
//                    PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(                transactionCompleteDetails.transactions.getGross_sales()
//                            +
//                            transactionCompleteDetails.transactions.getService_charge_value()))),
//                    context,
//                    false);
//        }

//                PrinterUtils.returnWithTwoDecimal(
//                        String.valueOf(
//                                Utils.roundedOffTwoDecimal(
//                                        transactionCompleteDetails.transactions.getGross_sales()
//                                ) +
//                                        Utils.roundedOffTwoDecimal(
//                                                transactionCompleteDetails.transactions.getService_charge_value()
//                                        ) +
//                                        Utils.roundedOffTwoDecimal(
//                                                transactionCompleteDetails.transactions.getDiscount_amount()
//                                        )
//                        )),
//                context,
//                false);

        finalString += PrinterUtils.receiptString(
                "AMOUNT DUE",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(amountDue - transactionCompleteDetails.transactions.getDiscount_amount() + transactionCompleteDetails.transactions.getService_charge_value()))),
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getNet_sales()))),
                context,
                false);

        Double payments = 0.00;
        List<Integer> tmpArray = new ArrayList<>();
        String pymntType = "";
        String cardType = "";
        String cardNumber = "";
        for (Payments pym : transactionCompleteDetails.paymentsList) {
            if (!pym.getIs_void()) {
                if (!tmpArray.contains(pym.getCore_id())) {
                    tmpArray.add(pym.getCore_id());
                    pymntType = pym.getName();
                }

                if (pym.getCore_id() != 2) {
                    payments += Utils.roundedOffTwoDecimal(pym.getAmount());
                }

                if (pym.getCore_id() == 2) {
                    try {
                        JSONObject jsonObject = new JSONObject(pym.getOther_data());
                        cardType = jsonObject.getString("card_type");
                        cardNumber = jsonObject.getString("card_number");

                        int starCount = 0;
                        if (cardNumber.length() < 3) {
                            cardNumber = jsonObject.getString("card_number");
                        } else {
                            starCount = cardNumber.length() - 3;
                            String starString = new String(new char[starCount]).replace("\0", "*");
                            cardNumber = cardNumber.substring(0, cardNumber.length() - starCount) + starString;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
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
        if (tmpArray.size() > 1) {
            finalString += PrinterUtils.receiptString(
                    "PAYMENT TYPE",
                    "MULTIPLE",
                    context,
                    false);
        } else {
            finalString += PrinterUtils.receiptString(
                    "PAYMENT TYPE",
                    pymntType,
                    context,
                    false);
            if (pymntType.equalsIgnoreCase("card")) {
                finalString += PrinterUtils.receiptString(
                        cardType,
                        cardNumber,
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

        if (transactionCompleteDetails.transactions.getTransaction_type().equalsIgnoreCase("delivery")) {
            finalString += PrinterUtils.receiptString(
                    "CONTROL#",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    transactionCompleteDetails.transactions.getControl_number(),
                    "",
                    context,
                    true);

            finalString += PrinterUtils.receiptString(
                    "DELIVERY FOR",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    transactionCompleteDetails.transactions.getDelivery_to(),
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    "DELIVERY ADDRESS",
                    "",
                    context,
                    true);
            finalString += PrinterUtils.receiptString(
                    transactionCompleteDetails.transactions.getDelivery_address(),
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
                "",
                "",
                context,
                true);


        finalString += PrinterUtils.receiptString(
                "ACCRED NO:" + SharedPreferenceManager.getString(context, AppConstants.ACCRED_NO),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + transactionCompleteDetails.transactions.getTreg().split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + PrinterUtils.yearPlusFive(transactionCompleteDetails.transactions.getTreg()).split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO:" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY),
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
                    temp.add(tr.transactions.getRoom_number());
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
                "",
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "ACCRED NO:" + SharedPreferenceManager.getString(context, AppConstants.ACCRED_NO),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "DATE ISSUED : " + transactionCompleteDetails.transactions.getTreg().split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL : " + PrinterUtils.yearPlusFive(transactionCompleteDetails.transactions.getTreg()).split(" ")[0],
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "PERMIT NO:" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_NO),
                "",
                context,
                true);

        finalString += PrinterUtils.receiptString(
                "DATE ISSUED :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_VALIDITY),
                "",
                context,
                true);
        finalString += PrinterUtils.receiptString(
                "VALID UNTIL :" + SharedPreferenceManager.getString(null, AppConstants.PERMIT_ISSUED),
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
