package com.nerdvana.positiveoffline.view.resumetransaction;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.ResumeTransactionAdapter;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.viewmodel.RoomsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ResumeTransactionActivity extends AppCompatActivity implements TransactionsContract {
    private UserViewModel userViewModel;
    private TextView tvNoData;
    private RecyclerView rvTransactionList;
    private Toolbar toolbar;
    private TextView title;
    private EditText search;

    private ResumeTransactionAdapter resumeTransactionAdapter;
    private TransactionsViewModel transactionsViewModel;
    private RoomsViewModel roomsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_transaction);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUserViewModel();
        initTransViewModel();
        initTransViewModelListener();
        initRoomsViewModel();
        setTitle();

    }

    private void setTitle() {
        title.setText("Select transaction to resume");
    }

    private void initTransViewModelListener() {
        transactionsViewModel.savedTransactionLiveData().observe(this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                if (transactions.size() > 0) {
                    rvTransactionList.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    rvTransactionList.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
                setTransactionAdapter(transactions);
            }
        });
    }

    private void initRoomsViewModel() {
        roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
    }


    private void initTransViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void initViews() {
        title = findViewById(R.id.title);
        search = findViewById(R.id.search);
        initSearchListener();
        toolbar = findViewById(R.id.toolbar);
        tvNoData = findViewById(R.id.tvNoData);
        rvTransactionList = findViewById(R.id.rvTransactionList);
    }

    private void initSearchListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (resumeTransactionAdapter != null) {
                    resumeTransactionAdapter.getFilter().filter(editable);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    void returnSuccessData(String transactionId) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.TRANS_ID, transactionId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTransactionAdapter(List<Transactions> transactionsList) {
        resumeTransactionAdapter = new ResumeTransactionAdapter(transactionsList, this, this);
        rvTransactionList.setAdapter(resumeTransactionAdapter);
        rvTransactionList.setLayoutManager(new GridLayoutManager(this, 5));
        resumeTransactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void clicked(Transactions transactions) {
        returnSuccessData(String.valueOf(transactions.getId()));
    }

    @Override
    public void remove(Transactions transactions) {
        Transactions tr = null;
        tr = new Transactions(
                transactions.getId(),
                transactions.getControl_number(),
                transactions.getUser_id(),
                transactions.getIs_void(),
                transactions.getIs_void_by(),
                transactions.getIs_completed(),
                transactions.getIs_completed_by(),
                transactions.getIs_saved(),
                transactions.getIs_saved_by(),
                transactions.getIs_cut_off(),
                transactions.getIs_cut_off_by(),
                transactions.getTrans_name(),
                transactions.getTreg(),
                transactions.getReceipt_number(),
                transactions.getGross_sales() == null ? 0.00 : transactions.getGross_sales(),
                transactions.getNet_sales() == null ? 0.00 : transactions.getNet_sales(),
                transactions.getVatable_sales() == null ? 0.00 :transactions.getVatable_sales(),
                transactions.getVat_exempt_sales() == null ? 0.00 : transactions.getVat_exempt_sales(),
                transactions.getVatable_sales() == null ? 0.00 : transactions.getVatable_sales(),
                transactions.getDiscount_amount() == null ? 0.00 : transactions.getDiscount_amount(),
                transactions.getChange(),
                transactions.getVoid_at(),
                transactions.getCompleted_at(),
                transactions.getSaved_at(),
                transactions.getIs_cut_off_at(),
                true,
                transactions.getIs_cancelled_by(),
                Utils.getDateTimeToday(),
                transactions.getTin_number()
        );
        tr.setRoom_id(transactions.getRoom_id());
        tr.setRoom_number(transactions.getRoom_number());
        tr.setMachine_id(transactions.getMachine_id());
        tr.setIs_sent_to_server(transactions.getIs_sent_to_server());
        tr.setBranch_id(transactions.getBranch_id());

        try {
            Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(transactions.getId());
            changeRoomStatus(tmpRm, 3, true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        transactionsViewModel.update(tr);
    }

    @Override
    public void clicked(TransactionWithOrders transactionWithOrders) {

    }

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void changeRoomStatus(Rooms room, int status_id, boolean is_transfer) {
        try {

            RoomStatus roomStatus = roomsViewModel.getRoomStatusViaId(status_id);
            room.setStatus_id(roomStatus.getCore_id());
            room.setStatus_description(roomStatus.getRoom_status());
            room.setHex_color(roomStatus.getHex_color());

            if (is_transfer) {
                room.setTransaction_id("");
            }
            roomsViewModel.update(room);
        } catch (Exception e) {

        }
    }
}
