package com.esri.samples.mapevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;

public class DS2013_BasicMapListenerActivity extends Activity {
	
	MapView mMapView = null;
	ArcGISTiledMapServiceLayer baseMap = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		/* create a @ArcGISTiledMapServiceLayer */
		baseMap = new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");

		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
			
			private static final long serialVersionUID = 1L;

			public void onStatusChanged(Object source, STATUS status) {
				
				String message = "Map OnStatusChangedListener = ";
				
				if(source == mMapView){
					switch(status){
						case INITIALIZED:
							message = message + status.toString();
							break;
						case INITIALIZATION_FAILED:
							message = message + status.toString();
							break;
						case LAYER_LOADED:
							message = message + status.toString();
							break;
						case LAYER_LOADING_FAILED:
							message = message + status.toString();
							break;						
					}
				}
				
				Log.d("Test", message);
			}
		});
		
		baseMap.setOnStatusChangedListener( new OnStatusChangedListener() {
			
			private static final long serialVersionUID = 1L;

			public void onStatusChanged(Object source, STATUS status) {
				
				String message = "BaseMap Layer OnStatusChangedListener = ";
				
				if(source == baseMap){
					switch(status){
						case INITIALIZED:
							message = message + status.toString();
							break;
						case INITIALIZATION_FAILED:
							message = message + status.toString();
							break;					
					}
				}
				
				Log.d("Test", message);
			}
		});
		
		// Add tiled layer to MapView
		mMapView.addLayer(baseMap);

    }

	@Override 
	protected void onDestroy() { 
		super.onDestroy();
 }
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
 }
	@Override 	protected void onResume() {
		super.onResume(); 
		mMapView.unpause();
	}

}