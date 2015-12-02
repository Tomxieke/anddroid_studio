package notification;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayerService extends Service {
	MediaPlayer mMediaPlayer ;
	@Override
	public void onCreate() {
		super.onCreate();
		mMediaPlayer = new MediaPlayer();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String path = intent.getStringExtra("MUSIC_PATH");
		
		if(intent.getIntExtra("MUSIC_STATE", 0)  == 1){
			pauseMusic();
		}else{
			pauseMusic();
			playMusic(path);
		}
			
		
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void pauseMusic(){
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
		}else{
			mMediaPlayer.start();
		}
	}
	
	/*播放音乐*/
	public void playMusic(String path){
		if(mMediaPlayer != null){
			try {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
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
}
