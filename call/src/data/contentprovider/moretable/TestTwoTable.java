package data.contentprovider.moretable;

import mylog.Mylog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestTwoTable extends AndroidTestCase {
	ContentResolver contentResolver;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		contentResolver = getContext().getContentResolver();
	}
	
	public void testInsertUser(){
		ContentValues values = new ContentValues();
		values.put("id", 001);
		values.put("username", "小红");
		values.put("password", "123");
		contentResolver.insert(TwoTableContentProvider.CONTENT_URI_USER, values);
	}
	
	
	public void testInsertStudent(){
		ContentValues values = new ContentValues();
		values.put("id", 1111);
		values.put("name", "小明");
		contentResolver.insert(TwoTableContentProvider.CONTENT_URI_STUDENT, values);
	}
	
	
	public void testQueryUser(){
		Uri uri = TwoTableContentProvider.CONTENT_URI_USER;  // 得到对应Uri
		Cursor c = contentResolver.query(uri, null, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
				String name = c
						.getString(c
								.getColumnIndex("username"));
				Mylog.v(name);
			}
		}
	}
	
	public void testQueryStudent(){
		Uri uri = TwoTableContentProvider.CONTENT_URI_STUDENT;
		Cursor c = contentResolver.query(uri, null, null, null, null);
		if(c != null){
			while(c.moveToNext()){
				String name = c.getString(c.getColumnIndex("name"));
				int id = c.getInt(c.getColumnIndex("id"));
				Mylog.v("id:  "+ id + "         name:   "+ name);
			}
		}
	}
	
}
