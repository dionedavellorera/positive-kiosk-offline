package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.ProductsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsertProductAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<FetchProductsResponse.Result> list;
    private ProductsViewModel productsViewModel;
    private Context context;
    public InsertProductAsync(List<FetchProductsResponse.Result> list, SyncCallback syncCallback,
                                  ProductsViewModel productsViewModel, Context context) {
        this.syncCallback = syncCallback;
        this.list = list;
        this.productsViewModel = productsViewModel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Products> productsList = new ArrayList<>();
        for (final FetchProductsResponse.Result r : list) {

            Products product = new Products(
                    r.getCoreId(),
                    r.getProduct(),
                    r.getProductInitial() != null ? r.getProductInitial() : Utils.getInitials(r.getProduct()),
                    r.getProductBarcode() != null ? r.getProductBarcode().toString() : "",
                    r.getTaxRate(),
                    r.getImageFile() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
                    r.getAmount(),
                    r.getMarkUp(),
                    getBranchCategory(r),
                    getBranchDepartment(r),
                    r.getBranchDepartments().size() > 0 ? r.getBranchDepartments().get(0).getDepartmentId() : 0,
                    r.getBranchCategories().size() > 0 ? r.getBranchCategories().get(0).getCategoryId() : 0);


            if (r.getImageFile() != null) {
                File direct = new File(Environment.getExternalStorageDirectory()
                        + "/POS/PRODUCTS");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


                File directory = Environment.getExternalStorageDirectory();
                File file = new File(directory, "/POS/PRODUCTS/" + r.getCoreId() + ".jpg");

                if (!file.exists()) {
                    Uri downloadUri = Uri.parse("http://192.168.1.90/pos/uploads/company/product/" + r.getImageFile().toString());
                    DownloadManager.Request request = new DownloadManager.Request(
                            downloadUri);

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(r.getProduct())
                            .setDestinationInExternalPublicDir("/POS/PRODUCTS", String.valueOf(r.getCoreId()) + ".jpg");

                    mgr.enqueue(request);
                }

            }

            productsList.add(product);
        }

        productsViewModel.insert(productsList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("products");
    }

    private String getBranchCategory(FetchProductsResponse.Result r) {
        String branchCategory = "";
        if (r.getBranchCategories().size() > 0) {
            if (r.getBranchCategories().get(0).getBranchCategory() != null) {
                branchCategory = r.getBranchCategories().get(0).getBranchCategory().getCategory();
            } else {
                branchCategory = "NA";
            }
        } else {
            branchCategory = "NA";
        }
        return branchCategory;
    }

    private String getBranchDepartment(FetchProductsResponse.Result r) {
        String branchDepartment = "";
        if (r.getBranchDepartments().size() > 0) {
            if (r.getBranchDepartments().get(0).getBranchDepartment() != null) {
                branchDepartment = r.getBranchDepartments().get(0).getBranchDepartment().getDepartment();
            } else {
                branchDepartment = "NA";
            }
        } else {
            branchDepartment = "NA";
        }
        return branchDepartment;
    }


}
