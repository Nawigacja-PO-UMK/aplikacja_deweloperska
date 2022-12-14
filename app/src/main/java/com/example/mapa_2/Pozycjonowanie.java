package com.example.mapa_2;

import android.content.Context;
import android.widget.TextView;

public class Pozycjonowanie {

        private Context kontekst;
        private Wifi_Manager WIFI;
        private String plikBazy;

    private Baza baza;

        Pozycjonowanie(Context kontekst,String plikBazy) {
            baza= new Baza(plikBazy,kontekst);
            WIFI = new Wifi_Manager(kontekst);
            this.plikBazy=plikBazy;
            this.kontekst=kontekst;
        }

        public void Wświetl_czujniki(TextView wifi) {
            wypisz akcjaskanu= new wypisz(wifi);
            WIFI.Akcje_Wifi(akcjaskanu);
        }

        public void zapisz_skan_do_Bazy(float X,float Y,double Z)
        {
            wspułżedne xy=new wspułżedne();
            xy.X=X;
            xy.Y=Y;
            xy.Z=Z;
            zapisywanie_wifi_do_Bazy sesja= new zapisywanie_wifi_do_Bazy(baza,xy);
            WIFI.Akcje_Wifi(sesja);
        }
        public void zapisz_skan_do_Bazy(wspułżedne xy)
        {
            zapisywanie_wifi_do_Bazy sesja= new zapisywanie_wifi_do_Bazy(baza,xy);
            WIFI.Akcje_Wifi(sesja);
        }
        public void odczytaj_pozycje(znacznik_Pozycji znacznik)
        {
            odczytywanie_pozycji sesja=new odczytywanie_pozycji(baza,kontekst,znacznik);
            WIFI.Akcje_Wifi(sesja);
        }

        public void dynamiczne_funkcje_na_wifi(Akcje_na_Wifi akcja)
        {
            WIFI.Akcje_Wifi(akcja);
        }

        public String wypisz_zawartość_bazy() {

            typ_danych_bazy_skan[] skany = baza.odczytaj_dane();
            String wypisz = new String("");
            if(skany!= null) {
                for (int i = skany.length-1; i >=0 ; i--) {
                    wypisz += "pozycja :" + String.valueOf(skany[i].XY.Y) + " " + String.valueOf(skany[i].XY.X) +" " + String.valueOf(skany[i].XY.Z)+ "\n";
                    wypisz += "liczba zarejestrowanych punktów" + String.valueOf(skany[i].AP.length) + "\n";
                }
            }
            return wypisz;
        }

        public void Kasuj_baze_skanów()
        {
            baza.kasuj();
        }

        public void wyślij_skany_do_bazy()
        {
            baza.wysyłanie_na_Serwer();
        }

}

