package com.example.mapa_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Mapa {

    private MapView mapView;
    private IMapController mapController;
    Context context;
    private int level;
    private int levelmax;
    private int levelmin;
    final int[] levels = {-1, 0, 1, 2};
    Floors floors;

    Kml_loader loadKml;

    Mapa(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint point2 = new GeoPoint(53.017270, 18.60300);
        IMapController mapController = mapView.getController();
        mapController.setCenter(point2);
        mapController.setZoom(19.7);

        mapView.setMinZoomLevel(19.7);
        mapView.setMaxZoomLevel(23.0);
        mapView.setMultiTouchControls(true);
        levelmax = 2;
        levelmin = -1;
        level = 0;

        floors = new Floors(context, mapView, levels);
        wczytywanie_mapy(level);
    }

    public int level() {
        return level;
    }

    public int levelmax() {
        return levelmax;
    }

    public int Levelmin() {
        return levelmin;
    }

    @SuppressLint("SuspiciousIndentation")
    private void wczytywanie_mapy(int level) {

        ConnectivityManager łączę = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(łączę.getActiveNetworkInfo() != null && łączę.getActiveNetworkInfo().isConnected()))
            Toast.makeText(context, "włacz internet", Toast.LENGTH_LONG).show();
        else floors.add_Floor_Overlay_with_marker(level);
    }

    public void wczytaj_nowa_mape(int level) {
        this.level = level;
        mapView.getOverlays().clear();
        mapView.invalidate();
        wczytywanie_mapy(level);
    }

    public współrzedne odczytaj_współrzędne() {
        współrzedne XY = new współrzedne();
        GeoPoint punkt = floors.znacznik.getPosition();
        XY.X = punkt.getLatitude();
        XY.Y = punkt.getLongitude();
        XY.Z = level;
        return XY;
    }

}
