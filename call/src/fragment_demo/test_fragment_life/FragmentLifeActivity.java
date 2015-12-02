package fragment_demo.test_fragment_life;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.call.R;

import fragment_demo.fragment.FragmentTwo;
import fragment_demo.fragment.MyFragment;

public class FragmentLifeActivity extends Activity implements View.OnClickListener{
    private Button mAddBtn,mRemoveBtn,mReplaceBtn;
    private FragmentManager manager;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_life_layout);
        mAddBtn = (Button) findViewById(R.id.fragment_add_btn);
        mAddBtn.setOnClickListener(this);
        mRemoveBtn = (Button) findViewById(R.id.fragment_remove_btn);
        mRemoveBtn.setOnClickListener(this);
        mReplaceBtn = (Button) findViewById(R.id.fragment_replace_btn);
        mReplaceBtn.setOnClickListener(this);
        fragment = MyFragment.newIntance();
        manager = getFragmentManager();
        manager.beginTransaction().add(R.id.fragment_life_framelayout,fragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_add_btn:
                manager.beginTransaction().add(R.id.fragment_life_framelayout,fragment).commit();
                break;
            case R.id.fragment_remove_btn:
                manager.beginTransaction().remove(fragment).commit();
                break;
            case R.id.fragment_replace_btn:
                manager.beginTransaction().replace(R.id.fragment_life_framelayout, FragmentTwo.newInstance()).commit();
                break;
        }
    }
}
