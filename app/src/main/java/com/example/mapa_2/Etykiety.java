package com.example.mapa_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;

public class Etykiety extends Overlay {

    private ArrayList<Overlay> markers;
    private Drawable icon;

    public Etykiety(Drawable icon)
    {
        super();
        this.icon = icon;
        markers = new ArrayList<Overlay>();
    }

    public void addMarker(Context context, MapView mapView, IGeoPoint punkt)
    {
        ScaledMarker scaledMarker = new ScaledMarker(context, mapView,punkt, icon);
        markers.add(scaledMarker);
    }

    public void addMarker(Context context, MapView mapView, IGeoPoint punkt, String name, int iconHeight)
    {
        MarkerText markerText = new MarkerText(context,name,punkt,iconHeight);
        markers.add(markerText);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas,mapView,shadow);

        for (Overlay marker : markers) {
            marker.draw(canvas, mapView, shadow);
        }
    }
}
