package webview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import mylog.Mylog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import util.HttpClientConnectUtil;
import util.HttpClientConnectUtil.ConnectCallBack;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.call.R;

public class HttpServeletActivity extends Activity implements OnClickListener{
	private TextView mShowMsgTxt;
	private Button mConnecyBtn,mDopostBtn,mHttpClientGetBtn,mHttpClientPostBtn,mHttpClientBtn,mHttpClientCallBackBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.httpservelet_main_layout);
		mShowMsgTxt = (TextView) findViewById(R.id.http_text);
		
		mConnecyBtn = (Button) findViewById(R.id.http_doget_btn);
		mConnecyBtn.setOnClickListener(new OnClickListener() {
			String httpUrl = "http://192.168.1.112:8080/app/myweb";
			@Override
			public void onClick(View v) {
				new AsyncTask<String, Void, String>(){
					@Override
					protected String doInBackground(String... params) {
						return getHttpDoget(params[0]);
					}
					@Override
					protected void onPostExecute(String result) {
						mShowMsgTxt.setText(result);
					};
				}.execute(httpUrl);
			}
		});
		
		mDopostBtn = (Button) findViewById(R.id.http_dopost_btn);
		mDopostBtn.setOnClickListener(new OnClickListener() {
			String http = "http://192.168.1.112:8080/app/myweb";
			@Override
			public void onClick(View v) {
				new AsyncTask<String, Void, String>(){
					@Override
					protected String doInBackground(String... params) {
						return getHttpDopost(params[0]);
					}
					@Override
					protected void onPostExecute(String result) {
						mShowMsgTxt.setText(result);
					};
				}.execute(http);
			}
		});
		
		mHttpClientGetBtn = (Button) findViewById(R.id.http_client_get_btn);
		mHttpClientGetBtn.setOnClickListener(this);
		mHttpClientPostBtn = (Button) findViewById(R.id.http_client_post_btn);
		mHttpClientPostBtn.setOnClickListener(this);
		mHttpClientBtn = (Button) findViewById(R.id.httpclient_getpost_btn);
		mHttpClientCallBackBtn = (Button) findViewById(R.id.httpclient_callback_btn);
		mHttpClientBtn.setOnClickListener(this);
		mHttpClientCallBackBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.http_client_get_btn:
			String http = "http://192.168.1.112:8080/app/myweb";
			new AsyncTask<String, Void, String>(){
				@Override
				protected String doInBackground(String... params) {
					HttpClient client = new DefaultHttpClient();  //得到客户端
					String permeterUrl = params[0] +"?username=小明&password=123456";  //带参数的。
					HttpGet getRequest = new HttpGet(permeterUrl);   //确定是get请求  若是post请求的话就构造post请求  若要传参的话，get请求参数同样是加在url地址后面。
			//		HttpPost postRequest = new HttpPost(params[0]);  // 若是post请求的话就构造post请求
					try {
						HttpResponse response = client.execute(getRequest);   //执行请求并得到响应
						String str = EntityUtils.toString(response.getEntity());   //得到响应数据
						/*得到响应数据还可以通过流的方式*/
						/*InputStream in = response.getEntity().getContent();
						BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));  //得到连接服务器的缓冲流
						String line;
						StringBuilder sb = new StringBuilder();
						while((line=br.readLine()) != null){    //从服务中读取请求返回的数据
							sb.append(line);
						}
						return sb.toString();*/
						return str;
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(String result) {
					mShowMsgTxt.setText(result + "    HttpClient的响应");
				};
				
			}.execute(http);
			break;
		
		case R.id.http_client_post_btn:  //post请求带参
			String httpUrl = "http://192.168.1.112:8080/app/myweb";
			new AsyncTask<String, Void, String>(){
				@Override
				protected String doInBackground(String... params) {
					HttpClient client = new DefaultHttpClient();
					HttpPost postRequst = new HttpPost(params[0]);
					BasicNameValuePair namePair = new BasicNameValuePair("username", "小明");  //post请求要借助这个类来传参
					BasicNameValuePair passwordPair = new BasicNameValuePair("password", "11111");
					ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
					pairs.add(namePair);
					pairs.add(passwordPair);
					try {
						HttpEntity entity = new UrlEncodedFormEntity(pairs, "UTF-8");  //设置编码字符集防止乱码
						postRequst.setEntity(entity);  //向请求中加载参数
						HttpResponse response = client.execute(postRequst);  //执行请求返回响应对象
						String str = EntityUtils.toString(response.getEntity());
						return str;
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					return null;
				}
				
				@Override
				protected void onPostExecute(String result) {
					mShowMsgTxt.setText(result + "    HttpClient的post含参响应");
				};
				
			}.execute(httpUrl);
			break;
		case R.id.httpclient_getpost_btn:
			String url = "http://192.168.1.112:8080/app/myweb";
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("username", "小花");
			map.put("password", "123");
			HttpConnectMethod(url, "post", map);
			break;
		case R.id.httpclient_callback_btn:
			String callbackurl = "http://192.168.1.112:8080/app/myweb";
			HashMap<String,Object> callbackmap = new HashMap<String,Object>();
			callbackmap.put("username", "admin");
			callbackmap.put("password", "12321");
			
			/*ConnectCallBack callBack = new ConnectCallBack(){
				@Override
				public void getConnectMsg(String msg) {
					mShowMsgTxt.setText(msg + "    HttpClient回调封装的工具类响应");
				}
			};
			HttpClientConnectUtil connectUtil = new HttpClientConnectUtil();
			connectUtil.setConnectCallBack(callBack);
			connectUtil.HttpConnectMethod(callbackurl, "post", callbackmap);*/
			/*带参数的请求*/
			new HttpClientConnectUtil().HttpConnectMethod(callbackurl, HttpClientConnectUtil.DO_GET, callbackmap, new ConnectCallBack(){
				@Override
				public void getConnectMsg(String msg) {
					mShowMsgTxt.setText(msg + " -------   HttpClient回调封装的工具类响应+++++");
				}});
			
			/*不带参数的请求
			String callbackurl = "http://192.168.1.112:8080/app/and";
			new HttpClientConnectUtil().HttpConnectMethod(callbackurl, HttpClientConnectUtil.DO_GET,  new ConnectCallBack(){
				@Override
				public void getConnectMsg(String msg) {
					mShowMsgTxt.setText(msg + " -------   HttpClient回调封装的工具类响应+++++");
				}});*/
			break;
		}
	}
	
	/*post请求方式连接服务器,给服务器传递参数用流写入的方式*/
	public String getHttpDopost(String httpUrl){
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");  //请求方法
			conn.setConnectTimeout(15000);  //设置连接超时
			conn.setReadTimeout(10000);  //设置读取超时
			conn.connect();  //建立连接
			
		//	int statusCode = conn.getResponseCode();  //得到请求码   请求码==200才说明跟服务器连接成功了。执行这句后若不判断的话后面的代码都不会执行了。
			/*if(statusCode == HttpURLConnection.HTTP_OK){
				Mylog.v("连接成功---Code:" + statusCode);
				//这里面再执行连接操作。
			}*/
			
			/*得到流向服务器传数据*/
			OutputStream out = conn.getOutputStream();
			PrintWriter pr = new PrintWriter(out);
			pr.print("username=小明&password=1234");
			pr.flush();
			Mylog.v("连接成功---Code==============:" );
			/*读取服务端数据*/
			InputStream in = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));  //得到连接服务器的缓冲流
			String line;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine()) != null){    //从服务中读取请求返回的数据
				sb.append(line);
			}
			in.close();//关闭连接关闭流
			out.close();
			conn.disconnect();//关闭连接
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/*get请求方式连接服务器   get请求传递参数在url后面加参数*/
	public String getHttpDoget(String httpUrl){
		try {
			String name = URLEncoder.encode("小红", "utf-8");   //中文输入先指定编码，不然会出现乱码
			//get请求带参数的URL地址   http://192.168.1.112:8080/app/myweb?username=小红&password=abcd  
			URL url = new URL(httpUrl+"?username="+name+"&password=abcd"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");	//请求方式
			conn.setConnectTimeout(15000);  //设置连接超时
			conn.setReadTimeout(10000);  //设置读取超时
			conn.connect();  //建立连接
			
			InputStream in = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));  //得到连接服务器的缓冲流
			String line;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine()) != null){    //从服务中读取请求返回的数据
				sb.append(line);
			}
			in.close();  //关闭连接关闭流
			conn.disconnect();
			return sb.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*对HttpClient请求网络的一个封装*/
	public void HttpConnectMethod(String url,final String MethodType,final HashMap<String,Object> parameters){
		new AsyncTask<String, Void, String>(){
			@Override
			protected String doInBackground(String... params) {
				String httpUrl = params[0];
				HttpClient client = new DefaultHttpClient();
				if(MethodType.equalsIgnoreCase("GET")){   //get请求
					if(parameters != null){  //参数不为空。
						httpUrl = httpUrl + "?";
						StringBuilder sb = new StringBuilder(httpUrl);
						for(String key:parameters.keySet()){
							Object values = parameters.get(key);
							sb.append(key).append("=").append(values).append("&");   
							//http://192.168.1.112:8080/app/myweb?username=xxx&password=xxx&  就是多出一个&
						}
						httpUrl = sb.substring(0, sb.length()-1);
					}
					HttpGet getRequest = new HttpGet(httpUrl);
					try {
						HttpResponse response = client.execute(getRequest);
						String str = EntityUtils.toString(response.getEntity());   //得到响应数据
						return str;
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}   //执行请求并得到响应
				}else if(MethodType.equalsIgnoreCase("post")){  //post请求
					HttpPost postRequst = new HttpPost(httpUrl);
					if(parameters != null){  //带参数的
						ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
						for(String key:parameters.keySet()){
							Object values = parameters.get(key);
							BasicNameValuePair Pair = new BasicNameValuePair(key, (String) values);
							pairs.add(Pair);
						}
						try {
							HttpEntity entity = new UrlEncodedFormEntity(pairs, "UTF-8");//设置编码字符集防止乱码
							postRequst.setEntity(entity);  //向请求中加载参数
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}  
					}
					try {
						HttpResponse response = client.execute(postRequst);  //执行请求返回响应对象
						String str = EntityUtils.toString(response.getEntity());
						return str;
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				mShowMsgTxt.setText(result + "    HttpClient封装响应");
			};
		}.execute(url);
	}


	
	
}
