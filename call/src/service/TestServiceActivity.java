package service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.call.R;

public class TestServiceActivity extends Activity implements OnClickListener{
	private Button mStartServiceBtn,mEndServiceBtn;
	private MySercive mService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_activity_layout);
		initView();
	}
	
	public void initView(){
		mStartServiceBtn = (Button) findViewById(R.id.service_start_btn);
		mStartServiceBtn.setOnClickListener(this);
		mEndServiceBtn = (Button) findViewById(R.id.service_end_btn);
		mEndServiceBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.service_start_btn:   //多次启动，会执行多次onStartCommand()方法
			Intent intent = new Intent(TestServiceActivity.this,MySercive.class);
			startService(intent);
			break;
		case R.id.service_end_btn:
			Intent endIntent = new Intent(TestServiceActivity.this,MySercive.class);
			stopService(endIntent);
			break;
		}
	}
	

}
