package data.contentprovider.moretable;
/**
 * contentProvider多表查询需要定义区分的path和code用来区分是查询的那个表
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class TwoTableContentProvider extends ContentProvider {
	private static final String AUTHORITY = "data.contentprovider.moretable.TwoTableContentProvider";  //多表查询权限
	private static final String USER_PATH = "user";
	private static final String STUDENT_PATH = "studnet";
	public static final Uri CONTENT_URI_USER = Uri.parse("content://" + AUTHORITY +"/" +USER_PATH);  //后面加了个path字段作为区分
	public static final Uri CONTENT_URI_STUDENT = Uri.parse("content://" + AUTHORITY +"/"+ STUDENT_PATH);
	
	private static final int USER_CODE = 1;
	private static final int STUDENT_CODE = 2;
	
	public static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);  //区分需要用的一个工具类
	static{   //有多少张表就加入多少个
		uriMatcher.addURI(AUTHORITY, USER_PATH, USER_CODE);  //这里就加入了对应的code和path作为区分了。
		uriMatcher.addURI(AUTHORITY, STUDENT_PATH, STUDENT_CODE);
	}
	
	
	private SQLiteDatabase db;
	@Override
	public boolean onCreate() {
		TwoTableDBHelper helper = new TwoTableDBHelper(getContext());
		db = helper.getReadableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
		int code = uriMatcher.match(uri);   //匹配对应的code;
		switch(code){
		case USER_CODE:   //这里只需要判断code后执行查询对应表的语句就可以了。
			 return db.query("user", projection, selection, selectionArgs, null, null, sortOrder);
		case STUDENT_CODE:
			return db.query("student", projection, selection, selectionArgs, null, null, sortOrder);
		
			default:
				return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int code = uriMatcher.match(uri);
		switch(code){   //执行相应的查询语句
		case USER_CODE:
			long id = db.insert("user", null, values);
			return Uri.withAppendedPath(uri, String.valueOf(id));
		case STUDENT_CODE:
			id = db.insert("student", null, values);
			return Uri.withAppendedPath(uri, String.valueOf(id));
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
