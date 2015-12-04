package fragment_demo.fragment;

import android.app.Activity;
import android.os.Bundle;

import com.example.call.R;

public class FragmentDomeActivity extends Activity implements FragmentTwo.OnCallBackInter {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_activtiy_layout);
	}

	@Override
	public void OngetMsg(String msg) {

	}
}
