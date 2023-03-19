package com.example.mapa_2;

import android.content.Context;
import android.os.AsyncTask;


import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Floors {
    private final Context context;
    private final MapView mapView;
    public Znacznik_Pozycji znacznik;
    Hashtable<Integer, Kml_loader> kml_loader_hashTable;

    public Floors(Context context, MapView mapView, int[] levels)
    {
        this.context=context;
        this.mapView=mapView;
        List <Kml_loader> list = new ArrayList<>();
        kml_loader_hashTable = new Hashtable<Integer,Kml_loader>();

        for (int level:levels) {
            Kml_loader kml_loader = (Kml_loader) new Kml_loader(mapView,level,context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            try {
                kml_loader.get(1800, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
            kml_loader_hashTable.put(level,kml_loader);
        }
    }

    public void add_Floor_Overlay_with_marker(int floor_level)
    {
        Kml_loader kml_loader = kml_loader_hashTable.get(floor_level);

        assert kml_loader != null;
        KmlDocument kml_Document = kml_loader.get_Kml_Document();
        FolderOverlay kml_Overlay = kml_loader.get_Kml_Overlay();
       // Etykieta etykieta = kml_loader.get_Etykieta();
        //Etykieta etykieta2 = kml_loader.get_Etykieta2();
        Stylistyka stylistyka = kml_loader.get_stylistyka();

        stylistyka.dodaj_warstwy_kolorow();
        dodawanie_znacznika_lokalizacji(kml_Overlay);
        mapView.getOverlays().add(kml_Overlay);
        mapView.getOverlays().add(znacznik);
        //mapView.getOverlays().add(etykieta);
        //mapView.getOverlays().add(etykieta)
        stylistyka.dodaj_warstwy_etykiet();
        prepare_Area_Limit(kml_Document);
        mapView.invalidate();
    }

    void prepare_Area_Limit(KmlDocument kml_Document)
    {
        BoundingBox bb = kml_Document.mKmlRoot.getBoundingBox();
        double height = Math.abs(bb.getLatNorth() - bb.getLatSouth())/3*2;
        double width= Math.abs(bb.getLonEast() - bb.getLonWest())/3*2;

        BoundingBox cc = new BoundingBox(bb.getLatNorth() + height,bb.getLonEast()+ width,bb.getLatSouth() - height,bb.getLonWest() - width);
        mapView.setScrollableAreaLimitDouble(cc);
        mapView.zoomToBoundingBox(bb, true);
    }

    void dodawanie_znacznika_lokalizacji(FolderOverlay kmlOverlay)
    {
        znacznik= new Znacznik_Pozycji(mapView,context,1, kmlOverlay.getBounds().getCenterWithDateLine());
        nasłuchiwanie_znacznika_pozycji nasłuchiwanie = new nasłuchiwanie_znacznika_pozycji(context,1);
        znacznik.setOnMarkerDragListener(nasłuchiwanie);
    }
}
