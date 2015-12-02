package fragment_demo.Test_fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.call.R;

import fragment_demo.fragment.FragmentTwo;
import fragment_demo.fragment.MyFragment;

public class TestFragmentActivity extends Activity implements View.OnClickListener{
    private Button mBtnOne,mBtnTwo;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_layout);
        mBtnOne = (Button) findViewById(R.id.fragment_btn_one);
        mBtnOne.setOnClickListener(this);
        mBtnTwo = (Button) findViewById(R.id.fragment_btn_two);
        mBtnTwo.setOnClickListener(this);
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();   //v4包要统一
        fragmentTransaction.add(R.id.frament_content,new FragmentTwo()).commit();//提交事务


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_btn_one:
                fragmentManager.beginTransaction().replace(R.id.frament_content, MyFragment.newIntance()).commit();
            break;
            case R.id.fragment_btn_two:
                fragmentManager.beginTransaction().replace(R.id.frament_content,FragmentTwo.newInstance()).commit();
                break;
        }
    }
}
