package fragment_demo.fragment;

import android.app.Activity;
import android.os.Bundle;

import com.example.call.R;

import mylog.Mylog;

public class FragmentDomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Mylog.d("FragmentDomeActivity=-------------");
		setContentView(R.layout.fragment_activtiy_layout);
	}
}
