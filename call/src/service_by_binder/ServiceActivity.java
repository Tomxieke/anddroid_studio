package service_by_binder;
import service_by_binder.MyBinderService.ServiceIBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.call.R;

public class ServiceActivity extends Activity implements OnClickListener{
	private Button mStartBtn,mGetConunBtn,mEndBtn,btn;
	private TextView mShowCountTxt;
	private int count;
	private ServiceIBinder mBinder;
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBinder = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinder = (ServiceIBinder) service;   //这里可以看到service回调过来的IBinder对象。从而可以得到service的一些参数
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_binder_layout);
		initView();
	}
	
	public void initView(){
		mStartBtn = (Button) findViewById(R.id.service_startBtn);
		mStartBtn.setOnClickListener(this);
		mGetConunBtn = (Button) findViewById(R.id.service_getCountBtn);
		mGetConunBtn.setOnClickListener(this);
		mShowCountTxt = (TextView) findViewById(R.id.service_text);
		mEndBtn = (Button) findViewById(R.id.service_distoryBtn);
		mEndBtn.setOnClickListener(this);
		
		btn = (Button) findViewById(R.id.service_ssBtn);
		btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.service_startBtn:
			count = 30;
			mBinder.setCount(count);  //调用方法设置对应参数
			break;
			
		case R.id.service_getCountBtn:
			int index = mBinder.getCount();  // 调用方法得到对应参数
			mShowCountTxt.setText("" + index);
			break;
		case R.id.service_distoryBtn:
			mBinder.changeFalg(false);
			if (mServiceConnection != null) {
				unbindService(mServiceConnection);
			}
			break;
		case R.id.service_ssBtn:   //多次执行也只会调用一次onCreate（）和onBind（）。  跟StartService有点区别。  StartService会重复调用OnStartCommend()方法
			Intent intent = new Intent(this,MyBinderService.class);
			bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
			break;
		}
	}
	
	
/*	@Override
	protected void onDestroy() {   //activity的onDestroy方法，当activity销毁时，讲service解绑销毁掉
		super.onDestroy();
		if (mServiceConnection != null) {
			unbindService(mServiceConnection);
		}
	}
	*/
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (mService != null) {
				unbindService(conn);
				mService.onDestroy();
			}
		}
		return super.onKeyDown(keyCode, event);
	}*/
}
