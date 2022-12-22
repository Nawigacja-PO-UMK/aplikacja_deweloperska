package com.example.mapa_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Baza {

    private String plikBazy;
    private Context kontekst;
    private JSONArray Bazadanych;
    private URL url;
    private SharedPreferences plik;
    private SharedPreferences.Editor edytor;

    public Baza(String plikBazy,Context kontekst,URL url)  {
        this.plikBazy=plikBazy;
        this.kontekst=kontekst;
        this.url=url;
        Bazadanych = new JSONArray();
    }

    public typ_danych_bazy_skan[] odczytaj_dane()  {

        String JSON=odczytaj_plik();
        if( JSON!=null) {
           return parsowanie_z_JSON(JSON);
        }
        return null;
    }
    public String odczytaj_plik()
    {
        plik = kontekst.getSharedPreferences(plikBazy, Context.MODE_PRIVATE);
        String dane=plik.getString("baza","");
        return dane;

    }
    private void Zapisywanie_do_pliku(String dane)
    {
        plik = kontekst.getSharedPreferences(plikBazy, Context.MODE_PRIVATE);
        plik.edit().putString("baza",dane).commit();
    }
    public void Zapisywanie_do_Bazy(List<ScanResult> rezultat_skanu, wspułżedne XY)
    {
        formatowanie_danych_do_bazy dane= new formatowanie_danych_do_bazy(rezultat_skanu,XY);
        String JSON=parsowanie_do_JSON(dane.get());
        if(JSON!=null) {
            Zapisywanie_do_pliku(JSON);
            Toast.makeText(kontekst, "zapisane", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(kontekst, "nie można zapisać", Toast.LENGTH_LONG).show();
    }

   private String parsowanie_do_JSON(typ_danych_bazy_skan dane)
    {
        try {
            //klasa wspułzedne
            JSONObject xy= new JSONObject();
            xy.put("X",dane.XY.X);
            xy.put("Y",dane.XY.Y);
            xy.put("Z",dane.XY.Z);
            JSONArray lista_punktów=new JSONArray();
           for (skan sk: dane.AP) {
                //pojedyńczy zeskanowany router
                JSONObject punkt = new JSONObject();
                punkt.put("Name", sk.Nazwa);
                punkt.put("MAC", sk.MAC);
                punkt.put("RSSI", sk.RSSI);
                ///klasa
                lista_punktów.put(punkt);
            }
            JSONObject skan=new JSONObject();
            skan.put("XY",xy);
            skan.put("skan",lista_punktów);
            Bazadanych.put(skan);
             return Bazadanych.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private typ_danych_bazy_skan[] parsowanie_z_JSON( String dane)
    {

        try {
            JSONArray skany=new JSONArray(dane);
            typ_danych_bazy_skan[]  wynik = new typ_danych_bazy_skan[skany.length()];
            for (int j=0;j<skany.length();j++) {
                JSONObject skan = skany.getJSONObject(j);
                wynik[j]=new typ_danych_bazy_skan();
                ///klasa wspułżednych
                JSONObject xy = skan.getJSONObject("XY");
                wynik[j].XY = new wspułżedne();
                wynik[j].XY.X =  xy.getDouble("X");
                wynik[j].XY.Y =  xy.getDouble("Y");
                wynik[j].XY.Z =  xy.getDouble("Z");
                // skany
                JSONArray listapunktów = skan.getJSONArray("skan");
                wynik[j].AP = new skan[listapunktów.length()];
                for (int i = 0; i < listapunktów.length(); i++) {
                    wynik[j].AP[i] = new skan();
                    JSONObject punkt = listapunktów.getJSONObject(i);

                    wynik[j].AP[i].Nazwa = punkt.getString("Name");
                    wynik[j].AP[i].MAC = punkt.getString("MAC");
                    wynik[j].AP[i].RSSI = punkt.getInt("RSSI");
                }
            }
            return wynik;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    void wysyłanie_na_Serwer()
    {
        ConnectivityManager łączę =(ConnectivityManager) kontekst.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(łączę.getActiveNetworkInfo()!=null &&  łączę.getActiveNetworkInfo().isConnected()))
        {
            Toast.makeText(kontekst,"włacz internet",Toast.LENGTH_LONG).show();
            return;
        }
        String baza=odczytaj_plik();
        StringRequest WysyłaneDane= new StringRequest(Request.Method.POST, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(kontekst,response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(kontekst,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> wystłane= new HashMap<String,String>();
                wystłane.put("dane",baza);
                return wystłane;
            }
        };
        RequestQueue gniazdo= Volley.newRequestQueue(kontekst);
        gniazdo.add(WysyłaneDane);
    }

    public void kasuj()
    {
        plik = kontekst.getSharedPreferences(plikBazy, Context.MODE_PRIVATE);
        plik.edit().remove("baza").commit();
        Bazadanych=new JSONArray();
    }
}
