package util;
/**
 * 解决网络取图片放到适配器控件时图片错乱和缓存问题
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncMemoryCacheImag {
	private Context mContext;
	private LruCache<String, Bitmap> mMemoryCache;
	
	public AsyncMemoryCacheImag(Context context){
		mContext = context;
		initLruCache();
	}

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
	
	
	public void loadBitmap(Resources res, String imageUrl, ImageView imageView,
			int resId) {
		/*
		 * mapBitmap = weakHashBitmap.get(); //第二种方式也在这里来判断
		 * if(mapBitmap.containsKey(imageUrl)){ Bitmap bitmap =
		 * mapBitmap.get(imageUrl); imageView.setImageBitmap(bitmap);
		 * Log.v("test", "从内存中去图片"); return; }
		 */
		/*Bitmap bitmap = getCacheBitmap(imageUrl);// 从缓存中获取Bitmap
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			Log.v("test", "从内存中去图片");
			return;
		}*/
		
		Bitmap filemap = readImgFromFile(imageUrl);
		if (filemap != null) {
			imageView.setImageBitmap(filemap);
			Log.v("test", "从文件中去图片");
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

	/**
	 * 创建一个专用的Drawable的子类来储存返回工作任务的引用。在这种情况下，当任务完成时BitmapDrawable会被使用
	 * 
	 */
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
				//	addBitMapToLruCache(data, bitmap);// 第三种方式向缓存中添加图片
					writeFileToExternalPrivate(data,bitmap);  //将图片添加到文件
				}
			}
		}
	}
	
	/*将图片保存到文件用图片 地址url的一部分来作为文件名*/
	public String dealImgUrl(String imgUrl){
		int index = imgUrl.lastIndexOf("/");
		String str = imgUrl.substring(index+1);
		return str;
	}
	
	/*从文件中获取图片*/
	public Bitmap readImgFromFile(String imgUrl){
		String name = dealImgUrl(imgUrl);
		String cacheDir = mContext.getExternalCacheDir().getAbsolutePath();   //获取外部储存私有部分路径
		
		Bitmap bitmap = BitmapFactory.decodeFile(cacheDir + File.separator + "map" + File.separator + name);  //参数为文件路径
		return bitmap;
	}
	
	/*
	 * 访问外部储存私有区域  将图片缓存到外部储存私有区域
	 * 路径为   getExternalFilesDir（）       /mnt/sdcard/Android/data/com.example.call/files
	 * 		getExternalCacheDir()    /mnt/sdcard/Android/data/com.example.call/cache
	 */
	public void writeFileToExternalPrivate(String imgUrl,Bitmap bitmap){
		String name = dealImgUrl(imgUrl);
		String cacheDir = mContext.getExternalCacheDir().getAbsolutePath();   //获取外部储存私有部分路径
		File mapFile = new File(cacheDir+ File.separator+"map");   //再将图片加载到目录之前先创建
		Log.d("test", "00000000" + mapFile.mkdir());
		File file = new File(cacheDir+ File.separator+"map",name);  //将图片保存到刚创建好的目录下
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* 连接网络取数据 */
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
}
