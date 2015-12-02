package adapter_view_refresh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import bean.Info;
import bean.Msg;

import com.example.call.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.warmtel.android.xlistview.XListView;
import com.warmtel.android.xlistview.XListView.IXListViewListener;

import cz.msebera.android.httpclient.Header;

public class XlistViewRefreshActivity extends Activity implements IXListViewListener{
	private XListView mxListView;
	RefreshAdapter adapter;
	private int pageNo = 1;   //第一次请求服务器时是加载的第一页数据
	private int totalPage;  //服务器返回的总页数
	private SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd-hh:mm:ss");  //格式化刷新时间
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xlistview_layout);
		mxListView = (XListView) findViewById(R.id.pager_listview);
		adapter = new RefreshAdapter(this);
		mxListView.setAdapter(adapter);
		mxListView.setXListViewListener(this);
	//	ConnectHttpByAsync_Http(pageNo);  //async_http方式请求
		getHttpData(pageNo);
	}
	public void getHttpData(final int pageNo){
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
				if(pageNo == 1){   //为1时为开始页，直接加载
					adapter.refreshData(strList);
				}else{   //后面的就是添加
					adapter.addData(strList);
				}
		//		mxListView.setPullRefreshEnable(true);   //设置下拉刷新可用
				mxListView.setPullLoadEnable(true);  //设置可以加载更多，让加载更多显示
				mxListView.stopRefresh();   //下拉刷新取到数据后停止刷新
			//	mxListView.setRefreshTime("刚刚");
				setTime();   //格式化更新时间
				
				mxListView.stopLoadMore();   //加载完成之后停止加载更多
				
				//当加载到最后一页时，隐藏加载更多
			//	if(pageNo == totalPage){
			//		mxListView.setPullLoadEnable(false);   
			//	}
				
			}
			
		});
	}
	
	/**
	 * 处理刷新时间
	 */
	public void setTime(){
		long systemTime = System.currentTimeMillis();
		mxListView.setRefreshTime(format.format(new Date(systemTime)));
	}
	
	public class RefreshAdapter extends BaseAdapter{
		ArrayList<String> data = new ArrayList<String>() ;
		private LayoutInflater inflater;
		
		public  RefreshAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
	
		public void refreshData(ArrayList<String> list){
			this.data = list;  //这里变成添加数据
			notifyDataSetChanged();
		}
		
		public void addData(ArrayList<String> list){
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
			
			String str = (String) getItem(position);
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
	
	
	
	@Override
	/**
	 * 下拉刷新
	 */
	public void onRefresh() {
		Mylog.i("onRefresh+++++++++++++");   
		pageNo = 1;   //下拉刷新时让pageNo = 1 重新加载数据
		getHttpData(pageNo);
		mxListView.setPullLoadEnable(true);   //让加载更多显示
	}
	@Override
	/**
	 * 下拉加载更多
	 */
	public void onLoadMore() {
		Mylog.i("onLoadMore+++++++上拉加载更多++++++");   
		if(++pageNo > totalPage){
			Mylog.d("onLoadMore-------else" + pageNo);
			Toast.makeText(XlistViewRefreshActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
			mxListView.stopLoadMore();
			mxListView.setPullLoadEnable(false);   //加载到最后一页，让加载更多隐藏
		}else{
			Mylog.d("onLoadMore-------if" + pageNo);
			getHttpData(pageNo);	
		}
	}
	
	
	
	
	
}
