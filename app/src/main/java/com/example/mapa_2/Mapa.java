package com.example.mapa_2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Mapa {

    private MapView mapView;
    private IMapController mapController;
    Context kontekst;
    private int level;
    private int levelmax;
    private int levelmin;
    int id_map;
     Kml_loader loadKml;

    Mapa(Context kontekst, MapView mapView, int raw_map) {
        this.kontekst = kontekst;
        this.id_map = raw_map;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint point2 = new GeoPoint(53.01699, 18.60282);
        IMapController mapController = mapView.getController();
        mapController.setCenter(point2);
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        levelmax=2;
        levelmin=-1;
        level = 0;
        wczytywanie_mapy(level);
    }

    public int level() {
        return level;
    }

    public int levelmax()
    {
        return levelmax;
    }

    public int Levelmin()
    {
        return levelmin;
    }
    private void wczytywanie_mapy(int level) {

        loadKml= (Kml_loader) new Kml_loader(kontekst,mapView,level).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public void wczytaj_nowa_mape(int level)
    {
        this.level=level;
        mapView.getOverlays().clear();
        mapView.invalidate();
        wczytywanie_mapy(level);
    }

    public wspułżedne odczytaj_wspułrzędne()
    {
        wspułżedne XY=new wspułżedne();
        GeoPoint punkt=loadKml.znacznik.getPosition();
        XY.X=punkt.getLatitude();
        XY.Y=punkt.getLongitude();
        XY.Z=level;
        return XY;
    }
}
