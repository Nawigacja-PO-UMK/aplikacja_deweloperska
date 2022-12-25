package com.example.mapa_2;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import java.util.List;

public class zapisywanie_wifi_do_Bazy implements Akcje_na_Wifi{

    protected Baza baza;
    protected wspułżedne XY;
    protected boolean czynagrywać;
    protected Context kontekst;


    zapisywanie_wifi_do_Bazy(Baza baza,Context kontekst, wspułżedne XY,boolean czynagrywać)
    {
        this.XY=XY;
        this.baza=baza;
        this.czynagrywać=czynagrywać;
        this.kontekst=kontekst;
    }

    public void przestań_nagrywać()
    {
        czynagrywać=false;
    }
    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {

        baza.Zapisywanie_do_Bazy(rezultat_skanu,XY);
        if(!czynagrywać)
            Toast.makeText(kontekst, "zapisane", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results) {
        return !czynagrywać;
    }
}
