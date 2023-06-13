package com.example.mapa_2;

import android.content.Context;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Pozycjonowanie {

        private Context kontekst;
        private Wifi_Manager WIFI;
        private String plikBazy;
        private Baza_skanów baza;
        private baza_testów bazatestów;
        private opis_nagrania znacznik_testów;
        private zapisywanie_wifi_do_Bazy sesja_testu;

        Pozycjonowanie(Context context,String plikBazy) throws MalformedURLException {
            URL urlbazy=new URL("https://34.125.80.2/server/serwe/");
            URL urlbazatestów=new URL("https://34.125.80.2/server/serwe/baza_test.php");
            baza= new Baza_skanów(plikBazy,context,urlbazy);
            bazatestów=new baza_testów("bazatesty",context,urlbazatestów);
            WIFI = new Wifi_Manager(context);
            this.plikBazy=plikBazy;
            this.kontekst=context;
            znacznik_testów= new opis_nagrania();
            znacznik_testów.nagranie=0;

        }
        public void zapisz_skan_do_Bazy(float X,float Y,double Z)
        {
            współrzedne xy=new współrzedne();
            xy.X=X;
            xy.Y=Y;
            xy.Z=Z;
            zapisywanie_wifi_do_Bazy sesja= new zapisywanie_wifi_do_Bazy(baza,kontekst,xy,false);
            WIFI.Akcje_Wifi(sesja);
        }
        public void zapisz_skan_do_Bazy(współrzedne xy)
        {
            zapisywanie_wifi_do_Bazy sesja= new zapisywanie_wifi_do_Bazy(baza,kontekst,xy,false);
            WIFI.Akcje_Wifi(sesja);
        }
        public void odczytaj_pozycje(Znacznik_Pozycji znacznik)
        {
            odczytywanie_pozycji sesja=new odczytywanie_pozycji(baza,kontekst,znacznik);
            WIFI.Akcje_Wifi(sesja);
        }

        public void dynamiczne_funkcje_na_wifi(Akcje_na_Wifi akcja)
        {
            WIFI.Akcje_Wifi(akcja);
        }

        public String wypisz_zawartość_bazy() {

            typ_danych_bazy_skan[] skany =(typ_danych_bazy_skan[]) baza.odczytaj_dane();
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

///testy
        public void wyślij_testy_do_bazy(){bazatestów.wysyłanie_na_Serwer();}
        public void Kasuj_baze_testów() {bazatestów.kasuj();}


        public void Nagraj_test()
         {
             znacznik_testów.nagranie=(new Date()).getTime();
            sesja_testu= new zapisywanie_wifi_do_Bazy(bazatestów,kontekst,znacznik_testów,true);
            WIFI.Akcje_Wifi(sesja_testu);
        }
        public void przestań_nagrywać_test()
        {
            sesja_testu.przestań_nagrywać();
        }


    public String wypisz_zawartość_bazytestów() {
    long nagranie=0;
    double czaspoczątkowy=0;
    int lidzba_skanów=1;
        typ_danych_bazy_test[] skany = (typ_danych_bazy_test[]) bazatestów.odczytaj_dane();
        String wypisz = new String("");
        if(skany!= null) {;
            nagranie=skany[skany.length-1].opis.nagranie;
            czaspoczątkowy=skany[skany.length-1].opis.czas;
            for (int i = skany.length-1; i >=0 ; i--) {
                if(nagranie!=skany[i].opis.nagranie | i==0) {
                    wypisz += "nagranie :" + String.valueOf(nagranie) + "\n";
                    wypisz += "liczba skanów" + String.valueOf(lidzba_skanów) + "\n";
                    if(i ==0 | i==skany.length-1)
                    wypisz += "czas od: "+String.valueOf(skany[i].opis.czas)+ "sekund do: "+ String.valueOf(czaspoczątkowy)+"sekund\n";
                    else {
                        wypisz += "czas od: " + String.valueOf(skany[i + 1].opis.czas) + "sekund do: " + String.valueOf(czaspoczątkowy) + "sekund\n";
                    }
                        lidzba_skanów=1;
                    nagranie=skany[i].opis.nagranie;
                    czaspoczątkowy=skany[i].opis.czas;
                }
                lidzba_skanów++;

            }
        }
        return wypisz;
    }
}

