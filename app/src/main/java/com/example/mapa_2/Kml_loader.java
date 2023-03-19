package com.example.mapa_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class Kml_loader extends AsyncTask<Void, Void, Void>  {
    @SuppressLint("StaticFieldLeak")
    private final MapView mapView;

    private KmlDocument kml_Document;
    private FolderOverlay kml_Overlay;
    private Context context;
    private Stylistyka stylistyka;
    private final int floor_level;

    Kml_loader(MapView mapView,int floor_level, Context context)
    {
        this.mapView=mapView;
        this.floor_level=floor_level;
        this.context=context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        createKml();
        stylistyka = new Stylistyka(context,kml_Document,mapView);
        stylistyka.zastosuj_stylistyke();

        kml_Overlay = (FolderOverlay) kml_Document.mKmlRoot.buildOverlay(mapView, null, null, kml_Document);

        return null;
    }

    private void createKml() {
        kml_Document = new KmlDocument();
        Map_Overpass map_overpass = new Map_Overpass();
        String tag = "level=" + floor_level;
        BoundingBox boxA = new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197);
        String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
        map_overpass.addInKmlFolder(kml_Document.mKmlRoot,url);
    }

    KmlDocument get_Kml_Document() { return kml_Document; }

    FolderOverlay get_Kml_Overlay() { return kml_Overlay;}

    int get_FloorLevel() { return floor_level; }

    public Stylistyka get_stylistyka() {return stylistyka;}

}
