package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.AlacartAdapter;
import com.nerdvana.positiveoffline.adapter.BranchGroupFilterAdapter;
import com.nerdvana.positiveoffline.adapter.BranchGroupProductAdapter;
import com.nerdvana.positiveoffline.adapter.SelectedBranchGroupAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.intf.BgpaContract;
import com.nerdvana.positiveoffline.intf.SelectedbgpaContract;
import com.nerdvana.positiveoffline.model.BranchGroupFilterModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class BundleCompositionDialog extends BaseDialog implements View.OnClickListener{

    private List<ProductAlacart> productAlacartList;
    private List<BranchGroup> branchGroupList;
    private List<BranchGroup> selectedBranchGroupList = new ArrayList<>();
    private RecyclerView rvAlacart;
    private RecyclerView rvBranchGroupFilterModel;
    private RecyclerView rvBranchGroupProduct;
    private RecyclerView rvSelectedProducts;
    private List<BranchGroupFilterModel> branchGroupFilterModelList = new ArrayList<>();
    List<String> tmpString = new ArrayList<>();
    private Filter filter;
    private TransactionsViewModel transactionsViewModel;
    private double qtyMultiplier;
    private BgpaContract bgpaContract;
    private SelectedbgpaContract selectedbgpaContract;

    private SelectedBranchGroupAdapter selectedBranchGroupAdapter;
    private Button btnConfirm;

    private int totalRequiredCount = 0;

    public BundleCompositionDialog(Context context, List<ProductAlacart> productAlacartList,
                                   List<BranchGroup> branchGroupList, TransactionsViewModel transactionsViewModel,
                                   double qtyMultiplier) {
        super(context);
        this.productAlacartList = productAlacartList;
        this.branchGroupList = branchGroupList;
        this.transactionsViewModel = transactionsViewModel;
        this.qtyMultiplier = qtyMultiplier;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_bundle_composition, "Bundle Composition");
        setCancelable(false);
        initViews();
        initBgpaContract();
        initSelectedBgpaContract();

        initFilterIntf();
        setAlacartAdapter();
        setBranchGroupFilter();
        setSelectedBranchGroupAdapter();
    }

    private void initSelectedBgpaContract() {
        selectedbgpaContract = new SelectedbgpaContract() {
            @Override
            public void clicked(BranchGroup branchGroup, int position) {
                int qty = selectedBranchGroupList.get(position).getSelectedQty() - 1;
                if (qty <= 0) {
                    selectedBranchGroupList.remove(position);
                    selectedBranchGroupAdapter.notifyItemRemoved(position);
                } else {
                    selectedBranchGroupList.get(position).setSelectedQty(qty);
                    selectedBranchGroupAdapter.notifyItemChanged(position);
                }
            }
        };
    }

    private void setSelectedBranchGroupAdapter() {
        selectedBranchGroupAdapter = new SelectedBranchGroupAdapter(selectedBranchGroupList, getContext(), selectedbgpaContract);
        rvSelectedProducts.setAdapter(selectedBranchGroupAdapter);
        rvSelectedProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        selectedBranchGroupAdapter.notifyDataSetChanged();
    }

    private void initBgpaContract() {
        bgpaContract = new BgpaContract() {
            @Override
            public void clicked(BranchGroup branchGroup) {
                double maxQty = 0;
                for (BranchGroup bg : branchGroupList) {
                      if (bg.getBranch_group_id().equalsIgnoreCase(branchGroup.getBranch_group_id())) {
                          maxQty = bg.getBranch_qty() * qtyMultiplier;
                          break;
                      }
                }
                int totalSelectedPerBranch = 0;
//                BranchGroup selected = null;
                boolean isExisting = false;
                int selectedPosition = 0;
                int position = 0;
                String department = "";
                for (BranchGroup selectedBg : selectedBranchGroupList) {
                    department = selectedBg.getBranch_group_name();
                    if (branchGroup.getBranch_group_id().equalsIgnoreCase(selectedBg.getBranch_group_id())) {
                        totalSelectedPerBranch += selectedBg.getSelectedQty();

                    }

                    if (branchGroup.getProduct_id() == selectedBg.getProduct_id()) {
                        isExisting = true;
                        selectedPosition = position;
                    }
                    position += 1;
                }


                if (isExisting) {
                    if (totalSelectedPerBranch < maxQty) {
                        int totalSelectedTmp = selectedBranchGroupList.get(selectedPosition).getSelectedQty() + 1;
                        selectedBranchGroupList.get(selectedPosition).setSelectedQty(totalSelectedTmp);
                        if (selectedBranchGroupAdapter != null) selectedBranchGroupAdapter.notifyItemChanged(selectedPosition);
                    } else {
                        Helper.showDialogMessage(getContext(), "Max qty reached for " + department, "Information");
                    }
                } else {
                    if (totalSelectedPerBranch < maxQty) {
                        branchGroup.setSelectedQty(1);
                        selectedBranchGroupList.add(branchGroup);
                        if (selectedBranchGroupAdapter != null) selectedBranchGroupAdapter.notifyDataSetChanged();
                    } else {
                        Helper.showDialogMessage(getContext(), "Max qty reached for " + department, "Information");
                    }
                }


            }
        };
    }

    private void initFilterIntf() {
        filter = new Filter() {
            @Override
            public void clicked(int position) {
                try {
                    setBranchGroupProductAdapter(
                            transactionsViewModel.getFilteredProductsPerCategory(String.valueOf(branchGroupFilterModelList.get(position).getBranch_group_id())),
                            bgpaContract);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void setBranchGroupFilter() {
        for (BranchGroup bg : branchGroupList) {
            if (!tmpString.contains(bg.getBranch_group_id())) {
                tmpString.add(bg.getBranch_group_id());
                branchGroupFilterModelList.add(
                        new BranchGroupFilterModel(
                                Integer.valueOf(bg.getBranch_group_id()),
                                bg.getBranch_group_name(),
                                bg.getBranch_qty() * qtyMultiplier));

                totalRequiredCount += bg.getBranch_qty() * qtyMultiplier;
            }
        }

        if (branchGroupList.size() > 0) {
            try {
                setBranchGroupProductAdapter(
                        transactionsViewModel.getFilteredProductsPerCategory(String.valueOf(branchGroupFilterModelList.get(0).getBranch_group_id())),
                        bgpaContract);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        BranchGroupFilterAdapter branchGroupFilterAdapter =
                new BranchGroupFilterAdapter(branchGroupFilterModelList, getContext(),
                        filter);
        rvBranchGroupFilterModel.setAdapter(branchGroupFilterAdapter);
        rvBranchGroupFilterModel.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        branchGroupFilterAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        rvAlacart = findViewById(R.id.rvAlacart);
        rvBranchGroupFilterModel = findViewById(R.id.rvBranchGroupFilterModel);
        rvBranchGroupProduct = findViewById(R.id.rvBranchGroupProduct);
        rvSelectedProducts = findViewById(R.id.rvSelectedProducts);
    }

    private void setAlacartAdapter() {
        AlacartAdapter alacartAdapter = new AlacartAdapter(productAlacartList, getContext());
        rvAlacart.setAdapter(alacartAdapter);
        rvAlacart.setLayoutManager(new LinearLayoutManager(getContext()));
        alacartAdapter.notifyDataSetChanged();
    }

    private void setBranchGroupProductAdapter(List<BranchGroup> branchGroupList, BgpaContract bgpaContract) {
        BranchGroupProductAdapter bgpa = new BranchGroupProductAdapter(branchGroupList, getContext(), bgpaContract);
        rvBranchGroupProduct.setAdapter(bgpa);
        rvBranchGroupProduct.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        bgpa.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dialog dialog = this;
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                int totalSelectedCount = 0;
                for (BranchGroup bg : selectedBranchGroupList) {
                    totalSelectedCount += bg.getSelectedQty();
                }
                if (totalRequiredCount == totalSelectedCount) {
                    bundleCompleted(selectedBranchGroupList);
                    dismiss();
                } else {
                    Helper.showDialogMessage(getContext(), "Please complete the package", "Information");
                }
                break;
        }
    }

    public interface Filter {
        void clicked(int position);
    }

    public abstract void bundleCompleted(List<BranchGroup> bgList);
}
