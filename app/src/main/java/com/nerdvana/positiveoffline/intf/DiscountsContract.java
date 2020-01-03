package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;

import java.util.concurrent.ExecutionException;

public interface DiscountsContract {
    void clicked(DiscountWithSettings discountWithSettings) throws ExecutionException, InterruptedException;
    void clicked(TransactionWithDiscounts transactionWithDiscounts);
}
