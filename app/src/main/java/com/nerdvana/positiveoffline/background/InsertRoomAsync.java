package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomResponse;
import com.nerdvana.positiveoffline.apiresponses.RoomRateMain;
import com.nerdvana.positiveoffline.apiresponses.RoomRateSub;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.model.RoomTableModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsertRoomAsync extends AsyncTask<Void, Void, Void> {
    private SyncCallback syncCallback;
    private List<FetchRoomResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertRoomAsync(List<FetchRoomResponse.Result> list, SyncCallback syncCallback,
                                 DataSyncViewModel dataSyncViewModel, Context context) {
        this.syncCallback = syncCallback;
        this.list = list;
        this.dataSyncViewModel = dataSyncViewModel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        List<RoomTableModel> productsModelList = new ArrayList<>();

        for (FetchRoomResponse.Result r : list) {
            List<RoomRateMain> roomRateMainList = new ArrayList<>();
            List<Integer> tempList = new ArrayList<>();



            for (RoomRateSub rateSub : r.getRoomRate()) {
                if (!tempList.contains(rateSub.getRoomRatePriceId())) {
                    if (rateSub.getRatePrice() != null) {
                        roomRateMainList.add(
                                new RoomRateMain(rateSub.getId(), rateSub.getRoomRatePriceId(),
                                        r.getRoomTypeId(),rateSub.getCreatedBy(),
                                        rateSub.getCreatedAt(), rateSub.getUpdatedAt(),
                                        rateSub.getDeletedAt(), rateSub.getRatePrice())
                        );
                        tempList.add(rateSub.getRoomRatePriceId());
                    }

                }
            }

            if (r.getType() != null) {
                if (r.getType().getParent() != null) {
                    for (RoomRateMain p : r.getType().getParent().getRoomRate()) {
                        if (p.getRatePrice() != null) {
                            if (!tempList.contains(p.getRoomRatePriceId())) {
                                roomRateMainList.add(p);
                                tempList.add(p.getRoomRatePriceId());
                            }
                        }
                    }
                }

                if (r.getType().getRoomRate() != null) {
                    if (r.getType().getRoomRate().size() > 0) {
                        for (RoomRateMain rateList : r.getType().getRoomRate()) {
                            if (rateList.getRatePrice() != null) {

                                if (!tempList.contains(rateList.getRoomRatePriceId())) {
                                    roomRateMainList.add(rateList);
                                    tempList.add(rateList.getRoomRatePriceId());

                                }


                            }

                        }
                    }
                }

            }


            productsModelList.add(
                    new RoomTableModel (
                            r.getId(),
                            r.getRoomTypeId(),
                            r.getType().getRoomType(),
                            0, //r.getType().getParent() == null ? 0 : r.getType().getParent().getId(),
                            "test parent", //r.getType().getParent() == null ? "NONE" : r.getType().getParent().getRoomType(),
                            r.getRoomAreaId(),
                            r.getArea().getRoomArea(),
                            r.getStatus().getRoomStatus(),
                            r.getRoomNo(),
                            roomRateMainList,
                            true,
                            "https://imageog.flaticon.com/icons/png/512/51/51882.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF",
                            String.valueOf(r.getCRoomStat()),
                            r.getStatus().getColor(),
                            0.00,
                            false,
                            "",
                            0,
                            r.getStatus().getIsBlink() == 1 ? true : false,
                            r.getStatus().getIsTimer() == 1 ? true : false,
                            "",
                            "",
                            "",
                            0
                    )
            );
        }

        for (RoomTableModel rtm : productsModelList) {
            Rooms rooms = new Rooms(rtm.getRoomId(), rtm.getAreaName(),
                    rtm.getAreaId(), rtm.getName(),
                    rtm.getRoomType(), rtm.getRoomTypeId(), 1,
                    Integer.valueOf(rtm.getStatus()), rtm.getStatusDescription(),
                    rtm.getHexColor()
                    );
            dataSyncViewModel.insertRoom(rooms);
            for (RoomRateMain rrm : rtm.getPrice()) {
                RoomRates roomRates = new RoomRates(rrm.getRoomTypeId(), rtm.getRoomId(),
                        rrm.getRoomRatePriceId(), rrm.getRatePrice().getAmount(),
                        rrm.getRatePrice().getRoomRate().getRoomRate());
                dataSyncViewModel.insertRoomRate(roomRates);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("rooms");
    }
}
