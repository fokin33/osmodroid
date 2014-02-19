package com.OsMoDroid;

import java.util.ArrayList;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.OverlayItem.HotspotPlace;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;

public class MapFragment extends SherlockFragment implements DeviceChange{
	 private ResourceProxyImpl mResourceProxy;
	    private ItemizedOverlay<OverlayItem> mOverlay;
		ArrayList<OverlayItem> items;
		private MapView mMapView;
		private IMapController mController;
		
	@Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
         setRetainInstance(true);
         LocalService.devlistener=this;
         super.onCreate(savedInstanceState);
     }
	
	
	
	@Override
	public void onAttach(Activity activity) {
			super.onAttach(activity);
	}

	@Override
	public void onDestroyView() {
		Log.d(getClass().getSimpleName(), "map ondestroyview");
		LocalService.mapCenter=mMapView.getMapCenter();
		LocalService.mapZoom=mMapView.getZoomLevel();
		super.onDestroyView();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			Log.d(getClass().getSimpleName(), "map oncreateview");
			mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
	        mMapView = new MapView(inflater.getContext(), 256, mResourceProxy);
	        items = new ArrayList<OverlayItem>();
	        for (Device dev : LocalService.deviceList){
	        		addpoint(dev);
	        }
	        for (Channel ch: LocalService.channelList){
	        	for (Device dev: ch.deviceList){
	        		addpoint(dev);
	        	}
	        }
	        
            mOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                            @Override
                            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                    Toast.makeText(getSherlockActivity(),item.getSnippet(), Toast.LENGTH_LONG).show();
                                    return false; 
                            }

                            @Override
                            public boolean onItemLongPress(final int index, final OverlayItem item) {
                            		Toast.makeText(getSherlockActivity(),item.getSnippet(), Toast.LENGTH_LONG).show();
                                    return false;
                            }
                    }, mResourceProxy);
            mMapView.getOverlays().add(mOverlay);
            mMapView.setBuiltInZoomControls(true);
            mMapView.setMultiTouchControls(true);
            if(LocalService.mapCenter!=null){
            	mController = mMapView.getController();
            	mController.setZoom(LocalService.mapZoom);
            	mController.setCenter(LocalService.mapCenter);
            }
	        return mMapView;
	}



	



	@Override
	public void onDetach() {
		 Log.d(getClass().getSimpleName(), "map ondetach");
		super.onDetach();
	}



	private void addpoint(Device dev) {
		BitmapDrawable bd = getPointBitmapDrawable(dev);
		OverlayItem o=new OverlayItem(Integer.toString(dev.u), dev.name, dev.where, new GeoPoint(dev.lat, dev.lon));
		o.setMarker(bd);
		o.setMarkerHotspot(HotspotPlace.CENTER);		
		items.add(o);
	}



	private BitmapDrawable getPointBitmapDrawable(Device dev) {
		int w =400;
		int h =200;
		int radius=15;
		Bitmap b = Bitmap.createBitmap(w, h,  Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(b);
		Paint paint= new Paint();
		paint.setDither(true);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setTextSize(22f);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(Color.parseColor("#013220"));
		c.drawText(dev.name, w/2, h/2-radius, paint);
		c.drawText(dev.speed, w/2,h/2-2*radius, paint);
		paint.setColor(Color.parseColor("#" + dev.color));
		paint.setShadowLayer(radius, 0, 0, Color.GRAY);
		c.drawCircle(w/2, h/2, radius, paint);
		BitmapDrawable bd = new BitmapDrawable(this.getResources(),b);
		return bd;
	}



	@Override
	public void onDeviceChange(Device dev) {
		 Log.d(getClass().getSimpleName(), "ondevicechange");
		for (OverlayItem o: items){
			if (o.getUid().equals(Integer.toString(dev.u))){
				//BitmapDrawable bd = getPointBitmapDrawable(dev);
				Canvas c = new Canvas();
				Paint p = new Paint();
				p.setColor(Color.RED);
				c.drawCircle(50, 50, 12, p);
				((BitmapDrawable)o.getDrawable()).draw(c);
				//((BitmapDrawable)o.getDrawable()).getBitmap().recycle();
				//o.setMarker(bd);
				o.getPoint().setLatitudeE6( (int) (dev.lat * 1E6));
				o.getPoint().setLongitudeE6( (int) (dev.lon * 1E6));
			}
		}
		mMapView.invalidate();
		
	}
}
