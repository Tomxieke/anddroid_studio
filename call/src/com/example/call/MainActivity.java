package com.example.call;

/**
 * 进一步简化一下测试代码
 * 在封装一层
 */

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.StudyActivityLifeOne;
import adapter.AdapterSimpleAdapter;
import adapter.ListArrayAdapter;
import adapter.MyAdapterActivity;
import adapter.MyMessageAdapterActivity;
import adapter_view_refresh.LastRefreshActivity;
import adapter_view_refresh.XlistViewRefreshActivity;
import app.login.ui.LoginLogin;
import app.login.ui.WeiXinLogin;
import application.MyApplication;
import asynctask.AsynctaskActivity;
import asynctask.GridViewAsynctaskActivity;
import broadcast_receiver.ReceiverActivity;
import call_back.TestCallBackActivity;
import data.contentprovider.sms.SmsTestActivity;
import data.files.WritePrictureToStoreActivity;
import dataparmte.xml.HttpXmlParmaterActivity;
import event.ButtonOnClickEvent;
import event.LaunchMadeSingTop;
import event.LearnActivityLaunchMade;
import event.LearnSaveStateActivity;
import fragment_demo.Test_fragment.TestFragmentActivity;
import fragment_demo.fragment.FragmentDomeActivity;
import fragment_demo.fragment.FragmentMainActivity;
import fragment_demo.fragment.FragmentStackActivity;
import fragment_demo.fragment.WorkFragmentActivity;
import fragment_demo.sendMsg.FragmentMsgActivity;
import fragment_demo.subfragment.SubFragmentActivity;
import fragment_demo.tabfragment.FragmentStateActivity;
import fragment_demo.tabfragment.FragmentTabHostActivity;
import fragment_demo.tabfragment.TabFragmentActivity;
import fragment_demo.test_fragment_life.FragmentLifeActivity;
import intent.TestIntentOne;
import music.mediaplayer.Mp3PlayerActivity;
import music.mediaplayer.MusicActivity;
import notification.NotificationActivity;
import progess_bar.ProgressBarActivity;
import scan_files.LookFilesActivity;
import senddatebetweenactivity.OneActivity;
import service.TestServiceActivity;
import service_by_binder.ServiceActivity;
import study_dialog.ExitDialog;
import study_dialog.TestDialogActivity;
import study_handler.HandlerActivity;
import study_menu.ActionBarActivity;
import study_menu.ContentMenuActivity;
import study_menu.OptionMenuActivity;
import studytest.AdapterHomeworkActivity;
import studytest.ImageInTextActivity;
import tabhost.PopWindowsActivity;
import tabhost.TabHostActivity;
import tools.ShapeActivity;
import ui_adapter_wiaget.ForeverViewPagerActivity;
import ui_adapter_wiaget.GridViewActivty;
import ui_adapter_wiaget.HistoryAutoCompleteTextViewActiviity;
import ui_adapter_wiaget.SetTitleActivity;
import ui_adapter_wiaget.StudySpinnerActivity;
import ui_adapter_wiaget.ViewPagerActivity;
import webview.HttpServeletActivity;
import webview.WebViewActivity;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<Map<String, Object>> data = this.getData(); // 调用方法得到资源文件
		setListAdapter(new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_1, // 实列化适配器
				new String[] { "title" }, new int[] { android.R.id.text1 }));
		
		setSelection(data.size()-1);  //设置进入时光标处于最底部
	}
	/*
	 * (non-Javadoc)  重写退出按钮   弹出自定义的对话框 要处理点击事件哟
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){  // 如果是退出请求
			ExitDialog  exitDialog = new ExitDialog(this ,this);
			exitDialog.show();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	
	protected List<Map<String, Object>> getData() { // 封装添加资源方法
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(); // 用来保存数据源
		addItem(data, "布局管理器学习", LayoutMainActivity.class); // 调用方法，添加第一个元素
		addItem(data, "微信登录界面", WeiXinLogin.class);
		addItem(data, "事件处理", ButtonOnClickEvent.class);
		addItem(data, "控件学习", WiagetMainActivity.class);
		addItem(data, "Activity生命周期学习", StudyActivityLifeOne.class);
		addItem(data, "SaveState状态保存", LearnSaveStateActivity.class);
		addItem(data, "Activity的启动模式之标准模式->单实例", LearnActivityLaunchMade.class);
		addItem(data, "Activity的启动模式之单顶模式->单任务", LaunchMadeSingTop.class);
		addItem(data, "LoginLogin", LoginLogin.class);
		addItem(data, "Application", MyApplication.class);
		addItem(data, "Activity之间的参数传递", OneActivity.class);
		addItem(data, "Intent学习", TestIntentOne.class);
		addItem(data, "ArrayAdapter学习", ListArrayAdapter.class);
		addItem(data, "SimpleAdapter学习", AdapterSimpleAdapter.class);
		addItem(data, "自定义adapter学习", MyAdapterActivity.class);
		addItem(data, "自定义对象传值adapter学习", MyMessageAdapterActivity.class);
		addItem(data, "美团adapter", AdapterHomeworkActivity.class);
		addItem(data, "Spinner控件联系适配器", StudySpinnerActivity.class);
		addItem(data, "由历史记录提示的AutoComplete", HistoryAutoCompleteTextViewActiviity.class);
		addItem(data, "GridView学习", GridViewActivty.class);
		addItem(data, "文本中加载图片", ImageInTextActivity.class);
		addItem(data, "自定义标题title", SetTitleActivity.class);
		addItem(data, "引导界面ViewPager学习", ViewPagerActivity.class);
		addItem(data, "无限循环滑动ViewPager", ForeverViewPagerActivity.class);
		addItem(data, "进度条ProgressBar学习", ProgressBarActivity.class);
		addItem(data, "Dialog对话框学习", TestDialogActivity.class);
		addItem(data, "TabHost学习", TabHostActivity.class);
		addItem(data, "Menu学习", OptionMenuActivity.class);
		addItem(data, "Content_Menu学习", ContentMenuActivity.class);
		addItem(data, "ActionBar学习", ActionBarActivity.class);
		addItem(data, "shape处理色块", ShapeActivity.class);
		addItem(data, "PopWindow", PopWindowsActivity.class);
		addItem(data, "Handler", HandlerActivity.class);
		addItem(data, "回调机制-类之间的信息传递", TestCallBackActivity.class);
	//	addItem(data, "SQL数据查询", SQLdbActivity.class);
		addItem(data, "向手机储存中的各个部分写入图片", WritePrictureToStoreActivity.class);
		addItem(data, "文件管理器", LookFilesActivity.class);
		addItem(data, "监听短信", SmsTestActivity.class);
		addItem(data, "播放音乐", MusicActivity.class);
		addItem(data, "MP3播放器", Mp3PlayerActivity.class);
		addItem(data, "service服务", TestServiceActivity.class);
		addItem(data, "Binder启动服务与activity交互", ServiceActivity.class);
		addItem(data, "broadcastReceiver广播", ReceiverActivity.class);
		addItem(data, "notification通知", NotificationActivity.class);
		addItem(data, "异步操作asynctask", AsynctaskActivity.class);
		addItem(data, "异步操作asynctask网络加载图片", GridViewAsynctaskActivity.class);
		addItem(data, "webview学习", WebViewActivity.class);
		addItem(data, "httpservelet学习", HttpServeletActivity.class);
		addItem(data, "xml解析学习", HttpXmlParmaterActivity.class);
		addItem(data, "listView下拉刷新", LastRefreshActivity.class);
		addItem(data, "XlistView下拉刷新", XlistViewRefreshActivity.class);
		addItem(data, "Fragment学习", FragmentDomeActivity.class);
		addItem(data, "TestFragmentActivity", TestFragmentActivity.class);
		addItem(data, "FragmentLifeActivity", FragmentLifeActivity.class);
		addItem(data, "Fragment之参数传递", FragmentMsgActivity.class);
		addItem(data, "Fragment之参数传递二", FragmentMainActivity.class);
		addItem(data, "WorkFragmentActivity", WorkFragmentActivity.class);
		addItem(data, "FragmentStackActivity", FragmentStackActivity.class);
		addItem(data, "SubFragmentActivity", SubFragmentActivity.class);
		addItem(data, "TabFragmentActivity", TabFragmentActivity.class);
		addItem(data, "FragmentStateActivity", FragmentStateActivity.class);
		addItem(data, "FragmentTabHostActivity", FragmentTabHostActivity.class);
		return data;
	}

	protected void addItem(List<Map<String, Object>> data, String name,
			Class<?> c) { // 封装Intent
		addItem(data, name, new Intent(this, c));
	}

	protected void addItem(List<Map<String, Object>> data, String name,
			Intent intent) { // 向数据源中添加内容的方法
		Map<String, Object> temp = new HashMap<String, Object>(); // 实列化集合对象
		temp.put("title", name); // 向集合中添加元素
		temp.put("intent", intent);
		data.add(temp); // 向数据源中添加元素
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);
		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}
}
