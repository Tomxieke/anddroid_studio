package adapter_view_refresh;

import java.util.ArrayList;

import mylog.Mylog;
import util.HttpClientConnectUtil;
import util.HttpClientConnectUtil.ConnectCallBack;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import bean.Info;
import bean.Msg;

import com.example.call.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class LastRefreshActivity extends Activity implements OnScrollListener{
	private ListView mListView;
	RefreshAdapter adapter;
	private int pageNo = 1;   //第一次请求服务器时是加载的第一页数据
	private int totalPage;  //服务器返回的总页数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.down_refresh_layout);
		mListView = (ListView) findViewById(R.id.refresh_list);
		adapter = new RefreshAdapter(this);
		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this);
		ConnectHttpByAsync_Http(pageNo);  //async_http方式请求
		getHttpData(pageNo);
		
		
	}
	public void getHttpData(int pageNo){
		String url = "http://192.168.1.112:8080/app/page?PageNo="+pageNo+"&PageSize=20";
		HttpClientConnectUtil util = new HttpClientConnectUtil();
		util.HttpConnectMethod(url, HttpClientConnectUtil.DO_GET, new ConnectCallBack(){

			@Override
			public void getConnectMsg(String msg) {
				Mylog.d(msg);
				Gson g = new Gson();
				Info info = g.fromJson(msg, Info.class);
				totalPage = Integer.valueOf(info.getTotalPage());
				ArrayList<Msg> msgList = info.getContent();
				ArrayList<String> strList = new ArrayList<String>();
				for(Msg m: msgList){
					strList.add(m.getMsg());
				}
				adapter.refreshData(strList);
			}
			
		});
	}
	
	public boolean isLastItam = false;   //是否滑动到了最后一项
	@Override
	/*滑动完成以及滑动中调用  scrollState=1开始滑动 ，scrollState=0 结束滑动，scrollState=2 缓存的时候  */
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Mylog.e("scrollState   "+scrollState);
		if(isLastItam && SCROLL_STATE_IDLE == scrollState){  //SCROLL_STATE_IDLE 常量 == 0 表示结束滑动
			if(pageNo < totalPage){
				getHttpData(++pageNo);
				isLastItam = false;
				Mylog.d("pageNo" + pageNo);
			}
		}
	}


	@Override
	/*滑动中调用的方法*/
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		Mylog.e("firstVisibleItem: "+firstVisibleItem+"   visibleItemCount: "+visibleItemCount +"   totalItemCount:"+totalItemCount);
		if((firstVisibleItem+visibleItemCount) == totalItemCount && totalItemCount > 0){
			isLastItam = true;
		}
	}
	
	
	public class RefreshAdapter extends BaseAdapter{
		ArrayList<String> data = new ArrayList<String>() ;
		private LayoutInflater inflater;
		
		public  RefreshAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
	
		public void refreshData(ArrayList<String> list){
			this.data.addAll(list);  //这里变成添加数据
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
			}
			
			String str = data.get(position);
			TextView txt = (TextView)convertView;
			txt.setText(str);
			return convertView;
		}
		
	}

	/**
	 * 第三方包 async-http-cilent请求网络连接
	 * @param pageNo
	 */
	public void ConnectHttpByAsync_Http(int pageNo){
		String url = "http://192.168.1.112:8080/app/page?PageNo="+pageNo+"&PageSize=20";
		AsyncHttpClient client = new AsyncHttpClient();
		
	/*	client.get(url, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Mylog.d("------"+arg2);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});*/
		client.get(url, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
				Mylog.i("++++statusCode  :" + statusCode);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				Mylog.d("++++++++++statusCode:"+statusCode +"------"+ new String(response));
			}
			
			@Override
			public void onRetry(int retryNo) {
				super.onRetry(retryNo);
				
			}
			
		});
		
	}
	
	
	
	
	
}
