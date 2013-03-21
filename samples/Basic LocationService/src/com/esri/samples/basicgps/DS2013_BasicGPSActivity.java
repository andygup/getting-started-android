package com.esri.samples.basicgps;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationService;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;


public class DS2013_BasicGPSActivity extends Activity {
	
	MapView mMapView = null;
	ArcGISTiledMapServiceLayer tileLayer = null;
	GraphicsLayer graphicsLayer = null;
	LocationService locationService = null;
	static DecimalFormat decimalFormat = null;
	static final String DECIMAL_FORMAT = "0.000";
	static final double ZOOM_RESOLUTION = 4500.00;
	static final String MAP_ENDPOINT = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		decimalFormat = new DecimalFormat(DECIMAL_FORMAT);

		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		/* create a @ArcGISTiledMapServiceLayer */
		tileLayer = new ArcGISTiledMapServiceLayer(MAP_ENDPOINT);
		// Add tiled layer to MapView
		mMapView.addLayer(tileLayer);
		
		graphicsLayer = new GraphicsLayer();
		
		mMapView.addLayer(graphicsLayer);
		
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

		    public void onStatusChanged(Object source, STATUS status) {
			
			    if (source == mMapView && status == STATUS.INITIALIZED) {

				    setupLocationService();
			    }
		   }
		});

    }

    private void setupLocationService(){
    	if(locationService == null){
    		locationService = mMapView.getLocationService();
    		locationService.setAutoPan(false);
    		locationService.setAccuracyCircleOn(true);
    		locationService.setAllowNetworkLocation(true);
    		locationService.setLocationListener(new LocationListener() {
				
				public void onStatusChanged(String provider, int status, Bundle arg2) {
					Log.d("Test","LocationService status changed: " + status);
				}
				
				public void onProviderEnabled(String provider) {
					Log.d("Test","LocationService provider enabled: " + provider);
				}
				
				public void onProviderDisabled(String provider) {
					Log.d("Test","LocationService provider disabled: " + provider);
				}
				
				public void onLocationChanged(Location location) {
					if(location != null){
						if(location.hasAccuracy()){
							final Point latlon = new Point(location.getLongitude(), location.getLatitude());
							final Point point = (Point)GeometryEngine.project(latlon,SpatialReference.create(4326), mMapView.getSpatialReference());
							
							Map<String, Object> attributes = new HashMap<String, Object>();
							attributes.put("Lat", decimalFormat.format(location.getLatitude()));
							attributes.put("Lon", decimalFormat.format(location.getLongitude()));
							
							//Set market's color, size and style. You can customize these as you see fit
							final SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(Color.BLUE,25, SimpleMarkerSymbol.STYLE.DIAMOND);			
							final Graphic graphic = new Graphic(point, symbol,attributes,null);
							graphicsLayer.addGraphic(graphic);
							
							//Zoom and center map
							mMapView.zoomToScale(point, ZOOM_RESOLUTION);
						}
					}
					else{
						Log.d("Test","LocationService retrieved a null location value");
					}
				}
			});
    		
    		locationService.start();
    	}
    }
    
	@Override 
	protected void onDestroy() { 
		super.onDestroy();
		locationService.stop();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
		locationService.stop();
	}
	@Override 	protected void onResume() {
		super.onResume(); 
		mMapView.unpause();
	}

}