/*
package adapter_view_refresh;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call.R;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderLayout.PresetIndicators;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.scxh.slider.library.SliderTypes.TextSliderView;
import com.warmtel.android.xlistview.XListView;
import com.warmtel.android.xlistview.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import bean.JSONMessage;
import util.AsyncMemoryCacheImag;
import util.HttpClientConnectUtil;
import util.HttpClientConnectUtil.ConnectCallBack;

public class HttpXlistViewActivty extends Activity implements IXListViewListener {
	private XListView mListView;
	Executor executors;
	ListAdapter adapter;
	AsyncMemoryCacheImag asyncCache;
	
	private int pageNo = 1;//当前请求页
	private int totalPage = 4; //数据总页数
	
	public final static String[] imageThumbUrls = new String[] {
		"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
		"http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
		"http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
		"http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
	};
	
	public final static String[] stringThumbUrls = new String[] {
		" 桑兰老公回应桑兰受伤造假传闻：这是在泼脏水",
		" 土耳其称击落战机时不知其属于俄军 普京：胡扯",
		" 跨越物种的友情！山羊与东北虎同吃同住",
		"俄国防部:已歼灭飞行员",
	};
	
	LayoutInflater mInflater ;
	//private SliderLayout mSliderLayout;   //listView的头部控件
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.http_xlistview_layout);
	///	Log.v("test", getJsonString());
		mInflater = LayoutInflater.from(this);
		executors = new ThreadPoolExecutor(5, 150, 10, TimeUnit.SECONDS,
				new LinkedBlockingDeque<Runnable>());// 一次同时加载多张
		mListView = (XListView) findViewById(R.id.json_listview);
		mListView.setXListViewListener(this);
		adapter = new ListAdapter(this);

		// ArrayList<JSONMessage> data = makeMessageList(getJsonString());

		// adapter.refishData(data);

		mListView.setAdapter(adapter);
		asyncCache = new AsyncMemoryCacheImag(this);
		initLruCache(); // 图片缓存初始化

		mListView.addHeaderView(getSliderLayoutView());
		
		getHttpData(pageNo);   //联网去数据
	}
	
	*/
/**
	 * 返回listView的头
	 * @return
	 *//*

	public View getSliderLayoutView(){
		View v = mInflater.inflate(R.layout.list_slider_layout, null);
		SliderLayout mSliderLayout = (SliderLayout) v.findViewById(R.id.image_slider_layout);
		int length = imageThumbUrls.length;
		for(int i = 0; i < length; i++){
			TextSliderView sliderView = new TextSliderView(this);   //向SliderLayout中添加控件
			sliderView.image(imageThumbUrls[i]);
			sliderView.description(stringThumbUrls[i]);
			sliderView.setOnSliderClickListener(new OnSliderClickListener() {
				@Override
				public void onSliderClick(BaseSliderView slider) {
					Toast.makeText(HttpXlistViewActivty.this, "中国加剧芯片产业并购潮 砸钱模式难以理解", Toast.LENGTH_SHORT).show();
				}
			});
		
			mSliderLayout.addSlider(sliderView);
		}
		mSliderLayout.setPresetIndicator(PresetIndicators.Right_Bottom);  //将小圆点设置到右下方
		return v;
	}
	
	*/
