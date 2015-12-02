package broadcast_receiver;

/**
 * BroadcastReceiver:广泛应用于应用程序之间的信息传递
 * 分为静态广播和动态广播
 * 下面这种是静态广播    1、继承BroadcastReceiver，实现onReceive方法。2、Manifest文件里面注册广播  3、需要发送的位置发送广播
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MeBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(Action.MyAction.ACTION_ONE)){
			Toast.makeText(context, "你有新的饿了么订单，请及时处理", Toast.LENGTH_SHORT).show();
		}else if(action.equals(Action.MyAction.ACTION_TWO)){
			Toast.makeText(context, "账号被盗，请修改密码", Toast.LENGTH_SHORT).show();
		}else if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
			Toast.makeText(context, "系统短信action", Toast.LENGTH_SHORT).show();
		}
	}

}
