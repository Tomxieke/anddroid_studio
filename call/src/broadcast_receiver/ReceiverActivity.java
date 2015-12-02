package broadcast_receiver;

import mylog.Mylog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.call.R;

public class ReceiverActivity extends Activity {
	private Button elm_btn, sms_btn, move_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broadcast_receiver_main_layout);
		elm_btn = (Button) findViewById(R.id.broadcastReceiver_btnone);

		elm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Action.MyAction.ACTION_ONE);
				sendBroadcast(intent);
			}
		});

		sms_btn = (Button) findViewById(R.id.broadcastReceiver_btn_two);

		sms_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Action.MyAction.ACTION_TWO);
				sendBroadcast(intent);
			}
		});

		move_btn = (Button) findViewById(R.id.broadcastReceiver_btn_three);
		move_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MoveBroadCastRectiver.MOVE_ACTIONG);
				sendBroadcast(intent);  // 发送广播
			}
		});
	}

	/* 注册和注销广播都应放到activity可视生命周期中*/
	MoveBroadCastRectiver rectiver;
	@Override
	protected void onResume() {
		super.onResume();
		Mylog.i("======onResume=====");
		regiestBroadcastReceiver();  // 注册广播
	}

	@Override
	protected void onPause() {
		super.onPause();
		Mylog.i("======onPause=====");
		if (rectiver != null) {
			unregisterReceiver(rectiver);  //注销广播
		}
	}
	
	public void regiestBroadcastReceiver(){
		rectiver = new MoveBroadCastRectiver();
		IntentFilter filter = new IntentFilter(
				MoveBroadCastRectiver.MOVE_ACTIONG);
		registerReceiver(rectiver, filter); // 都动态注册广播
	}

	/* 动态广播 */
	public class MoveBroadCastRectiver extends BroadcastReceiver {
		private static final String MOVE_ACTIONG = "com.call.receiver";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MOVE_ACTIONG.equals(intent.getAction())) {
				Toast.makeText(context, "动态广播", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
