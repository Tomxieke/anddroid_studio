package data.contentprovider.moretable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*contentprivder 多表查询  首先建立两个表*/
public class TwoTableDBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "two_table"; //数据库名
	private static final int VERSION = 1;  // 版本号

	public TwoTableDBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		
	}

	@Override
	/*创建两个表*/
	public void onCreate(SQLiteDatabase db) {
		String sql_user = "create table user (id int,username varchar(20),password varchar(50))";
		String sql_student = "create table student(id int,name varchar(20))";
		db.execSQL(sql_user);
		db.execSQL(sql_student);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
