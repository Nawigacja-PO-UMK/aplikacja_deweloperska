package com.example.mapa_2;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class znacznik_Pozycji extends Marker {
    MapView mapView;
    public znacznik_Pozycji(MapView mapView, Context kontekst) {
        super(mapView);
        GeoPoint punktstartowy;
        Drawable Icon = kontekst.getResources().getDrawable(R.drawable.ic_launcher);
        punktstartowy=new GeoPoint(mapView.getMapCenter().getLatitude(),mapView.getMapCenter().getLongitude());
        setPosition(punktstartowy);
        setAnchor(ANCHOR_CENTER,ANCHOR_BOTTOM);
        setDraggable(true);
        setIcon(Icon);
        this.mapView=mapView;
        GeoPoint pozycja = getPosition();
        setTitle("X="+pozycja.getLatitude()+" Y="+pozycja.getLongitude());
    }

    public void przesunięcię_wskaznika( GeoPoint point)
    {
        setPosition(point);
        mapView.invalidate();
    }

}