/**
	 * 连接网络获取数据
	 *//*

	public void getHttpData(final int pageNo){
		// 不带参数的请求
				String callbackurl = "http://192.168.1.112:8080/app/list";
				new HttpClientConnectUtil().HttpConnectMethod(callbackurl,
						HttpClientConnectUtil.DO_GET, new ConnectCallBack() {
							@Override
							public void getConnectMsg(String msg) {
								Log.d("test", "+++++++++++" + msg);
								ArrayList<JSONMessage> data = makeMessageList(msg);
								if(pageNo == 1){
									adapter.refishData(data);
								}else{
									adapter.addData(data);
								}
								
								mListView.setPullLoadEnable(true);  //设置上滑加载更多显示
								mListView.stopRefresh();   //下拉刷新取到数据后停止刷新
								mListView.setRefreshTime("刚刚");
								
								mListView.stopLoadMore();   //加载完成之后停止加载更多
							}
						});
	}

	// 从文件中将JSON解析成字符串
	public String getJsonString() {
		AssetManager manager = getAssets();
		try {
			InputStream in = manager.open("around");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String str;
			StringBuilder sb = new StringBuilder();
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			Log.v("test", sb.toString());
			in.close();
			reader.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将JSON字符串解析成msg对象并保存到list中
	public ArrayList<JSONMessage> makeMessageList(String jsonStr) {
		ArrayList<JSONMessage> list = new ArrayList<JSONMessage>();
		Log.v("test", "==================================");
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject JO = jsonObject.getJSONObject("info");
			JSONArray jsonArray = JO.getJSONArray("merchantKey");
		//	Log.v("test", "=======jsonObject==========================="
		//			+ jsonObject.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				String name = object.getString("name");
				String coupon = object.getString("coupon");
				String location = object.getString("location");
				String distance = object.getString("distance");
				String picUrl = object.getString("picUrl");
				String couponType = object.getString("couponType");
				String cardType = object.getString("cardType");
				String groupType = object.getString("groupType");
				JSONMessage msg = new JSONMessage();
				msg.setName(name);
				msg.setCoupon(coupon);
				msg.setLocation(location);
				msg.setDistance(distance);
				msg.setPicUrl(picUrl);
				msg.setCouponType(couponType);
				msg.setCardType(cardType);
				msg.setGroupType(groupType);
				list.add(msg);
			//	Log.v("test", "=============list==================" + list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public class ListAdapter extends BaseAdapter {
		ArrayList<JSONMessage> listData = new ArrayList<JSONMessage>();
		LayoutInflater inflater;

		public ListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
		
		public void refishData(ArrayList<JSONMessage> hlistData) {
			listData = hlistData;
			notifyDataSetChanged();
		}
		
		public void addData(ArrayList<JSONMessage> hlistData){  //添加数据源
			this.listData.addAll(hlistData);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Handler handler;
			View v = null;

			if (convertView == null) {
				handler = new Handler();
				v = inflater.inflate(R.layout.httplistview_item_layout, null);
				handler.couponTypeImg = (ImageView) v
						.findViewById(R.id.img_three);
				handler.cardTypeImg = (ImageView) v.findViewById(R.id.img_one);
				handler.groupTypeImg = (ImageView) v.findViewById(R.id.img_two);

				handler.picUrlImg = (ImageView) v.findViewById(R.id.icon_img);
				handler.nameTxt = (TextView) v.findViewById(R.id.name_txt);
				handler.couponTxt = (TextView) v.findViewById(R.id.coupon_txt);
				handler.locationTxt = (TextView) v
						.findViewById(R.id.location_text);
				handler.distanceTxt = (TextView) v
						.findViewById(R.id.distance_txt);
				v.setTag(handler);
			} else {
				v = convertView;
			}
			handler = (Handler) v.getTag();
			JSONMessage msg = listData.get(position);
			handler.nameTxt.setText(msg.getName());
			handler.couponTxt.setText(msg.getCoupon());
			handler.locationTxt.setText(msg.getLocation());
			handler.distanceTxt.setText(msg.getDistance());

			*/
/*
			 * new AsyncTask<String, Void, Bitmap>() {
			 * 
			 * @Override protected Bitmap doInBackground(String... params) {
			 * return getPicture(params[0]); }
			 * 
			 * @Override protected void onPostExecute(Bitmap result) {
			 * super.onPostExecute(result); if(result != null){
			 * handler.picUrlImg.setImageBitmap(result); } }
			 * }.executeOnExecutor(executors,msg.getPicUrl());
			 *//*


		//	loadBitmap(getResources(), msg.getPicUrl(), handler.picUrlImg,
		//			R.drawable.img_smile); // 防止图片加载错乱

			
			
			asyncCache.loadBitmap(getResources(), msg.getPicUrl(), handler.picUrlImg, R.drawable.img_smile);
			
			if (msg.getCardType().equalsIgnoreCase("yes")) {
				handler.cardTypeImg.setVisibility(View.VISIBLE);
			} else {
				handler.cardTypeImg.setVisibility(View.GONE);
			}

			if (msg.getCouponType().equalsIgnoreCase("yes")) {
				handler.couponTypeImg.setVisibility(View.VISIBLE);
			} else {
				handler.couponTypeImg.setVisibility(View.GONE);
			}

			if (msg.getGroupType().equalsIgnoreCase("yes")) {
				handler.groupTypeImg.setVisibility(View.VISIBLE);
			} else {
				handler.groupTypeImg.setVisibility(View.GONE);
			}

			return v;
		}

		class Handler {
			ImageView picUrlImg, couponTypeImg, cardTypeImg, groupTypeImg;
			TextView nameTxt, couponTxt, locationTxt, distanceTxt;
		}
	}

	*/
