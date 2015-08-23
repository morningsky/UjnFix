package firefly.ujnfix.fixer.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FixEvent;

public class KejiedanFragment extends ListFragment {
    private ArrayList<FixEvent> fixEvents = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BmobQuery<FixEvent> myQuery = new BmobQuery<>();
        myQuery.addWhereEqualTo("solvedState", 1);
        myQuery.findObjects(getActivity(), new FindListener<FixEvent>() {
            @Override
            public void onSuccess(List<FixEvent> list) {
                if (list != null) {
                    fixEvents = (ArrayList<FixEvent>) list;
                    SimpleAdapter simpleAdapter = new SimpleAdapter(
                            getActivity(),
                            getData(),
                            R.layout.listview_layout,
                            new String[]{"ItemTitle", "ItemText"},
                            new int[]{R.id.ItemTitle, R.id.ItemText});
                    setListAdapter(simpleAdapter);
                }
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(), "ERROR" + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<HashMap<String, String>> getData() {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < fixEvents.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemTitle", fixEvents.get(i).getTitle());
                    map.put("ItemText", fixEvents.get(i).getCreatedAt());
            list.add(map);
        }
        return list;
    }


    @Override
    //监听点击Listview的item
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), FixerDetailsActivity.class);
        intent.putExtra("tag", 1);
        intent.putExtra("ObID",fixEvents.get(position).getObjectId());
        startActivity(intent);
    }
}