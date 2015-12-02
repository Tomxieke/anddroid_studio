package service_by_binder;
/**
 * Bindservice启动service
 * 实现service与activity之间的交互
 * 1、定义一个交互接口
 * 2、定义一个IBinder类并实现交互接口 ServiceIBinder extends Binder implements ServiceCount
 * 3、onBind()方法返回该IBinder类的实列
 */
import mylog.Mylog;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyBinderService extends Service {
	private boolean flag = true;  //循环标签
	private int count;
	private ServiceIBinder serviceIBinder = new ServiceIBinder();
	public interface ServiceCount{   //交互接口
		public void setCount(int _count);
		public int getCount();
		public void changeFalg(boolean _flag);
		public void startDownLod();
	}
	
	public class ServiceIBinder extends Binder implements ServiceCount{
		@Override
		public void startDownLod() {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//耗时操作
				}
			}).start();
		}
		@Override
		public void setCount(int _count) {
			count = _count;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public void changeFalg(boolean _flag) {
			flag = _flag;  // 结束死循环
		}

		
		
	}
	
	
	@Override
	public void onCreate() { //启动了只调用一次
		super.onCreate();
		Mylog.i("-----------onCreate-----------------");
		new Thread(new Runnable() {
			@Override
			// 启动线程让count自增
			public void run() {
				while (flag) {
					try {
						Thread.sleep(1000);
						count++;
						Mylog.i("----------onCreate------------------" + count);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public IBinder onBind(Intent arg0) { // //启动了只调用一次
		Mylog.e("-----------onBind---------------------");
		Mylog.e("-----------onCreate-----------------");
		new Thread(new Runnable() {
			@Override
			// 启动线程让count自增
			public void run() {
				while (flag) {
					try {
						Thread.sleep(1000);
						count++;
						Mylog.e("----------onCreate------------------" + count);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		return serviceIBinder;        //将这个IBinder返回回去
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Mylog.i("-----------onUnbind---------------------");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		Mylog.i("-----------onDestroy---------------------");
	}

}
