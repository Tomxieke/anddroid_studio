package music.mediaplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import mylog.Mylog;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.call.R;

public class Mp3PlayerActivity extends Activity implements OnClickListener,OnItemClickListener,OnSeekBarChangeListener{
	private int index; // 表示当前是哪一首曲目正在播放
	private boolean flag = true;   //控制seekbar的无限循环 
	private ImageView mNextMusicImg,mPerMusicImg;
	private Button mSearchBtn,mPlayBtn,mPuseBtn;
	private Mp3Adapter mAdapter;
	private ListView mListView;
	private MediaPlayer mMediaplaryer = new MediaPlayer();
	private SeekBar mSeekBar;
	ArrayList<File> mp3List = new ArrayList<File>();  //所有mp3文件的集合 
	private TextView mBeginTimeTxt,mEndTimeTxt;
	private Handler mHandler = new Handler();  //加载歌曲应该放在后台线程中操作。采用Handler消息机制
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mp3_player_layout);
		initView();
	}
	
	
	/*加载所有控件*/
	private void initView(){
		mSearchBtn = (Button) findViewById(R.id.mp3_search_btn);
		mSearchBtn.setOnClickListener(this);
		mPlayBtn = (Button) findViewById(R.id.mp3_play_btn);
		mPlayBtn.setOnClickListener(this);
		mPuseBtn = (Button) findViewById(R.id.mp3_pause_btn);
		mPuseBtn.setOnClickListener(this);
		
		mBeginTimeTxt = (TextView) findViewById(R.id.mp3_begin_times);
		mEndTimeTxt = (TextView) findViewById(R.id.mp3_end_times);
		
		mListView = (ListView) findViewById(R.id.mp3_listview);
		mListView.setOnItemClickListener(this);
		
		mSeekBar = (SeekBar) findViewById(R.id.mp3_seekbar);
		mSeekBar.setOnSeekBarChangeListener(this); 
		new Thread(new Runnable() { // 进度条操作可以在子线程中进行 ,不用Handler来操作
					@Override
					public void run() {
						while (flag) {
							if (mMediaplaryer.isPlaying()) {
								final long allTimes = mMediaplaryer.getDuration(); // 曲目总时长
								final long nowTimes = mMediaplaryer.getCurrentPosition(); //当前播放时长
							//	Mylog.e("" + allTimes);
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										mEndTimeTxt.setText(setTimes(allTimes)); // 毫秒转换为时间并设置
										mBeginTimeTxt.setText(setTimes(nowTimes)); //设置当前时长
									}
								});
								mSeekBar.setMax((int)allTimes); // 将Mediaplaryer播放时长设置给seekbar
								mSeekBar.setProgress((int)nowTimes); // 将Mediaplaryer的当前时间设置给seekbar
							}
						}
					}
				}).start();
		
		mNextMusicImg = (ImageView) findViewById(R.id.mp3_nextMusic_img);
		mNextMusicImg.setOnClickListener(this);//下一曲监听
		mPerMusicImg = (ImageView) findViewById(R.id.mp3_beforMusic_img);
		mPerMusicImg.setOnClickListener(this);//上一曲监听
	}
	/*----------------获取时间并转化为格式--------------------*/
	public String setTimes(long times){
		long mTimes = times/1000;
		int minute = 0,second = 0 ;
		//if(mTimes > 60){
			minute = (int) (mTimes/60);
			second = (int) (mTimes%60);
			return getTime(minute) + ":" + getTime(second);
		/*}else{
			second = (int) (mTimes%60);
			return getTime(minute) + ":" + 
		}*/
	}
	
	public String getTime(int date){
		if(date < 10){
			return "0" + date;
		}else{
			return "" + date;
		}
	}
	/*-----------------------------------------------------------------*/
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.mp3_search_btn:  //加载按钮
			mAdapter = new Mp3Adapter(this);
			mListView.setAdapter(mAdapter);
			if(mp3List != null){   // 多次点击加载按钮不会重复去加载已有曲目
				mp3List.removeAll(mp3List);
			}
			new Thread(new Runnable() {   // 用线程来加载数据，当数据很多时才不会出现卡顿
				@Override
				public void run() {
					searchMp3File(Environment.getExternalStorageDirectory()); //设置数据源
					mHandler.post(new Runnable() {   // listView的刷新必须在ui主线程中执行，所有用到Handler
						@Override
						public void run() {
							mAdapter.refushData(mp3List);  //将目录下的所有MP3文件扫描后设置给listview
						}
					});
				}
			}).start();
			break;
			
		case R.id.mp3_play_btn:  // 播放按钮
			mMediaplaryer.start();
			break;
			
		case R.id.mp3_pause_btn:  //暂停按钮
			mMediaplaryer.pause();
			break;
		case R.id.mp3_nextMusic_img:  // 下一曲
			nextMusic();
			break;
		case R.id.mp3_beforMusic_img: //上一曲
			lastMusic();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		index = arg2;
		File file = (File) arg0.getAdapter().getItem(arg2);
		playMusic(file.getAbsolutePath());
	}
	
	
	
	/*播放音乐对应路径的音乐*/
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
		//当一首音乐播放完成时，直接下一曲
		mMediaplaryer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
			//	Mylog.d("==========前=====onCompletion======="+index);
				nextMusic();
			//	Mylog.d("==========后====onCompletion======="+index);
			}
		});
	}
	
	/*下一曲按钮*/
	public void nextMusic(){
	//	Mylog.d("==========前============"+index);
		if(++index >= mp3List.size()){
			index = 0;
		}
		File file = mp3List.get(index);   //只有从这里面取数据了。
		playMusic(file.getAbsolutePath());
	//	Mylog.d("==========后============"+index);
	}
	
	/*上一曲*/
	public void lastMusic(){
	//	Mylog.d("==========前============"+index);
		if(--index < 0){
			index = mp3List.size()-1;
		}
		File file = mp3List.get(index);   //只有从这里面取数据了。
		playMusic(file.getAbsolutePath());	
	//	Mylog.d("==========后============"+index);
	}
	
	/* 取得所有的mp3文件 */
	public void searchMp3File(File srcFile) {
		File[] arrFiles = srcFile.listFiles();
		if (arrFiles != null && arrFiles.length > 0) { // 空判断
			for (int i = 0; i < arrFiles.length; i++) { // 不为空就遍历
				File mp3File = arrFiles[i];
				if (mp3File.isDirectory()) { // 是目录 则递归
					searchMp3File(mp3File);
				} else {
					if (mp3File.getName().endsWith(".mp3")) { // 文件以mp3结尾，则放入数据源
						mp3List.add(mp3File);
						Mylog.e("----" + mp3List.size());
					}

				} 
			}
		}
	}

	/*listView 歌曲列表适配器 */
	private class Mp3Adapter extends BaseAdapter{
		ArrayList<File> fileData = new ArrayList<File>();
		private Context mContext;
		private LayoutInflater mInflater;
		public Mp3Adapter(Context context){
			this.mContext = context;
			mInflater = LayoutInflater.from(context);
		}
		
		public void refushData(ArrayList<File> list){
			this.fileData = list;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return fileData.size();
		}

		@Override
		public Object getItem(int position) {
			return fileData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
			}
			File mp3File = (File) getItem(position);
			TextView mp3Name = (TextView) convertView;
			mp3Name.setText(mp3File.getName());
			return convertView;
		}
	}
//------------------------用户拖动seekBar时改变音乐进度------------------------
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser == true){    //如果是用户拖动seekbar，则直接设置曲目进度
			mMediaplaryer.seekTo(progress);  
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	@Override // 当退出播放时。
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			flag = false; //结束seekbar的循环
			mMediaplaryer.stop();
		//	mMediaplaryer.release();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
