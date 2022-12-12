package com.example.mapa_2;

import android.content.Context;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class nasłuchiwanie_znacznika_pozycji implements Marker.OnMarkerDragListener {
    Context kontekst;
    nasłuchiwanie_znacznika_pozycji(Context kontekst)
    {
        this.kontekst=kontekst;
    }
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double X,Y;
        GeoPoint pozycja = marker.getPosition();
        X=pozycja.getLatitude();
        Y=pozycja.getLongitude();
        marker.setTitle("X="+X+" Y="+Y);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }
}
