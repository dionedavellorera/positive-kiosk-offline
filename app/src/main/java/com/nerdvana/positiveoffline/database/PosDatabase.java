package com.nerdvana.positiveoffline.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nerdvana.positiveoffline.dao.CashDenominationDao;
import com.nerdvana.positiveoffline.dao.CreditCardsDao;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.DiscountSettingsDao;
import com.nerdvana.positiveoffline.dao.DiscountsDao;
import com.nerdvana.positiveoffline.dao.OrderDiscountsDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.PaymentTypeDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;

@Database(entities = {  User.class, DataSync.class,
                        Products.class, Transactions.class,
                        Orders.class, PaymentTypes.class,
                        Payments.class, CreditCards.class,
                        CashDenomination.class, Discounts.class,
                        DiscountSettings.class, OrderDiscounts.class},
          version = 36)

public abstract class PosDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract DataSyncDao dataSyncDao();
    public abstract ProductsDao productsDao();
    public abstract TransactionsDao transactionsDao();
    public abstract OrdersDao ordersDao();
    public abstract OrderDiscountsDao orderDiscountsDao();
    public abstract PaymentTypeDao paymentTypeDao();
    public abstract PaymentsDao paymentsDao();
    public abstract CreditCardsDao creditCardsDao();
    public abstract CashDenominationDao cashDenominationDao();
    public abstract DiscountsDao discountsDao();
    public abstract DiscountSettingsDao discountSettingsDao();
}
