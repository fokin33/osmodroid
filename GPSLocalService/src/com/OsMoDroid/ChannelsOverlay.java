package com.OsMoDroid;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class ChannelsOverlay extends Overlay {
	//private final float mScale;
	Paint paint=new Paint();
	
	public ChannelsOverlay(ResourceProxy pResourceProxy) {
		super(pResourceProxy);
		// mScale = OsMoDroid.context.getResources().getDisplayMetrics().density;
		

		// TODO Auto-generated constructor stub
	}
	
	
	

	@Override
	protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
		final BoundingBoxE6 theBoundingBox = mapView.getBoundingBox();
		final Projection pj = mapView.getProjection();
        final Point curScreenCoords = new Point();
		for (Channel ch :LocalService.channelList){
			for(Device dev:ch.deviceList){
				 if (theBoundingBox.contains(new GeoPoint(dev.lat, dev.lon))) 
				 {
					pj.toMapPixels(new GeoPoint(dev.lat, dev.lon), curScreenCoords);
				  	paint.setDither(true);
					paint.setAntiAlias(true);
					paint.setTextSize(22f);
					paint.setTypeface(Typeface.DEFAULT_BOLD);
					paint.setTextAlign(Paint.Align.CENTER);
					paint.setColor(Color.parseColor("#013220"));
					canvas.save();
			        canvas.rotate(-mapView.getMapOrientation(), curScreenCoords.x, curScreenCoords.y);
					canvas.drawText(dev.name, curScreenCoords.x, curScreenCoords.y-10, paint);
					canvas.drawText(dev.speed, curScreenCoords.x,curScreenCoords.y-2*10, paint);
					paint.setColor(Color.parseColor("#" + dev.color));
					canvas.drawCircle(curScreenCoords.x, curScreenCoords.y, 10, paint);
					canvas.restore();
				 }
			}
			for(com.OsMoDroid.Channel.Point p: ch.pointList){
				if (theBoundingBox.contains(new GeoPoint(p.lat, p.lon))) 
				 {
					pj.toMapPixels(new GeoPoint(p.lat, p.lon), curScreenCoords);
				  	paint.setDither(true);
					paint.setAntiAlias(true);
					paint.setTextSize(22f);
					paint.setTypeface(Typeface.DEFAULT_BOLD);
					paint.setTextAlign(Paint.Align.CENTER);
					paint.setColor(Color.parseColor("#013220"));
					canvas.save();
			        canvas.rotate(-mapView.getMapOrientation(), curScreenCoords.x, curScreenCoords.y);
					canvas.drawText(p.name, curScreenCoords.x, curScreenCoords.y-10, paint);
					paint.setColor(Color.parseColor("#" + p.color));
					canvas.drawRect(curScreenCoords.x-10, curScreenCoords.y-10, curScreenCoords.x+10, curScreenCoords.y+10, paint);
					canvas.restore();
				 }
			}
		}
		
	}

	
}
