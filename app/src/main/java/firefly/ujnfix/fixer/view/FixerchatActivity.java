package firefly.ujnfix.fixer.view;
/*
修理员聊天
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.Fixer;

public class FixerchatActivity extends ActionBarActivity {
    private ArrayList<Fixer> Fixers = new ArrayList<>();
    private ListView listView;
    ImageView fixerchat_listview_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixchat_listview);
        listView = (ListView) findViewById(android.R.id.list);
        fixerchat_listview_state = (ImageView) findViewById(R.id.fixerchat_listview_state);
        //找出是维修员的账号
        BmobQuery<Fixer> myQuery = new BmobQuery<>();
        myQuery.addWhereEqualTo("isFixer", true);
        myQuery.findObjects(this, new FindListener<Fixer>() {
            public void onSuccess(List<Fixer> list) {
                Fixers = (ArrayList<Fixer>) list;
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        FixerchatActivity.this,
                        getData(),
                        R.layout.fixerchat_listview,
                        new String[]{"name", "state"},
                        new int[]{R.id.fixerchat_listview_name, R.id.fixerchat_listview_state})
                        //设置Listview的内部的点击事件
                {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final View view = super.getView(position, convertView, parent);
                        ImageView fixerchat_listview_chat = (ImageView) view.findViewById(R.id.fixerchat_listview_chat);
                        ImageView fixerchat_listview_phone = (ImageView) view.findViewById(R.id.fixerchat_listview_phone);
                        //设置打电话的点击事件
                        fixerchat_listview_phone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntentDial = new Intent(Intent.ACTION_DIAL);
                                startActivity(myIntentDial);
                            }
                        });
                        //设置发短信的点击事件
                        fixerchat_listview_chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //监听fixerchat
                            public void onClick(View v) {
                                Intent intent = new Intent(FixerchatActivity.this, FixerChatDialog.class);
                                startActivity(intent);
                            }
                        });
                        return view;
                    }
                };
                listView.setAdapter(simpleAdapter);
            }

            public void onError(int i, String s) {
            }
        });
    }

    private List<HashMap<String, Object>> getData() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < Fixers.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            switch (i) {
                case 0:
                    map.put("name", Fixers.get(i).getUsername());
                    if (Fixers.get(i).isOnDuty() == true)
                        map.put("state", R.drawable.fixer_online);
                    else
                        map.put("state", R.drawable.fixer_offline);
                case 1:
                    map.put("name", Fixers.get(i).getUsername());
                    if (Fixers.get(i).isOnDuty() == true)
                        map.put("state", R.drawable.fixer_online);
                    else
                        map.put("state", R.drawable.fixer_offline);
                case 2:
                    map.put("name", Fixers.get(i).getUsername());
                    if (Fixers.get(i).isOnDuty() == true)
                        map.put("state", R.drawable.fixer_online);
                    else
                        map.put("state", R.drawable.fixer_offline);
                case 3:
                    map.put("name", Fixers.get(i).getUsername());
                    if (Fixers.get(i).isOnDuty() == true)
                        map.put("state", R.drawable.fixer_online);
                    else
                        map.put("state", R.drawable.fixer_offline);
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
