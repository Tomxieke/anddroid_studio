package fragment_demo.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private String[] data = new String[]{"新闻", "电影", "旅游"};

    /*返回Fragment实例*/
    public static FragmentTwo newInstance(){
        FragmentTwo fragmentTwo = new FragmentTwo();
        return fragmentTwo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment_two_layout, container, false);
        mListView = (ListView) v.findViewById(R.id.fragment_listview);
        FragmentListAdapter adapter = new FragmentListAdapter(getActivity());
        adapter.setDataStr(data);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String str = (String) adapterView.getAdapter().getItem(i);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }


    public class FragmentListAdapter extends BaseAdapter {
        private String[] dataStr = new String[]{};
        private LayoutInflater inflater;




        public FragmentListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public void setDataStr(String[] data) {
            this.dataStr = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return dataStr.length;
        }

        @Override
        public Object getItem(int i) {
            return dataStr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }
            TextView txt = (TextView) view;
            txt.setText((CharSequence) getItem(i));
            return view;
        }

    }

}
