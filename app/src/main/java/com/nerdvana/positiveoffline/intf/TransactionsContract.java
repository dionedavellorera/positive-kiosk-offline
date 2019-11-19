package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

public interface TransactionsContract {
    void clicked(Transactions transactions);
    void clicked(TransactionWithOrders transactionWithOrders);
}
