package call_back;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestCallBackActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//方式一
		Boos boss = new Boos();
		Work work = new Work();
		work.setCallBack(boss); //调用对应方法。参数为接口实现类 这样两个类就建立联系了
		work.doSomeThing();  //消息是在这个方法里面发送的
		
		Log.v("test", "++++++++++++++++++++++++++");
		//方式二
		new Work().setOnCall(new CallBackInterface() {
			@Override
			public void call(String str) {
				Log.v("test", "Boss收到消息  匿名内部类方式:" +str);
			}
		});
	}
}