/**
	 * 通常类似 ListView 与 GridView 等视图组件在使用上面演示的AsyncTask 方法时会同时带来另外一个问题。
	 * 为了更有效的处理内存，那些视图的子组件会在用户滑动屏幕时被循环使用。如果每一个子视图都触发一个AsyncTask ，
	 * 那么就无法确保当前视图在结束task时，分配的视图已经进入循环队列中给另外一个子视图进行重用。 而且, 无法确保所有的
	 * 异步任务能够按顺序执行完毕。
	 * 
	 * @param imageUrl
	 * @param imageView
	 * @param resId
	 *默认图片资源 二级缓存：文件缓存
	 *//*


	// HashMap<String,Bitmap> mapBitmap = new HashMap<String,Bitmap>();
	// //第一种缓存方式，只用HashMap装缓存图片 ，缺点：不知道能装多少？占内存
	// 第二种方式：在第一种方式外套一个弱应用。当内存不足时自动释放内存
	// WeakReference<HashMap<String,Bitmap>> weakHashBitmap = new
	// WeakReference<HashMap<String,Bitmap>>(mapBitmap);
	// 第三种方式，采用LruCache类保存图片，在释放图片时会用“最近最少”原则。
	private LruCache<String, Bitmap> mMemoryCache;

	public void initLruCache() {
		// 获取设备的最大内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 将最大内存的八分之一用来缓存图片
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) { // 返回图片大小，以便内存不足时释放对应图片内存
				return value.getByteCount();
			}
		};
	}

	// 添加图片到内存储存
	public void addBitMapToLruCache(String url, Bitmap bitmap) {
		if (getCacheBitmap(url) == null) { // 从缓存中取出的Bitmap为空时，说明mMemoryCache缓存中没有这张图片
			mMemoryCache.put(url, bitmap); // 将图片添加到缓存
		}
	}

	// 通过url从mMemoryCache缓存中获取bitmap
	public Bitmap getCacheBitmap(String url) {
		Bitmap bitmap = mMemoryCache.get(url);
		return bitmap;
	}

	// ---------------------------------------------------------------------------
	public void loadBitmap(Resources res, String imageUrl, ImageView imageView,
			int resId) {
		*/
/*
		 * mapBitmap = weakHashBitmap.get(); //第二种方式也在这里来判断
		 * if(mapBitmap.containsKey(imageUrl)){ Bitmap bitmap =
		 * mapBitmap.get(imageUrl); imageView.setImageBitmap(bitmap);
		 * Log.v("test", "从内存中去图片"); return; }
		 *//*

		Bitmap bitmap = getCacheBitmap(imageUrl);// 从缓存中获取Bitmap
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			Log.v("test", "从内存中去图片");
			return;
		}
		
		
		
		if (cancelPotentialWork(imageUrl, imageView)) {
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			AsyncDrawable asyncDrawable = new AsyncDrawable(res,
					BitmapFactory.decodeResource(res, resId), task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(imageUrl);
		}
	}

	public static boolean cancelPotentialWork(String imageUrl,
			ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.data;
			if (!bitmapData.equals(imageUrl)) {
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	*/
/**
	 * 创建一个专用的Drawable的子类来储存返回工作任务的引用。在这种情况下，当任务完成时BitmapDrawable会被使用
	 * 
	 *//*

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
		}
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;// 弱引用
																	// ，特点当内存不够时，自动回收
		private String data = "";

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];
			return getPicture(data);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = (ImageView) imageViewReference
						.get();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
					// mapBitmap = weakHashBitmap.get(); //第二种方式套弱引用用这个得到。
					// mapBitmap.put(data, bitmap); //将数据保存到hashmap 第一种方式只需要这个
					Log.v("test", "从网络中去图片");
					addBitMapToLruCache(data, bitmap);// 第三种方式向缓存中添加图片
				}
			}
		}
	}
	
	
	
	
	
	
	*/
/* 连接网络取数据 *//*

	public Bitmap getPicture(String URLpath) {
		try {
			URL url = new URL(URLpath);
			Log.d("test", URLpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream in = conn.getInputStream(); // 将对应地址的图片读入流
			Bitmap bitmap = BitmapFactory.decodeStream(in); // 将流转化为Bitmap对象。
			in.close();
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	*/
/**
	 * 下拉刷新
	 *//*

	public void onRefresh() {
		Log.e("test", "onRefresh-----下拉刷新");
		pageNo = 1;
		getHttpData(pageNo);
		mListView.setPullLoadEnable(true);   //让加载更多显示
	}

	@Override
	*/
/**
	 * 上拉加载更多
	 *//*

	public void onLoadMore() {
		Log.e("test", "onLoadMore-----上拉加载跟多");
		if(++pageNo > totalPage){
			Log.d("test", "onLoadMore-------else" + pageNo);
			Toast.makeText(HttpXlistViewActivty.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
			mListView.stopLoadMore();
			mListView.setPullLoadEnable(false);   //加载到最后一页，让加载更多隐藏
		}else{
			getHttpData(pageNo);
		}
	}
}
*/
