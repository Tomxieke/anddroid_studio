package dataparmte_json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mylog.Mylog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.Info;
import bean.Msg;

import com.google.gson.Gson;

import android.content.res.AssetManager;
import android.test.AndroidTestCase;
import android.util.Log;

public class JsonTest extends AndroidTestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	
	public void testJson(){
		//{"student":{"name":"小红","age":12,"id":"2011_1_1","sex":"女"}}
		//{"name":"张三","sex":"男"}
		
		String str = "{\"student\":{\"name\":\"小红\",\"age\":12,\"id\":\"2011_1_1\",\"sex\":\"女\"}}";
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject object = jsonObject.getJSONObject("student");  //{"name":"小红","age":12,"id":"2011_1_1","sex":"女"}
			String name = object.getString("name");
			int age = object.getInt("age");
			String id = object.getString("id");
			String sex = object.getString("sex");
			Mylog.i("name:"+name+"   age:  "+ age+"   id :" + id+"   sex: "+sex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void testJSONArray(){
	
		String str = "[ { \"name\": 张三\", \"age\":22, \"email\": \"zhangsan@qq.com\" }," 
					+"{ \"name\": \"李四\", \"age\":23, \"email\": \"lisi@qq.com\"},"
				    +"{ \"name\": \"王五\", \"age\":24, \"email\": \"wangwu@qq.com\" }]";
		try {
			JSONArray jsonArray = new JSONArray(str);
			for(int i=0;i<jsonArray.length();i++){
				JSONObject object = jsonArray.getJSONObject(i);
				String name = object.getString("name");
				int age = object.getInt("age");
				String email = object.getString("email");
				Mylog.i("name:"+name+"   age:  "+ age+"   email :" + email);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void testAssetsFile(){
		AssetManager manager = getContext().getAssets();
		try {
			InputStream in = manager.open("json_xx");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String str;
			StringBuilder sb = new StringBuilder();
			while((str=reader.readLine()) != null){
				sb.append(str);
			}
			in.close();
			reader.close();
			Mylog.e(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*遍历Assets目录下的文件*/
	public void scanAssetsFile() throws IOException{
		AssetManager manager = getContext().getAssets();
		String[] list = manager.list("");
		for(String file:list){
			Mylog.i("file   "+file);
		}
	}
	
	/**
	 * 格式化时间
	 */
	public void setTimes(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd---hh:mm:ss");  
		//yyyy:MM:dd---hh:mm:ss   决定了时间显示的格式
		long systemTime = System.currentTimeMillis();  //获取系统时间；
		String timeStr = sdf.format(new Date(systemTime));
		Log.d("test", timeStr);
	}
	
	/**
	 * org.json构造JSON字符串
	 * @throws JSONException 
	 * 
	 */
	public void createJSONStr() throws JSONException {
		JSONArray array = new JSONArray();
		for (int i = 20; i < 40; i++) {
			JSONObject ob = new JSONObject();
			ob.put("msg", "第 " + i + " 条新闻");
			array.put(ob);
		}

		JSONObject OB = new JSONObject();
		OB.put("code", "1");
		OB.put("message", "message");
		OB.putOpt("content", array);
		String jsonStr = OB.toString();
		Log.v("test", jsonStr);
		// ------------------上面是构造JSON对象，下面将这个JSON对象解析成Msg类对象 和Info对象。

		JSONObject object = new JSONObject(jsonStr);
		JSONArray arrayInfo = object.getJSONArray("content");
		for (int i = 0; i < arrayInfo.length(); i++) {
			JSONObject o = arrayInfo.getJSONObject(i);
			String msg = o.getString("msg");
			Log.v("test", msg);
		}
	}
	
	/**
	 * fastjson 处理JSON
	 */
	public void createJSONByFastJson(){
		ArrayList<String> strlist = new ArrayList<String>();
		for(int i=20;i<40;i++){
			String msg = "第  " + i +"  条新闻；";
			strlist.add(msg);
		}
		
		ArrayList<Msg> list = new ArrayList<Msg>();
		for(int i = 0;i<strlist.size();i++){
			Msg msg = new Msg();
			msg.setMsg(strlist.get(i));
			list.add(msg);
		}
		
		Info info = new Info();
		info.setCode("1");
		info.setContent(list);
		info.setMessage("message");
		info.setTotalPage("6");
		
		String fastJson = com.alibaba.fastjson.JSONObject.toJSONString(info);
		Log.d("test", fastJson);  //log打印出来是完全正确的
		
		//----------------上面生成JSON字符串，下面来将JSON字符串解析成对象
		Info decodeInfo = (Info) com.alibaba.fastjson.JSONObject.parseObject(fastJson, Info.class);
		Log.d("test", decodeInfo.getCode());  //打印出来是  1
		
		ArrayList<Msg> msgs = decodeInfo.getContent();
		for(Msg m:msgs){
			Log.d("test", m.getMsg());
		}
	}
	
	
	
}
