package firefly.ujnfix.user.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FixEvent;

/**
 * Created by Phineas on 2015/7/27.
 */
public class QueryFragment extends ListFragment {
    public SimpleAdapter adapter;
    private BmobUser user;
    private ArrayList<FixEvent> fixEvents = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(getActivity());
        BmobQuery<FixEvent> myQuery = new BmobQuery<>();
        myQuery.addWhereEqualTo("submitUser", user.getObjectId());
        myQuery.findObjects(getActivity(), new FindListener<FixEvent>() {
            @Override
            public void onSuccess(List<FixEvent> list) {

                fixEvents = (ArrayList<FixEvent>) list;
                        adapter = new SimpleAdapter(
                        getActivity(),
                        getData(),
                        R.layout.activity_list_query,
                        new String[]{"tv_title", "tv_time", "iv_solvedState"},
                        new int[]{R.id.tv_title, R.id.tv_time, R.id.iv_solvedState});
                setListAdapter(adapter);
                }


            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(), "ERROR" + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < fixEvents.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_title", fixEvents.get(i).getTitle());
            switch (fixEvents.get(i).getSolvedState()) {
                case 1:
                    map.put("iv_solvedState", R.drawable.unprogressed);
                    map.put("tv_time", fixEvents.get(i).getCreatedAt());
                    break;
                case 2:
                    map.put("iv_solvedState", R.drawable.progressing);
                    map.put("tv_time", fixEvents.get(i).getCreatedAt());
                    break;
                case 3:
                    map.put("iv_solvedState", R.drawable.solved);
                    map.put("tv_time", fixEvents.get(i).getUpdatedAt());
                    break;
                default:
                    map.put("tv_time", "error");
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String ObID = fixEvents.get(position).getObjectId();
        Log.d("where", ObID);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("ObID",ObID);
        startActivity(intent);
    }
}
