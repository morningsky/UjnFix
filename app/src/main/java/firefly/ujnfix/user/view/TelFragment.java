package firefly.ujnfix.user.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import firefly.ujnfix.R;

/**
 * Created by Phineas on 2015/7/27.
 */
public class TelFragment extends ListFragment {
    private String[] name;
    private String[] tel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getResources().getStringArray(R.array.name);
        tel = getResources().getStringArray(R.array.tel);
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                getData(),
                R.layout.activity_list_tel,
                new String[]{"tv_name", "tv_tel", "iv_call"},
                new int[]{R.id.tv_name, R.id.tv_tel, R.id.iv_call}) {
            @Override                                                                   //重写getview为ImageView绑定监听事件
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_call);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel[position]));
                        startActivity(intent);
                    }
                });
                return view;
            }
        };
        setListAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_name", name[i]);
            map.put("tv_tel", tel[i]);
            map.put("iv_call", R.drawable.call);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity(), tel[position], Toast.LENGTH_SHORT).show();
    }
}
