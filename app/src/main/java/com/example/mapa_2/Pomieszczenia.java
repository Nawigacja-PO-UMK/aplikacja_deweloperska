package com.example.mapa_2;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Pomieszczenia {

    private MapView mapView;
    private int color;
    private List<Polygon> polygon_list = new ArrayList<>();

    public Pomieszczenia(MapView mapView, int color)
    {
        this.mapView = mapView;
        this.color=color;
    }

    public void Dodaj_pomieszczenie(KmlFeature feature)
    {
        KmlGeometry geometry = ((KmlPlacemark)(feature)).mGeometry;

        Polygon polygon = new Polygon();

        polygon.setPoints(geometry.mCoordinates);
        polygon.setFillColor(color);
        polygon.setStrokeWidth(7);
        polygon.setStrokeColor(Color.GRAY);
        polygon_list.add(polygon);
    }

    public void dodaj_warstwy()
    {
        mapView.getOverlays().addAll(polygon_list);
    }


}
