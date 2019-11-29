package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;

public interface DiscountsContract {
    void clicked(DiscountWithSettings discountWithSettings);
    void clicked(TransactionWithDiscounts transactionWithDiscounts);
}
