package com.example.mapa_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.kml.KmlDocument;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity{

    private Context context;
    Pozycjonowanie pozycja;
    final String Baza="daneBazy.jos";
    TextView plik;
    Switch przełocznik;
    Button skan;
    Mapa mapa;
    boolean nagrywanie=false;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.main);
        plik= (TextView) findViewById(R.id.ekran);
        przełocznik=(Switch) findViewById(R.id.zapisać);
        skan=(Button) findViewById(R.id.skanuj);
        context = this;
        mapa= new Mapa(context,findViewById(R.id.map));
        ///pozycjonowanie
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        try {
            pozycja = new Pozycjonowanie(context,Baza);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SuspiciousIndentation")
    public void LewelUpMap(View view)
    {
        if(mapa.levelmax()>mapa.level())
        mapa.wczytaj_nowa_mape(mapa.level()+1);
    }

    public void LewelDownMap(View view)
    {
        if(mapa.Levelmin()<mapa.level())
            mapa.wczytaj_nowa_mape(mapa.level()-1);
    }

    public void zmiana_trybu(View view) {
        if (przełocznik.isChecked())
            skan.setText("Zapisz skan");
        else
            skan.setText("Nagraj skany");


    }
    public void Kasuj_skany(View view)
    {
        if(przełocznik.isChecked()) {
            pozycja.Kasuj_baze_skanów();
            plik.setText(pozycja.wypisz_zawartość_bazy());
        }else {
            pozycja.Kasuj_baze_testów();
            plik.setText(pozycja.wypisz_zawartość_bazytestów());
            plik.setText(pozycja.wypisz_zawartość_bazytestów());
        }
    }
    public void Operacje_na_pozycji(View view)
    {
        if(przełocznik.isChecked()) {
            zapiszywanie_pozycji();
        }
        else
            if(nagrywanie) {
                pozycja.przestań_nagrywać_test();
                nagrywanie=false;
                skan.setText("Nagraj skany");
            }else {
                pozycja.Nagraj_test();
                nagrywanie=true;
                skan.setText("Stop ");
            }
    }
    private void odczytywanie_pozycji()
    {

        pozycja.odczytaj_pozycje(mapa.loadKml.znacznik);
    }
    private void zapiszywanie_pozycji() {
        pozycja.zapisz_skan_do_Bazy(mapa.odczytaj_wspułrzędne());
    }
    public void odczytwanie_plik(View view)
    {
        if(przełocznik.isChecked())
        plik.setText(pozycja.wypisz_zawartość_bazy());
        else
            plik.setText(pozycja.wypisz_zawartość_bazytestów());
    }
    public void wyslij_do_bazy(View view)
    {
        if(przełocznik.isChecked())
        pozycja.wyślij_skany_do_bazy();
        else
            pozycja.wyślij_testy_do_bazy();
    }
}