package com.example.mapa_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

public class MarkerText extends Overlay {

    private String text;
    private IGeoPoint location;
    private Paint paint;
    private Rect bounds;
    private float iconHeight;


    public MarkerText(Context context, String text, IGeoPoint location, float iconHeight) {
        super(context);
        this.text = text;
        this.location = location;
        this.paint = new Paint();
        this.bounds = new Rect();
        this.iconHeight = iconHeight;

        // Set text color, size and font
        //paint.setColor(0xFF000000); // Black color
        paint.setTextSize(39);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if(mapView.getZoomLevelDouble() > 21.5){
            // Get the screen coordinates of the location
            Point screenCoords = new Point();
            mapView.getProjection().toPixels(location,screenCoords);

            // Measure the bounds of the text
            //paint.setTextSize((float)1.8 * (float)(mapView.getZoomLevelDouble()));

            paint.getTextBounds(text, 0, text.length(), bounds);



            // Draw the text at the location
            canvas.drawText(text, screenCoords.x - bounds.width() / 2, (float)(screenCoords.y + bounds.height() / 2  + iconHeight), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        // Do nothing on touch events
        return false;
    }
}