package service;

import java.io.File;
import java.io.IOException;

import mylog.Mylog;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

public class MySercive extends Service {
	private MediaPlayer mMediaplaryer;
	File file;
	@Override
	public void onCreate() {   //只执行一次，当这个service已经创建好之后，再次调用startServic()方法时不会再执行这个方法，只会执行下面的onStartVommand()方法
		super.onCreate();
		file = Environment.getExternalStorageDirectory();
		mMediaplaryer = new MediaPlayer();
		Mylog.i("----service-------------onCreate----前----------------" + file.getAbsolutePath()+"/cafe.mp3");
		playMusic(file.getAbsolutePath()+"/cafe.mp3");
		Mylog.i("----service-------------onCreate-----后---------------" + file.getAbsolutePath()+"/cafe.mp3");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mylog.i("----service-------------onStartCommand--------------------");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//耗时操作
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Mylog.i("----service-------------onBind--------------------");
		return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		
		Mylog.i("----service-------------onUnbind--------------------");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		Mylog.i("----service-------------onDestroy--------------------");
		super.onDestroy();
		mMediaplaryer.stop();
	}
	
	
	public void playMusic(String filepath) {
		try {
		//	if (mMediaplaryer.isPlaying()) {
				mMediaplaryer.reset();
		//	}
			mMediaplaryer.setDataSource(filepath);
			mMediaplaryer.prepare();
			mMediaplaryer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
