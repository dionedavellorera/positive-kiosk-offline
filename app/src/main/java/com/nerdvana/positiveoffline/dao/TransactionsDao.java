package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import java.util.List;

@Dao
public interface TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Transactions> dataSyncList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Transactions transactionData);

    @Query("SELECT * FROM Transactions WHERE is_sent_to_server = 0")
    List<Transactions> unsyncedTransactions();

    @Query("SELECT * FROM Transactions WHERE is_backed_out = 0 AND is_saved = 0 AND is_completed = 0 AND is_cut_off = 0 AND is_cancelled = 0 AND is_shared = 0")
    LiveData<List<Transactions>> ldTransactionsList();

    @Query("SELECT * FROM Transactions WHERE is_saved = 1 AND is_completed = 0 AND is_cut_off = 0 AND is_cancelled = 0 AND is_backed_out = 0 AND is_shared = 0 ORDER BY DATE(saved_at) ASC")
    LiveData<List<Transactions>> ldSavedTransactionsList();

    @Query("SELECT * FROM Transactions WHERE DATE(completed_at) BETWEEN :startDate AND :endDate AND is_completed = 1 AND is_void = 0 AND is_cut_off = 0 AND is_shared = 0 AND is_cancelled = 0 AND is_backed_out = 0")
    List<TransactionWithOrders> completedTransactionList(String startDate, String endDate);

    @Query("SELECT * FROM Transactions WHERE is_saved = 1 AND is_completed = 0 AND is_void = 0 AND is_cut_off = 0 AND is_cancelled = 0 AND is_backed_out = 0 AND is_shared = 0")
    List<TransactionWithOrders> savedTransactionsList();


    @Query("SELECT * FROM Transactions WHERE room_number != '' AND is_completed = 0 AND is_void = 0 AND is_cut_off = 0 AND is_cancelled = 0 AND is_backed_out = 0 AND is_shared = 0")
    List<TransactionWithOrders> transactionListWithRoom();

    @Query("SELECT * FROM Transactions WHERE is_saved = 0 AND is_completed = 0 AND is_cut_off = 0 AND is_cancelled = 0 AND is_backed_out = 0 AND is_shared = 0")
    List<Transactions> transactionsList();

    @Query("SELECT * FROM Transactions")
    List<Transactions> getAllTransactions();

    @Query("SELECT * FROM Transactions WHERE is_cut_off = 0 AND (is_completed = 1 OR is_void = 1)")
    List<Transactions> unCutOffTransactions();

    @Query("SELECT * FROM Transactions ORDER BY id DESC LIMIT 1")
    Transactions lastTransactionId();

    @Query("SELECT * FROM Transactions WHERE receipt_number != '' ORDER BY id DESC LIMIT 1 ")
    Transactions lastOrNumber();

    @Query("SELECT * FROM Transactions WHERE is_cut_off = 0 AND is_shared = 0 AND (is_completed = 1 OR is_void = 1) AND (is_void_by = :user_id OR is_completed_by = :user_id)")
    List<Transactions> unCutOffTransactionsByUser(String user_id);

    @Query("SELECT * FROM Transactions WHERE id = :transaction_id")
    List<Transactions> loadedTransactionList(String transaction_id);

    @Query("SELECT * FROM Transactions WHERE control_number = :control_number")
    List<Transactions> loadedTransactionListViaControlNumber(String control_number);

    @Query("SELECT * FROM Transactions t WHERE t.receipt_number = :receipt_number LIMIT 1")
    TransactionCompleteDetails getTransactionViaReceiptNumber(String receipt_number);

    @Query("SELECT * FROM Transactions t WHERE t.id = :transaction_id LIMIT 1")
    TransactionCompleteDetails getTransactionViaTransactionId(String transaction_id);

    @Update()
    void update(Transactions transaction);

    @Update()
    Integer updateLong(Transactions transaction);

}
