package asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

import util.AsyncMemoryFileCacheImage;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.call.R;

public class GridViewAsynctaskActivity extends Activity {
	private GridView mGridView;
	private String[] urlPath = new String[]{  //数据源
			"http://img3.imgtn.bdimg.com/it/u=3921468129,634158244&fm=23&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=1519979105,1747027397&fm=23&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=3084720760,288869075&fm=23&gp=0.jpg",
			"http://www.touxiang.cn/uploads/20131231/31-032406_364.jpg",
			"http://img1.imgtn.bdimg.com/it/u=2642462636,439040675&fm=23&gp=0.jpg",
			"http://img5.imgtn.bdimg.com/it/u=3938127501,1933671504&fm=23&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=1341923083,1900907467&fm=23&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=3741892468,250959383&fm=23&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=690802292,3622175025&fm=23&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=4135556117,449468592&fm=23&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=2750562473,158034435&fm=23&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=391045188,3409015930&fm=23&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=3026404928,664034360&fm=23&gp=0.jpg",
			"http://img4.imgtn.bdimg.com/it/u=1640876311,3279665449&fm=23&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=2528101725,1306780188&fm=23&gp=0.jpg",
			"http://img5.imgtn.bdimg.com/it/u=3997415071,3054130748&fm=23&gp=0.jpg",
			"http://img5.imgtn.bdimg.com/it/u=2923667321,513117738&fm=23&gp=0.jpg",
			"http://img3.imgtn.bdimg.com/it/u=4131486482,3650592575&fm=23&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=902623817,4158271644&fm=23&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=2746922094,2107416533&fm=23&gp=0.jpg",
			"http://img5.imgtn.bdimg.com/it/u=2455377461,174243012&fm=23&gp=0.jpg"
	};
	Executor executors;
	private AsyncMemoryFileCacheImage mAsyncMemoryFileCacheImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_asynctask_layout);
		mGridView = (GridView) findViewById(R.id.gridview_asynctask);
		AsynctaskGridViewAdapter adapter = new AsynctaskGridViewAdapter(this);
		adapter.setData(urlPath);
		mGridView.setAdapter(adapter);
		
		mAsyncMemoryFileCacheImage = AsyncMemoryFileCacheImage.getInstance(this);
		
		/*executors = new ThreadPoolExecutor(5, 150, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());//一次同时加载多张
		MoreAsynctaskGridViewAdapter adapter = new MoreAsynctaskGridViewAdapter(this);
		adapter.setData(urlPath);
		mGridView.setAdapter(adapter);*/
		
	}
	
	/*一次加载多张的适配器*/
	public class MoreAsynctaskGridViewAdapter extends BaseAdapter{
		private String[] strData;
		private LayoutInflater inflater;
		public MoreAsynctaskGridViewAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
		
		public void setData(String[] data){
			this.strData = data;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return urlPath.length;
		}

		@Override
		public Object getItem(int position) {
			return urlPath[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.gridviewasynctask_item_layout, null);
			}
			final ImageView img = (ImageView) convertView.findViewById(R.id.item_img);
			String HttpPath = (String) getItem(position);
			/*启动异步任务下载图片，下载一张就设置一张。但是在滑动时会复用convertView，所以可能出项重复图片*/
			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... params) {
					return getPicture(params[0]);
				}
				
				@Override
				protected void onPostExecute(Bitmap result) {
					super.onPostExecute(result);
					if(result != null){
						img.setImageBitmap(result);
					}
				}
			}.executeOnExecutor(executors, HttpPath);
			return convertView;
		}
	}
	
	/*一张一张加载的适配器*/
	public class AsynctaskGridViewAdapter extends BaseAdapter{
		private String[] strData;
		private Context mContext;
		private LayoutInflater inflater;
		public AsynctaskGridViewAdapter(Context context){
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}
		
		public void setData(String[] data){
			this.strData = data;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return strData.length;
		}

		@Override
		public Object getItem(int position) {
			return strData[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.gridviewasynctask_item_layout, null);
			}
			final ImageView img = (ImageView) convertView.findViewById(R.id.item_img);
			String HttpPath = (String) getItem(position);
			/*启动异步任务下载图片，下载一张就设置一张。但是在滑动时会复用convertView，所以可能出项重复图片*/
			/*new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... params) {
					return getPicture(params[0]);
				}
				@Override
				protected void onPostExecute(Bitmap result) {
					super.onPostExecute(result);
					if(result != null){
						img.setImageBitmap(result);
					}
				}
			}.execute(HttpPath);*/
			
			mAsyncMemoryFileCacheImage.loadBitmap(getResources(), HttpPath, img);
			return convertView;
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
}
