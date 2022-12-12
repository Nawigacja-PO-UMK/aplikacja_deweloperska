package com.example.mapa_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

class Kml_loader extends AsyncTask<Void, Void, Void>  {
        ProgressDialog progressDialog;
        KmlDocument kmlDocument;
        znacznik_Pozycji znacznik;

        private Context kontekst;
        private MapView mapView;
        int IDmap;
        most most;

        Kml_loader(Context kontekst,most most, MapView mapView,int IDmap)
        {
            this.IDmap=IDmap;
         this.kontekst=kontekst;
         this.mapView=mapView;
         this.progressDialog = new ProgressDialog(kontekst);
         this.most=most;
        }



    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading Project...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids){
            kmlDocument = new KmlDocument();
            kmlDocument.parseKMLStream(kontekst.getResources().openRawResource(R.raw.mapa), null);
            FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(mapView, null, null,kmlDocument);
            mapView.getOverlays().add(kmlOverlay);
            ///dodawanieznacznika
            dodawanie_znacznika_lokalizacji();
            mapView.setBackgroundColor(2000);
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            mapView.invalidate();
            BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
            mapView.zoomToBoundingBox(bb, true);
//            mapView.getController().setCenter(bb.getCenter());
            super.onPostExecute(aVoid);
        }


    void dodawanie_znacznika_lokalizacji()
    {
        znacznik= new znacznik_Pozycji(mapView,kontekst);
        nasłuchiwanie_znacznika_pozycji nasłuchiwanie=new nasłuchiwanie_znacznika_pozycji(kontekst);
        znacznik.setOnMarkerDragListener(nasłuchiwanie);
        mapView.getOverlays().add(znacznik);

    }
}
