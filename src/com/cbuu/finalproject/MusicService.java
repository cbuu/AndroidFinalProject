package com.cbuu.finalproject;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.MediaController;

public class MusicService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Context context =null;
	private MediaPlayer player = null;
	
	public MusicService(Context context) {
		this.context = context;
		player = MediaPlayer.create(context, R.raw.abc);
		player.setLooping(true);
	}
	
	public void PlayOrPause(){
		if (player.isPlaying()) {
			player.pause();
		}else {
			player.start();
		}
	}
	
	public void Stop(){
		if (player != null) {
			player.stop();
			player.reset();
		}
	}
	
	

}
