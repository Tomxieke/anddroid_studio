package asynctask;
/**
 * 异步任务。线程和Handler的封装
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import mylog.Mylog;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.call.R;

public class AsynctaskActivity extends Activity implements OnClickListener{
	private TextView mProgressTxt;
	private SeekBar mSeekBar;
	private Button mThreadStartBtn,mAsyncTaskStartBtn,mDownloadPrictureBtn;
	private ImageView mImg;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 1:
				mProgressTxt.setText(""+msg.obj);
				break;
			case 2:
				mProgressTxt.setText(""+msg.obj);
			}
		};};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asynctask_main_layout);
		initView();
	}
	
	public void initView(){
		mProgressTxt = (TextView) findViewById(R.id.show_progress_txt);
		mSeekBar = (SeekBar) findViewById(R.id.asynctask_seekbar);
		mThreadStartBtn = (Button) findViewById(R.id.thread_start_btn);
		mImg = (ImageView) findViewById(R.id.downloadpicture_img);
		mThreadStartBtn.setOnClickListener(this);
		mAsyncTaskStartBtn = (Button) findViewById(R.id.asybctask_start_btn);
		mAsyncTaskStartBtn.setOnClickListener(this);
		mDownloadPrictureBtn = (Button) findViewById(R.id.asybctask_downloadPicture_btn);
		mDownloadPrictureBtn.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.thread_start_btn:
			startByThread();
			break;
		case R.id.asybctask_start_btn:
			MyAsynctask myAsynctask = new MyAsynctask();
			myAsynctask.execute(); //开始加载 
			break;
		case R.id.asybctask_downloadPicture_btn:
			String urlPath = "http://img4.imgtn.bdimg.com/it/u=52754568,3369504778&fm=23&gp=0.jpg";
			new AsyncTask<String, Void, Bitmap>(){
				@Override
				protected Bitmap doInBackground(String... params) {
					return getPicture(params[0]);  //下载图片
				}
				@Override
				protected void onPostExecute(Bitmap result) {
					super.onPostExecute(result);
					mImg.setImageBitmap(result);
				}
				
			}.execute(urlPath);
			break;
		}
	}
	
	
	/*连接网络取数据*/
	public Bitmap getPicture(String URLpath){
		try {
			URL url = new URL(URLpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream in = conn.getInputStream();  //将对应地址的图片读入流
			Bitmap bitmap = BitmapFactory.decodeStream(in);   //将流转化为Bitmap对象。
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*handler更新控件*/
	public void startByThread(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 1;i <= 100;i++){
					mSeekBar.setProgress(i);
					Message msg = Message.obtain();
					msg.obj = i;
					msg.what = 1;
					mHandler.sendMessage(msg); //发送一个消息就应该Message.obtain()一个消息。也就是这两个方法配套出现。
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Message msg = Message.obtain();
				msg.what = 2;
				msg.obj = "加载完成";
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	/*Asybctack启动加载方式
	 * AsyncTask<Void, Integer, String>
	 * 第一个参数  params为调用execute()传进来的参数类型  Void表示无参数
	 * 第二个参数Progress为在异步操作执行中传递的参数类型。在doInBackground()中调用publishProgress()就可以将参数传递给onProgressUpdate()方法，从而更新UI控件
	 * 第三个参数 result是异步操作执行完成返回的参数类型
	 * */
	public class MyAsynctask extends AsyncTask<Void, Integer, String>{
		@Override
		/*最先执行的回调函数*/
		protected void onPreExecute() {
			super.onPreExecute();
			Mylog.d("--------onPreExecute-------");
		}
		
		@Override
		/*执行异步任务的回调方法  子线程中执行*/
		protected String doInBackground(Void... params) {  //Void... params  动态数组   子线程中执行
			Mylog.d("--------doInBackground-------");
			for(int i = 1;i <= 100; i++){
				publishProgress(i);//异步操作中的参数传递方法。调用一次就会执行一次onProgressUpdate()回调函数
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String result = "下载完成";
			return result;
		}
		
		@Override
		/*执行异步任务中时要调用的回调函数  UI线程中执行
		 * 当 doInBackground方法中调用了publishProgress()方法时才会执行onProgressUpdate()方法*/
		protected void onProgressUpdate(Integer... values) {   //UI线程中执行，更新空间操作
			super.onProgressUpdate(values);
			Mylog.d("--------onProgressUpdate-------");
			int progress = values[0];   //参数是动态数组，所以要先接收
			mSeekBar.setProgress(progress);
			mProgressTxt.setText(""+progress);
		}
		
		@Override
		/*异步操作结束时调用的回调函数   UI线程中执行
		 * */
		protected void onPostExecute(String result) {   // 最后的结果
			super.onPostExecute(result);
			Mylog.d("--------onPostExecute-------");
			mProgressTxt.setText(result);
		}
	}
}
