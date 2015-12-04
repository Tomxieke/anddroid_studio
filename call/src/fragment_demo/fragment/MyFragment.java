package fragment_demo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.call.R;

public class MyFragment extends Fragment {

	/*返回Fragment实例*/
	public static MyFragment newIntance(){
		MyFragment myFragment = new MyFragment();
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.myfragment_layout, container, false);
	}
}
