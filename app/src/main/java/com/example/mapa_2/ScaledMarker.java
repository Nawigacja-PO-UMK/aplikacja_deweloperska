package com.example.mapa_2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.Polygon;

import java.util.List;

public class ScaledMarker extends Marker {
    MarkerText markerText;

    //private Polygon mPolygon;
    private double mScale;
    private Context context;
    Paint textPaint = null;
    String mLabel = null;

    public ScaledMarker(Context context, MapView mapView, IGeoPoint geoPoint, Drawable drawable) {
        super(mapView);
        this.context = context;
        //mPolygon = polygon;
        setPosition((GeoPoint) geoPoint);
        setIcon(drawable);
        mScale = 1.0f;

        //markerText = new MarkerText(context,label,geoPoint,90);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if(mapView.getZoomLevelDouble() > 20.4){
            // Obliczamy najmniejszy prostokąt okalający polygon
            //BoundingBox boundingBox = mPolygon.getBounds();


            //Point topLeft = new Point();
            //Point bottomRight = new Point();

            // Obliczamy szerokość i wysokość markera w pikselach
            Drawable icon = getIcon();
            int iconWidth = icon.getIntrinsicWidth();
            int iconHeight = icon.getIntrinsicHeight();



           // double minsize = getDistanceToClosestWall(boundingBox.getCenter(),mPolygon);

            //double N = boundingBox.getCenterLatitude() - minsize*30000;
            //double S = boundingBox.getCenterLatitude() + minsize*30000;
            //double E = boundingBox.getCenterLongitude() + minsize*30000;
            //double W = boundingBox.getCenterLongitude() - minsize*30000;


            //IGeoPoint p1 = new GeoPoint(N, W);
           // IGeoPoint p2 = new GeoPoint(S, E);



           //  mapView.getProjection().toPixels(p1,topLeft);
            //mapView.getProjection().toPixels(p2, bottomRight);
            // Obliczamy skalę markera na podstawie szerokości i wysokości najmniejszego prostokąta okalającego
            //int scaledWidth = Math.abs(bottomRight.x - topLeft.x)/2;
           // double scaledWidth = Math.abs(bottomRight.x - topLeft.x);
          // double scaledHeight = Math.abs(topLeft.y - bottomRight.y);

            //paint.setTextSize(10 * (float)(mapView.getZoomLevelDouble()/5));

             double scaledWidth = 95;
             double scaledHeight = 95;

             //double scaledWidth = 4 * (float)(mapView.getZoomLevelDouble());
            // double scaledHeight = 4 * (float)(mapView.getZoomLevelDouble());

            double widthRatio = (double) scaledWidth / (double) iconWidth;
            double heightRatio = (double) scaledHeight / (double) iconHeight;
            mScale = Math.min(widthRatio, heightRatio);

            // Rysujemy marker w odpowiedniej skali
            Point screenPosition = mapView.getProjection().toPixels(getPosition(), null);
            int scaledIconWidth = (int) (iconWidth * mScale);
            int scaledIconHeight = (int) (iconHeight * mScale);
            Rect scaledIconBounds = new Rect(
                    screenPosition.x - scaledIconWidth / 2,
                    screenPosition.y - scaledIconHeight / 2,
                    screenPosition.x + scaledIconWidth / 2,
                    screenPosition.y + scaledIconHeight / 2
            );
            icon.setBounds(scaledIconBounds);

           // if(mLabel != null)
          //  canvas.drawText( mLabel, (float)boundingBox.getCenterLatitude(), (float)boundingBox.getCenterLongitude(), textPaint);

            icon.draw(canvas);
          //  markerText.draw(canvas,mapView,shadow);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        // Do nothing on touch events
        return false;
    }

  /* private double getDistanceToClosestWall(GeoPoint point, Polygon polygon) {
        double closestDistance = Double.MAX_VALUE;

        // Dla każdej ściany polygonu
        for (int i = 0; i < polygon.getPoints().size() - 1; i++) {
            // Oblicz wektor normalny do ściany
            GeoPoint p1 = polygon.getPoints().get(i);
            GeoPoint p2 = polygon.getPoints().get(i + 1);
            double wallNormalX = p2.getLatitude() - p1.getLatitude();
            double wallNormalY = p1.getLongitude() - p2.getLongitude();

            // Oblicz wektor od punktu do środka ściany
            GeoPoint wallCenter = new GeoPoint((p1.getLatitude() + p2.getLatitude()) / 2, (p1.getLongitude() + p2.getLongitude()) / 2);
            double vectorToCenterX = wallCenter.getLatitude() - point.getLatitude();
            double vectorToCenterY = point.getLongitude() - wallCenter.getLongitude();

            // Oblicz rzut wektora od punktu do środka ściany na wektor normalny
            double projection = Math.abs(vectorToCenterX * wallNormalX + vectorToCenterY * wallNormalY);

            // Jeśli rzut jest mniejszy niż najmniejszy znaleziony dotychczas, zaktualizuj wartość
            if (projection < closestDistance) {
                closestDistance = projection;
            }
        }

        return closestDistance;
    }


    /*public static float minWallDistance(Polygon polygon) {
        List<GeoPoint> points = polygon.getPoints();
        int size = points.size();
        float minDistance = Float.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            GeoPoint p1 = points.get(i);
            GeoPoint p2 = points.get((i + 1) % size);
            float[] normal = new float[2];
            normal[0] = (float) (p1.getLatitude() - p2.getLatitude());
            normal[1] = (float) (p2.getLongitude() - p1.getLongitude());
            float length = (float) Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1]);
            normal[0] /= length;
            normal[1] /= length;
            for (int j = i + 2; j < size + i - 1; j++) {
                GeoPoint p3 = points.get(j % size);
                GeoPoint p4 = points.get((j + 1) % size);
                float[] otherNormal = new float[2];
                otherNormal[0] = (float) (p3.getLatitude() - p4.getLatitude());
                otherNormal[1] = (float) (p4.getLongitude() - p3.getLongitude());
                float otherLength = (float) Math.sqrt(otherNormal[0] * otherNormal[0] + otherNormal[1] * otherNormal[1]);
                otherNormal[0] /= otherLength;
                otherNormal[1] /= otherLength;
                float distance = Math.abs(normal[0] * otherNormal[0] + normal[1] * otherNormal[1]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        return minDistance;
    }*/
}
