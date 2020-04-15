package com.nerdvana.positiveoffline.intf;

public interface SharedTransactionsContract {
    void customerClicked(int position);
    void cashClicked(int position);
    void cardClicked(int position);
    void orderRemovedClicked(int position, int viewPosition);
    void paymentRemovedClicked(int position, int orderPosition);
}
