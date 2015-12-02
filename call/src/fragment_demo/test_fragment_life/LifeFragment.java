package fragment_demo.test_fragment_life;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.call.R;

import mylog.Mylog;

public class LifeFragment extends Fragment {

    public static LifeFragment newInstance(String param1, String param2) {
        LifeFragment fragment = new LifeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("test", "onAttach:00000000000000000000000 ");
        Mylog.d("onAttach-------------------------");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mylog.e("onAttach-------------------------");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test", "onCreateView:00000000000000000000000 ");
        Mylog.i("onCreateView--------------------------");
        return inflater.inflate(R.layout.fragment_life, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Mylog.v("onActivityCreated----------------");
    }

    @Override
    public void onStart() {
        super.onStart();
        Mylog.v("onStart-------------------");
    }

    @Override
    public void onResume() {
        super.onResume();
        Mylog.i("onResume---------------");
    }

    @Override
    public void onPause() {
        super.onPause();
        Mylog.d("onPause------------------");
    }

    @Override
    public void onStop() {
        super.onStop();
        Mylog.e("onStop---------------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Mylog.d("onDestroyView------------------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Mylog.i("onDestroyView-------------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Mylog.d("onDetach------------------------");
    }


}
