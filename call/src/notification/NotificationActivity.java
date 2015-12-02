package notification;

import music.mediaplayer.MusicActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.call.R;

public class NotificationActivity extends Activity implements OnClickListener{
	private Button mSendBtn,mUpdateBut,mCleanBtn,mNavigationBtn;
	NotificationManager manager;
	private Button mDowlendBtn ,mUnDowlendBtn,mBigViewBtn,mMyNotificationBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notification_main_layout);
		mSendBtn = (Button) findViewById(R.id.notification_btn);
		mUpdateBut = (Button) findViewById(R.id.notification_update_btn);
		mCleanBtn = (Button) findViewById(R.id.notification_clean_btn);
		mNavigationBtn = (Button) findViewById(R.id.notification_navigation_btn);
		mDowlendBtn = (Button) findViewById(R.id.notification_dowlend_btn);
		mUnDowlendBtn = (Button) findViewById(R.id.notification_unknowdowlend_btn);
		mBigViewBtn = (Button) findViewById(R.id.notification_bigView_btn);
		mMyNotificationBtn = (Button) findViewById(R.id.notification_myself_btn);
		mSendBtn.setOnClickListener(this); 
		mMyNotificationBtn.setOnClickListener(this); 
		mBigViewBtn.setOnClickListener(this); 
		mUnDowlendBtn.setOnClickListener(this); 
		mNavigationBtn.setOnClickListener(this); 
		mCleanBtn.setOnClickListener(this); 
		mUpdateBut.setOnClickListener(this); 
		mDowlendBtn.setOnClickListener(this); 
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  //这个可以共用
			
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.notification_btn:
			sendNotification();
			break;
		case R.id.notification_update_btn:
			updateNotification();
			break;
		case R.id.notification_clean_btn:
			cleanNotification();
			break;
		case R.id.notification_navigation_btn:
			navigationNotification();
			break;
		case R.id.notification_dowlend_btn:
			dowlendNotification();
			break;
		case R.id.notification_unknowdowlend_btn:
			unknowDowlendNotification();
			break;
		case R.id.notification_bigView_btn:
			bigViewNotification();
			break;
		case R.id.notification_myself_btn:
			myNotification();
			break;
		}
	}
	
	/*发送通知*/
	public void sendNotification() {
		/* 构造通知显示界面 */
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setSmallIcon(R.drawable.img_smile); // 设置通知显示图标
		builder.setContentTitle("饿了么");
		builder.setContentText("你有新的饿了么订单，请及时处理");
		builder.setAutoCancel(true);  //点击通知后消失
		builder.setTicker("你有新的饿了么订单，请及时处理");  //通知出现时让其滚动显示这段内容
		
		/* 通知行为，点击通知时启动actvity 行为必须放在发送通知之前*/
		Intent intent = new Intent(this, MusicActivity.class);
		int requestCode = 1; // 请求码，跟activity有回传值启动请求码一致
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);  //点击时启动activity

		/* 发送通知 */
	//	NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int notificationId = 1; // 作为通知的唯一标识id，当多次发送时会去比照这个id若有这个id并且通知还没取消，则不会再次发送。
		// Notification notification = builder.getNotification(); // 这个方法过时了
		Notification notification = builder.build();
		manager.notify(notificationId, notification);
	}

	/*刷新通知*/
	public void updateNotification(){
		/*设置通知界面*/
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setSmallIcon(R.drawable.img_person); // 设置通知显示图标
		builder.setContentTitle("刷新饿了么");
		builder.setContentText("刷新饿了么++你有新的饿了么订单，请及时处理");
		builder.setAutoCancel(true);  //点击通知后消失
		builder.setTicker("刷新饿了么——————你有新的饿了么订单，请及时处理");  //通知出现时让其滚动显示这段内容
		
		
		int notificationId = 1; // 统一唯一标识id就可以刷新通知
		// Notification notification = builder.getNotification(); // 这个方法过时了
		Notification notification = builder.build();
		manager.notify(notificationId, notification);
	}
	
	/*清除通知*/
	public void cleanNotification(){
		manager.cancel(1);
	}
	
	/*通知导航1、常规activity（工作流中的axtivity） 2、特殊activity，工作流之外的activity*/
	public void navigationNotification(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setSmallIcon(R.drawable.meituan_image8); // 设置通知显示图标
		builder.setContentTitle("导航通知");
		builder.setContentText("导航通知，点击通知启动activity返回时不返回到主界面，返回到指定的parentactivity界面");
		builder.setAutoCancel(true);  //点击通知后消失
		builder.setTicker("导航通知，点击通知启动activity返回时不返回到主界面，返回到指定的parentactivity界面");  //通知出现时让其滚动显示这段内容
		
		/* 通知行为，点击通知时启动actvity 行为必须放在发送通知之前*导航通知就需要建立返回栈并在xml文件中指定父activity*/
		Intent intent = new Intent(this, MusicActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);  //得到返回栈
		stackBuilder.addParentStack(MusicActivity.class);  //向返回栈中压入activity，这里注意不是压入的父activity，而是点击通知启动的activity
		stackBuilder.addNextIntent(intent);
		
		int requestCode = 1; // pendingIntent 是由stackBuilder工厂得到
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingIntent);  //点击时启动activity

		/* 发送通知 */
	//	NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//  启动这个服务可以定义为全局
		int notificationId = 11; // 作为通知的唯一标识id，当多次发送时会去比照这个id若有这个id并且通知还没取消，则不会再次发送。
		// Notification notification = builder.getNotification(); // 这个方法过时了
		Notification notification = builder.build();
		manager.notify(notificationId, notification);
	}
	
	/*确定型下载通知*/
	public void dowlendNotification(){
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.img_buetyful);
		builder.setContentTitle("下载通知");
		builder.setContentText("正在下载xxxx");
		builder.setTicker("开始下载xxxxxxx");
		builder.setAutoCancel(true);
		
		final int notificationId = 100;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 1;i <= 100;i++){
					builder.setProgress(100, i, false);
					manager.notify(notificationId, builder.build());  //builder.build()这个参数必须在这里定义
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				builder.setContentText("下载完成");
				builder.setProgress(0, 0, false);  // 下载完成让进度条消失
				manager.notify(notificationId, builder.build());
			}
		}).start();
	}
	
	/*不确定型下载通知*/
	public void unknowDowlendNotification(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.img_buetyful);
		builder.setContentTitle("下载通知");
		builder.setContentText("正在下载xxxx");
		builder.setTicker("开始下载xxxxxxx");
		builder.setAutoCancel(true);
		builder.setProgress(0, 0, true);  // true false 决定是确定型seekbar还是不确定型seekbar
		int notificationId = 101;
		manager.notify(notificationId, builder.build());
	}
	
	/*bigView通知*/
	public void bigViewNotification(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.meituan_image8);
		builder.setContentTitle("闹钟");
		//builder.setContentText("睡你麻痹起来嗨");
		builder.setTicker("睡你麻痹起来嗨");
		builder.setAutoCancel(true);
		builder.setStyle(new NotificationCompat.BigTextStyle().bigText("睡你麻痹起来嗨"));
		
		
		Intent playIntent = new Intent(this,PlayerService.class);
		playIntent.setAction("aaaa");  //必须设置action才能传不同的参数
		playIntent.putExtra("MUSIC_STATE", 11);
		playIntent.putExtra("MUSIC_PATH", "file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/a.mp3");
		PendingIntent playPendingIntent = PendingIntent.getService(this, 10, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.addAction(R.drawable.img_person, "播放", playPendingIntent);   //设置下面两个按钮显示的图标以及内容
		
		Intent pauseIntent = new Intent(this,PlayerService.class);
		pauseIntent.putExtra("MUSIC_STATE", 1);
		pauseIntent.setAction("bbbb");//这里也要设置action而且不能刚上面的一样
		PendingIntent pausePendingIntent = PendingIntent.getService(this, 10, pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.addAction(R.drawable.img_smile, "暂停", pausePendingIntent);
		int id = 1002;
		manager.notify(id, builder.build());
	}
	
	/*自定义通知显示栏*/
	public void myNotification(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.adapter_homework_icon);  //自定义Notification必须至少设置这项才会发送成功
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_notification_layout);   //xml资源布局文件
		
		Intent playIntent = new Intent(this,PlayerService.class);
		playIntent.setAction("ccc");  //必须设置action才能传不同的参数
		playIntent.putExtra("MUSIC_STATE", 11);
		playIntent.putExtra("MUSIC_PATH", "file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/a.mp3");
		PendingIntent pendingIntent = PendingIntent.getService(this, 10, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.mp3_puseMusic_btn, pendingIntent);  //为自定义通知上的图标添加点击事件。
		
		builder.setContent(remoteViews);
		int id = 111;
		manager.notify(id, builder.build());
	}
}
