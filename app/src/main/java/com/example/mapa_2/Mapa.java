package com.example.mapa_2;

import android.content.Context;
import android.os.AsyncTask;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Mapa {

    private MapView mapView;
    private IMapController mapController;
    Context kontekst;
    int id_map;
    Kml_loader loadKml;

     Mapa(Context kontekst, MapView mapView, int raw_map)
    {
        this.kontekst=kontekst;
        this.id_map=raw_map;
        this.mapView=mapView;
        most.X=-1;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint point2 = new GeoPoint(53.01699,18.60282);
        IMapController mapController = mapView.getController();
        mapController.setCenter(point2);
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        most most=new most();
        loadKml= (Kml_loader) new Kml_loader(kontekst, most,mapView,raw_map).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public wspułżedne odczytaj_wspułrzędne()
    {
        wspułżedne XY=new wspułżedne();
        GeoPoint punkt=loadKml.znacznik.getPosition();
        XY.X=punkt.getLatitude();
        XY.Y=punkt.getLongitude();
        XY.Z=loadKml.poziom_mapy;
        return XY;
    }


}
