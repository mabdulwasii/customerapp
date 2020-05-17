package com.mykab.rider.models;

import com.mykab.rider.json.fcm.FCMType;

import java.io.Serializable;

public class OrderFCM implements Serializable {
    public int type = FCMType.ORDER;
    public String id_rider;
    public String id_transaksi;
    public String response;
}