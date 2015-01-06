package com.cbuu.finalproject;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

public class ShakeUtil implements SensorEventListener {

	private SensorManager sensorManager = null;
	private Context context = null;
	private Handler handler = null;

	static private float average = 0;
	static private float time = 0;

	private Handler mHandler = new Handler();
	
	private boolean running = false;
	
	public void setRunning(boolean running) {
		synchronized (this) {
			this.running = running;
		}
	}
	
	private static final int SAMPLE_DELAY = 1200;

	public ShakeUtil(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		this.sensorManager = (SensorManager) context
				.getSystemService(Service.SENSOR_SERVICE);
	}

	public void register() {
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
		//mHandler.postDelayed(stop, 6000);
		mHandler.postDelayed(sample, SAMPLE_DELAY);
		running = true;
	}

	public void unregister() {
		running = false;
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	private Runnable stop = new Runnable() {
		@Override
		public void run() {
			unregister();
		}
	};

	private Runnable sample = new Runnable() {

		@Override
		public void run() {
			if (running) {
				int result = (int) (average / time);

				if (result>17) {
					handler.obtainMessage(Constant.SHAKE).sendToTarget();
				}
				mHandler.postDelayed(sample, SAMPLE_DELAY);
			}
			average = 0;
			time = 0;
		}
	};

	@Override
	public void onSensorChanged(SensorEvent event) {

		int sensorType = event.sensor.getType();
		// values[0]:XÖá£¬values[1]£ºYÖá£¬values[2]£ºZÖá

		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			// MyUtils.log(Math.abs(values[0])+"--"+Math.abs(values[1])+"--"+Math.abs(values[2]));

			double sum = Math.sqrt(x * x + y * y + z * z);
			
			//sum -= 10;
			average += Math.abs(sum);
			time++;
		}
	}

}
