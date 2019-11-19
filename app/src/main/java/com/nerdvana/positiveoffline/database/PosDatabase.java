package com.nerdvana.positiveoffline.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nerdvana.positiveoffline.dao.CreditCardsDao;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.PaymentTypeDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;

@Database(entities = {User.class, DataSync.class,
                      Products.class, Transactions.class,
                        Orders.class, PaymentTypes.class,
                        Payments.class, CreditCards.class},
          version = 18)

public abstract class PosDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract DataSyncDao dataSyncDao();
    public abstract ProductsDao productsDao();
    public abstract TransactionsDao transactionsDao();
    public abstract OrdersDao ordersDao();
    public abstract PaymentTypeDao paymentTypeDao();
    public abstract PaymentsDao paymentsDao();
    public abstract CreditCardsDao creditCardsDao();
}
