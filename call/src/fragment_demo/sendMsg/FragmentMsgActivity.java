package fragment_demo.sendMsg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.call.R;


public class FragmentMsgActivity extends Activity implements SendMsgToActivityFragment.OnFragmentInteractionListener {

    private EditText mEditText;
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_msg_layout);
        mEditText = (EditText) findViewById(R.id.fragment_editxt);
        mSendBtn = (Button) findViewById(R.id.fragment_send_msg_btn);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditText.getText().toString();
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_msg_contnent_framentLayotu,SendMsgToActivityFragment.newInstance(msg)).commit();

            }
        });
    }


    @Override
    public void sendMsg(String msg) {
        mEditText.setText(msg);
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().replace(R.id.fragment_msg_contnent_framentLayotu,MsgFragment.newInstance(msg)).commit();
    }
}
