package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.apiresponses.FetchPosToTransactions;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

public interface ToTransactionsContract {
    void clicked(FetchPosToTransactions.ToTransactionsData transactions);
}
