package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.ArOnlineResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomStatusResponse;
import com.nerdvana.positiveoffline.apiresponses.TakasTypeResponse;
import com.nerdvana.positiveoffline.entities.ArOnline;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.Takas;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.repository.DataSyncRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataSyncViewModel extends AndroidViewModel {

    private DataSyncRepository mRepository;
    private LiveData<List<DataSync>> mSyncData;

    public DataSyncViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataSyncRepository(application);
        mSyncData = mRepository.getAllSyncList();
    }


    public LiveData<List<DataSync>> getmSyncData() {
        return mSyncData;
    }

    public List<DataSync> getUnsyncedData() throws ExecutionException, InterruptedException {
        return mRepository.getUnsyncedList();
    }

    public void updateIsSynced(DataSync dataSync) {
        mRepository.update(dataSync);
    }

    public void truncatePrinterSeries() {
        mRepository.truncatePrinterSeries();
    }


    public void truncatePaymentTypes() {
        mRepository.truncatePaymentTypes();
    }

    public void truncateProducts() {
        mRepository.truncateProducts();
    }

    public void truncateDiscounts() {
        mRepository.truncateDiscounts();
    }



    public void truncatePrinterLanguage() {
        mRepository.truncatePrinterLanguage();
    }

    public void truncateThemeSelection() {
        mRepository.truncateThemeSelection();
    }

    public void truncateArOnline() {
        mRepository.truncateArOnline();
    }

    public void truncateTakas() {
        mRepository.truncateTakas();
    }

    public void updatePrinterSeries(PrinterSeries printerSeries) {
        mRepository.update(printerSeries);
    }

    public void updatePrinterLanguage(PrinterLanguage printerLanguage) {
        mRepository.update(printerLanguage);
    }

    public void updateThemeSelection(List<ThemeSelection> themeSelection) {
        mRepository.update(themeSelection);
    }

    public void insertData(List<DataSync> list) {
        mRepository.insert(list);
    }

    public void requestCashDenomination() {
        mRepository.fetchCashDenominationRequest();
    }

    public void requestDiscounts() {
        mRepository.fetchDiscounts();
    }

    public void requestRoomsOrTables() {
        mRepository.fetchRoom();
    }

    public void fetchRoomStatus() {
        mRepository.fetchRoomStatus();
    }

    public void fetchArOnline() {
        mRepository.fetchArOnline();
    }

    public void fetchTakasType() {
        mRepository.fetchTakasType();
    }

    public MutableLiveData<ArOnlineResponse> getArOnlineLiveData() {
        return mRepository.getFetchArOnlineLiveData();
    }

    public MutableLiveData<TakasTypeResponse> getTakasTypeLiveData() {
        return mRepository.getFetchTakasTypeLiveData();
    }

    public MutableLiveData<FetchDiscountResponse> getDiscountLiveData() {
        return mRepository.getFetchDiscountLiveData();
    }

    public List<PrinterSeries> getPrinterSeries() throws ExecutionException, InterruptedException {
        return mRepository.getPrinterSeriesList();
    }

    public List<PrinterLanguage> getPrinterLanguage() throws ExecutionException, InterruptedException {
        return mRepository.getPrinterLanguageList();
    }

    public List<ThemeSelection> getThemeSelectionList() throws ExecutionException, InterruptedException {
        return mRepository.getThemeSelectionList();
    }

    public PrinterSeries getActivePrinterSeries() throws ExecutionException, InterruptedException {
        return mRepository.getActivePrinterSeries();
    }

    public PrinterLanguage getActivePrinterLanguage() throws ExecutionException, InterruptedException {
        return mRepository.getActivePrinterLanguage();
    }

    public MutableLiveData<FetchCashDenominationResponse> getCashDenoLiveData() {
        return mRepository.getFetchCashDenominationLiveData();
    }

    public MutableLiveData<FetchRoomStatusResponse> getRoomStatusLiveData() {
        return mRepository.getFetchRoomStatusLiveData();
    }

    public List<CashDenomination> getCashDeno() throws ExecutionException, InterruptedException {
        return mRepository.getCashDenoList();
    }


    public void requestPaymentType() {
        mRepository.fetchPaymentTypeRequest();
    }

    public void requestCreditCards() {
        mRepository.fetchCreditCardRequest();
    }

    public MutableLiveData<FetchRoomResponse> getFetchRoomLiveData() {
        return mRepository.getFetchRoomLiveData();
    }

    public LiveData<List<ThemeSelection>> getThemeSelectionLiveData() {
        return mRepository.getThemeSelectionLiveData();
    }

    public MutableLiveData<FetchPaymentTypeResponse> getPaymentTypeLiveData() {
        return mRepository.getFetchPaymentTypeLiveData();
    }

    public MutableLiveData<FetchCreditCardResponse> getCreditCardLiveData() {
        return mRepository.getFetchCreditCarDLiveData();
    }

    public List<PaymentTypes> getPaymentTypeList() throws ExecutionException, InterruptedException {
        return mRepository.getPaymentTypeList();
    }

    public List<CreditCards> getCreditCardList() throws ExecutionException, InterruptedException {
        return mRepository.getCreditCardList();
    }

    public List<ArOnline> getArOnlineList() throws ExecutionException, InterruptedException {
        return mRepository.getArOnlineList();
    }

    public List<Takas> getTakasTypeList() throws ExecutionException, InterruptedException {
        return mRepository.getTakasTypeList();
    }


    public void insertPaymentType(List<PaymentTypes> list) {
        mRepository.insertPaymentType(list);
    }

    public void insertRoomStatus(List<RoomStatus> list) {
        mRepository.insertRoomStatus(list);
    }

    public void insertArOnline(List<ArOnline> list) {
        mRepository.insertArOnline(list);
    }

    public void insertTakas(List<Takas> list) {
        mRepository.insertTakas(list);
    }

    public void insertCreditCard(List<CreditCards> list) {
        mRepository.insertCreditCard(list);
    }

    public void insertPrinterSeries(List<PrinterSeries> list) {
        mRepository.insertPrinterSeries(list);
    }

    public void insertThemeSelection(List<ThemeSelection> list) {
        mRepository.insertThemeSelection(list);
    }

    public void insertPrinterLanguage(List<PrinterLanguage> list) {
        mRepository.insertPrinterLanguage(list);
    }

    public void insertCashDenomination(List<CashDenomination> list) {
        mRepository.insertCashDenomination(list);
    }

    public void insertRoom(Rooms rooms) {
        mRepository.insertRoom(rooms);
    }

    public void insertRoomRate(RoomRates roomRates) {
        mRepository.insertRoomRates(roomRates);
    }

    public void insertDiscountWithSettings(List<Discounts> list, List<DiscountSettings> discountSettingsList) {
        mRepository.insertDiscountWithSettings(list, discountSettingsList);
    }

}
