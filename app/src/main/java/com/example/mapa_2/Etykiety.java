package com.example.mapa_2;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;

public class Etykiety extends ItemizedOverlay<OverlayItem> {

    private final ArrayList<OverlayItem> mItemList = new ArrayList<OverlayItem>();

    public Etykiety(Drawable pDefaultMarker) {
        super(pDefaultMarker);
    }

    public void addOverlay(OverlayItem aOverlayItem)
    {
        Polygon polygon = new Polygon();
        mItemList.add(aOverlayItem);
        populate();
    }

    public void removeOverlay(OverlayItem aOverlayItem)
    {
        mItemList.remove(aOverlayItem);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public int size() {
        if (mItemList != null)
            return mItemList.size();
        else
            return 0;
    }

    @Override
    public boolean onSnapToItem(int x, int y, Point snapPoint, IMapView mapView) {
        return false;
    }
}
