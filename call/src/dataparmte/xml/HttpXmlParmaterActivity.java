package dataparmte.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import mylog.Mylog;

import util.HttpClientConnectUtil;
import util.HttpClientConnectUtil.ConnectCallBack;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.call.R;

public class HttpXmlParmaterActivity extends Activity implements OnClickListener{
	private Button mCreateXmlBtn,mGetXmlStrBtn;
	private ListView mListView;
	private TextView mShowXmlStrTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xml_parmater_forminternet_layout);
		mListView = (ListView) findViewById(R.id.xml_list);
		mShowXmlStrTxt = (TextView) findViewById(R.id.xml_showxmlstr_txt);
		
		mCreateXmlBtn = (Button) findViewById(R.id.xml_createxml_btn);
		mGetXmlStrBtn = (Button) findViewById(R.id.xml_getxmlstr_btn);
		mCreateXmlBtn.setOnClickListener(this);
		mGetXmlStrBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.xml_createxml_btn:
			mListView.setVisibility(View.GONE);
			mShowXmlStrTxt.setText(createXmlStr());
			break;
			
		case R.id.xml_getxmlstr_btn:
			mShowXmlStrTxt.setVisibility(View.GONE);
			getListData();
			break;
		}
	}
	
	
	public void getListData(){
		String url = "http://192.168.1.112:8080/XML/books.xml";
		new HttpClientConnectUtil().HttpConnectMethod(url, HttpClientConnectUtil.DO_GET,  new ConnectCallBack(){
			@Override
			public void getConnectMsg(String msg) {
				String xmlStr = msg;
				ByteArrayInputStream in = new ByteArrayInputStream(msg.getBytes());
				XmlParmterUtil util = new XmlParmterUtil();
				try {
					ArrayList<Book> booksList = util.parmateToBook(in);
					for(Book b:booksList){
						Mylog.d(b.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
	}
	
	/**
	* 创建xml
	 * @return  xmlStr
	*/
		public String createXmlStr(){
		ArrayList<Book> books = new ArrayList<Book>();
		Book  book = new Book();
		book.setId(11);
		book.setName("java宝典");
		book.setPrice("109.0");
		books.add(book);
		
		book = new Book();
		book.setId(111);
		book.setName("java入门");
		book.setPrice("24.0");
		books.add(book);
		
		XmlParmterUtil util = new XmlParmterUtil();
		String xmlStr = null;
		try {
			xmlStr = util.pramateToXmlStr(books);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlStr;
	}


		public class listAdapter extends BaseAdapter{
			private ArrayList<Book> data = new ArrayList<Book>();
			private LayoutInflater inflater;
			public listAdapter(Context context){
				inflater = LayoutInflater.from(context);
			}
			
			public void refishData(ArrayList<Book> listData){
				data = listData;
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
				
				return null;
			}
			
		}

}
