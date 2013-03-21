package com.esri.samples.touchevent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;


public class DS2013_BasicTouchListenerActivity extends Activity {
	
	MapView mMapView = null;
	ArcGISTiledMapServiceLayer tileLayer = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		/* create a @ArcGISTiledMapServiceLayer */
		tileLayer = new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		// Add tiled layer to MapView
		mMapView.addLayer(tileLayer);
		mMapView.setOnSingleTapListener(new OnSingleTapListener() {
			
			public void onSingleTap(float arg0, float arg1) {
				Toast.makeText(getApplicationContext(), "Map Touch Event", Toast.LENGTH_LONG).show();
			}
		});

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