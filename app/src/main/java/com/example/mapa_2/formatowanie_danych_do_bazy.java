package com.example.mapa_2;

import android.net.wifi.ScanResult;

import java.util.List;


public class formatowanie_danych_do_bazy {

    typ_danych_bazy_skan dane;

    formatowanie_danych_do_bazy(List<ScanResult> rezultat_skanu, wspułżedne XY)
    {
        dane=new typ_danych_bazy_skan();
        dane.XY=XY;
        this.dane.AP = wypisz_skanowanie(rezultat_skanu);
    }

    public typ_danych_bazy_skan get()
    {
        return this.dane;
    }
    private static skan[] wypisz_skanowanie(List<ScanResult> dane_punktów) {
        skan[] skany = new skan[dane_punktów.size()];
        int j=0;
        for (ScanResult i : dane_punktów) {
            skany[j]=new skan();
            skany[j].Nazwa =i.SSID;
            skany[j].MAC =i.BSSID;
            skany[j].RSSI = i.level;
            j++;
        }
        return skany;
    }
}


