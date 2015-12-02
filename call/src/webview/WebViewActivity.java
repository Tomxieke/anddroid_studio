package webview;


import mylog.Mylog;
import notification.NotificationActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.call.R;

 @SuppressLint("JavascriptInterface") public class WebViewActivity extends Activity {
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_activity_layout);
		
		/*第一步加HTML文件加载到WebView*/
		String url = "http://192.168.1.112:8080/HQ/hunqing.html"; 
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.loadUrl(url);
		
		/*在加载URL地址时回去启动浏览器，要使加载的URL地址在WebView内部跳转*/
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
		/*第三步加载行为*/
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new WebViewJavaScript(), WebViewJavaScript.JAVASCRIPT_NAME);
	}
	
	/*第二步构造交互接口名JavaScript_interface和交互方法show()等
	 *交互接口名和方法名都将在html中JavaScript中用到
	 *列如window.JavaScript_interface.show();*/
	class WebViewJavaScript{
		public static final String JAVASCRIPT_NAME = "JavaScript_interface"; //交互接口名
		public void show(){  //交互方法
			Mylog.v("=========================");
			Toast.makeText(WebViewActivity.this, "我是JavaScript接口方法", Toast.LENGTH_SHORT).show();
		}
		
		public void startActivity(){
			Intent intent = new Intent(WebViewActivity.this,NotificationActivity.class);
			WebViewActivity.this.startActivity(intent);
		}
		
		public void showImg(){
			Toast.makeText(WebViewActivity.this, "我是JavaScript接口方法", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	/*可能存在数据缓存问题，当第一次加载了html页面以后，Android系统会自动在data->data目录下新建数据库来保存加载的网络页面。有可能导致我们修改了网络html界面后
	 *再次通过Android系统进入网络界面时加载的是数据库保存的界面，所以当结束这个activity时可以将对应数据删除。就不会存在缓存问题了。
	 **/
	protected void onDestroy() {
		super.onDestroy();
		//遍历对应文件删除对应文件
	}
	

}
