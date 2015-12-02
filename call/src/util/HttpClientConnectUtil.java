package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



import android.os.AsyncTask;

/**
 * HttpClient连接网络的封装工具类  
 * 采用回调机制将服务器响应的参数传回给客服端
 * @author scxh
 *
 */
public class HttpClientConnectUtil {
	public static final String DO_GET = "GET";
	public static final String DO_POST = "POST";
	/*定义回调接口*/
	public interface ConnectCallBack{   
		public void getConnectMsg(String msg);
	}
	/*注册接口*/
	private ConnectCallBack mCallBack;
	public void setConnectCallBack(ConnectCallBack hCallBack){
		mCallBack = hCallBack;
	}
	
	/*不带参数的带参数注册接口*/
	public void HttpConnectMethod(String url,final String MethodType,ConnectCallBack hCallBack){
		
		 HttpConnectMethod(url,MethodType,null,hCallBack);
	}
	
	/*带参数的 对HttpClient请求网络的一个封装*/
	public void HttpConnectMethod(String url,final String MethodType,final HashMap<String,Object> parameters,ConnectCallBack hCallBack){
		mCallBack = hCallBack;
		new AsyncTask<String, Void, String>(){
			@Override
			protected String doInBackground(String... params) {
				return connectAndGetMessage(params[0],MethodType,parameters);
			}
			
			@Override
			protected void onPostExecute(String result) {
				mCallBack.getConnectMsg(result); //回调将结果传回去
			};
		}.execute(url);
	}
	
	/*连接网络取数据*/
	public String connectAndGetMessage(String url,String MethodType,HashMap<String,Object> parameters){
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = getRequest(url,MethodType,parameters);
		String msg = null;
		if(request != null){   
			try {
				HttpResponse response = client.execute(request);
				if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK){   //网络请求是否成功
					msg = EntityUtils.toString(response.getEntity());   //得到响应数据
				}else{
					msg = "网络出错";
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}
	
	/*将获取request的代码封装成方法*/
	public HttpUriRequest getRequest(String url,String MethodType,HashMap<String,Object> parameters){
		String httpUrl = url;
		if(MethodType.equalsIgnoreCase(HttpClientConnectUtil.DO_GET)){   //get请求
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
			return getRequest;
		}else if(MethodType.equalsIgnoreCase(HttpClientConnectUtil.DO_POST)){  //post请求
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
			return postRequst;
		}
		return null;
	}
}
