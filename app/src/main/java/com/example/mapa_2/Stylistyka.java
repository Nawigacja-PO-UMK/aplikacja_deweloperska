package com.example.mapa_2;

import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.j256.ormlite.stmt.NullArgHolder;
import com.j256.ormlite.stmt.query.IsNotNull;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class Stylistyka {

    Context context;
    KmlDocument kmlDocument;
    MapView mapView;

    Hashtable<String, Etykiety> etykieta_hashtable = new Hashtable<>();
    Hashtable<String, Pomieszczenia> pomieszczenie_hashtable = new Hashtable<>();
    Pomieszczenia main_layout;

    List <String> lista_pomieszczen;
   // Overlay overlay_color = new Overlay();

    @SuppressLint("UseCompatLoadingForDrawables")
    public Stylistyka(Context context, KmlDocument kmlDocument, MapView mapView)
    {
        this.context=context;
        this.kmlDocument=kmlDocument;
        this.mapView=mapView;
        this.main_layout = new Pomieszczenia(mapView,WHITE);

        lista_pomieszczen = Arrays.asList("room","cloakroom","elevator","corridor","office",
                "restaurant","class","steps","toilets","default","storage","toilet","library");
        for(String napis:lista_pomieszczen)
        {
            Drawable drawable;
            int color=0;
            switch (napis)
            {
                case "library":
                {
                    color=Color.rgb(160,186,97);
                    drawable= context.getResources().getDrawable(R.drawable.book);
                    break;
                }
                case "class" :{
                    color=Color.rgb(201,137,169);
                    drawable= context.getResources().getDrawable(R.drawable.sala);
                    break;
                }
                case "toilet": case "toilets" :{
                    color=Color.rgb(55,184,201);
                    drawable= context.getResources().getDrawable(R.drawable.wc);
                    break;
                }
                case "restaurant" : {
                    color=Color.rgb(122,200,94);
                    drawable = context.getResources().getDrawable(R.drawable.bar);
                    break;
                }
                case "staircase": case "steps" :{
                    color=Color.rgb(255,201,14);
                    drawable= context.getResources().getDrawable(R.drawable.stairs);
                    break;
                }
                case "corridor" :{
                    color=Color.rgb(222,222,222);
                    drawable= context.getResources().getDrawable(R.drawable.corridor);
                    break;
                }
                case "room":{
                    color=Color.rgb(152,200,200);
                    drawable= context.getResources().getDrawable(R.drawable.room);
                    break;
                }
                case "storage":{
                    color=Color.rgb(152,166,120);
                    drawable= context.getResources().getDrawable(R.drawable.box);
                    break;
                }
                case "office":{
                    color=Color.rgb(168,81,189);
                    drawable= context.getResources().getDrawable(R.drawable.office);
                    break;
                }
                case "concierge":{
                    color=Color.rgb(222,222,222);
                    drawable= context.getResources().getDrawable(R.drawable.concierge);
                    break;
                }
                case "cloakroom":{
                    color=Color.rgb(186,7,7);
                    drawable= context.getResources().getDrawable(R.drawable.hat);
                    break;
                }
                case "elevator":{
                    color=Color.rgb(224,222,52);
                    drawable= context.getResources().getDrawable(R.drawable.elevator);
                    break;
                }

                default: {
                    color=Color.rgb(222,222,222);
                    drawable= context.getResources().getDrawable(R.drawable.defaultl);
                    break;
                }
            }

            Pomieszczenia pomieszczenia = new Pomieszczenia(mapView,color);
            pomieszczenie_hashtable.put(napis,pomieszczenia);

            if(drawable!=null) {
                Etykiety etykiety = new Etykiety(drawable);
                etykieta_hashtable.put(napis, etykiety);
            }
        }
    }

    public void zastosuj_stylistyke() {
        ArrayList<KmlFeature> lista = kmlDocument.mKmlRoot.mItems;
        ArrayList<Integer>to_delete = new ArrayList<>();

        int i = 0;
        for (KmlFeature feature : lista) {

            KmlGeometry geometry = ((KmlPlacemark) (feature)).mGeometry;
            Polygon polygon = new Polygon();
            polygon.setPoints(geometry.mCoordinates);

            IGeoPoint punkt = new GeoPoint(feature.getBoundingBox().getCenterLatitude(), feature.getBoundingBox().getCenterLongitude());
            OverlayItem obiekt = new OverlayItem(feature.mName, feature.mName, punkt);

            String nazwa = feature.getExtendedData("room");

            if(nazwa == null) {
                nazwa = feature.getExtendedData("indoor");
                if(nazwa == null) {
                    nazwa = feature.getExtendedData("highway");
                    if(nazwa == null) {
                        nazwa = feature.getExtendedData("amenity");
                        if (nazwa == null)
                            nazwa = "default";
                        else {
                            main_layout.Dodaj_pomieszczenie(feature);
                            to_delete.add(i);
                            continue;
                        }
                    }
                }
            }
            i++;


            pomieszczenie_hashtable.get(nazwa).Dodaj_pomieszczenie(feature);
            etykieta_hashtable.get(nazwa).addOverlay(obiekt);

        }

        for (Integer d: to_delete) {
            kmlDocument.mKmlRoot.removeItem(d);
        }
    }
    public void dodaj_warstwy_kolorow()
    {
        main_layout.dodaj_warstwy();
        for (String nazwa:lista_pomieszczen) {
            pomieszczenie_hashtable.get(nazwa).dodaj_warstwy();
        }
    }

    public void dodaj_warstwy_etykiet()
    {
        for (String nazwa:lista_pomieszczen) {
            mapView.getOverlays().add(etykieta_hashtable.get(nazwa));
        }
    }
}
