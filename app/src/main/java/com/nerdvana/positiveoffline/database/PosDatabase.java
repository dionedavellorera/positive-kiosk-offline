package com.nerdvana.positiveoffline.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;

@Database(entities = {User.class, DataSync.class,
                      Products.class, Transactions.class,
                        Orders.class},
          version = 10)

public abstract class PosDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract DataSyncDao dataSyncDao();
    public abstract ProductsDao productsDao();
    public abstract TransactionsDao transactionsDao();
    public abstract OrdersDao ordersDao();
}
