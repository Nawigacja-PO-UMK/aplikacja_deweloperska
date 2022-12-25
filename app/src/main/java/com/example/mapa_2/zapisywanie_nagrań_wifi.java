package com.example.mapa_2;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class zapisywanie_nagrań_wifi extends zapisywanie_wifi_do_Bazy{
    Date czas_początkowy;

    zapisywanie_nagrań_wifi(Baza baza,Context kontekst, wspułżedne XY,boolean czynagrywać)
        {
            super(baza,kontekst,XY,czynagrywać);
            this.czas_początkowy =new Date();
        }

        @Override
        public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {

        Date obecny_czas=new Date();
        XY.X=(obecny_czas.getTime()-czas_początkowy.getTime())/1000;
            baza.Zapisywanie_do_Bazy(rezultat_skanu,XY);
            if(!czynagrywać)
                Toast.makeText(kontekst, "zapisane", Toast.LENGTH_SHORT).show();
        }
}

