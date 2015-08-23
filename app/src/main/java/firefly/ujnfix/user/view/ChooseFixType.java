package firefly.ujnfix.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import firefly.ujnfix.R;

/**
 * Created by 清晨 on 2015/8/1.
 */
public class ChooseFixType extends ActionBarActivity{
    private static int fixType_select;  //所选择的修理类型
    private List<Map<String,Object>> data_list;
    private String[] tags;
    private SimpleAdapter mAdapter;
    private GridView tag_gridview;
    private String select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosefixtype);

        //标记传入的场景
        fixType_select = getIntent().getIntExtra("flag_fixScene",0);
        Log.d("sky","" +fixType_select);

        tag_gridview = (GridView) findViewById(R.id.tag_gridView);
        data_list = new ArrayList<Map<String, Object>>();

        //使data_list赋值
        getData(fixType_select);

        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        mAdapter = new SimpleAdapter(this,data_list,R.layout.list_item_fixtype,new String[]{"text"},new int[]{R.id.tv_tag});

        //配置适配器
        tag_gridview.setAdapter(mAdapter);

        tag_gridview.setOnItemClickListener(new ItemClickListener());

//        btn_finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent();
//                i.putExtra("select",fixType_select);
//                setResult(1,i);
//                finish();
//            }
//        });
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String,Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
            select = item.get("text").toString();
            Toast.makeText(ChooseFixType.this,""+item.get("text"),Toast.LENGTH_SHORT).show();
        }
    }


    public void getData(int flag)
    {
        //"宿舍", "教室", "食堂", "图书馆", "教师公寓", "露天设备"
        switch (flag)
        {
            case 0:
                tags = this.getResources().getStringArray(R.array.dormitory);
                break;
            case 1:
                tags = this.getResources().getStringArray(R.array.teaching_building);
                break;
            case 2:
                tags = this.getResources().getStringArray(R.array.canteen);
                break;
            case 3:
                tags = this.getResources().getStringArray(R.array.library);
                break;
            case 4:
                tags = this.getResources().getStringArray(R.array.apartment);
                break;
            case 5:
                tags = this.getResources().getStringArray(R.array.outdoor);
        }

        //生成动态集合 并转入数组数据
        for (int i = 0;i<tags.length;i++)
        {
            Log.d("sky","这里是for循环:" + i + tags[i]);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("text",tags[i]);
            data_list.add(map);
        }

    }


//愚蠢的代码
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            case R.id.tag_11:
//                fixType_select = (String) tag_11.getText();
//                tag_11.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_12:
//                fixType_select = tag_12.getText().toString();
//                tag_12.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_13:
//                fixType_select = tag_13.getText().toString();
//                tag_13.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_21:
//                fixType_select = tag_21.getText().toString();
//                tag_21.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_22:
//                fixType_select = tag_22.getText().toString();
//                tag_22.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_23:
//                fixType_select = tag_23.getText().toString();
//                tag_23.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_31:
//                fixType_select = tag_31.getText().toString();
//                tag_31.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_32:
//                tag_32.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_33:
//                fixType_select = tag_33.getText().toString();
//                tag_33.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_41:
//                fixType_select = tag_41.getText().toString();
//                tag_41.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_42:
//                fixType_select = tag_42.getText().toString();
//                tag_42.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_43:
//                fixType_select = tag_43.getText().toString();
//                tag_43.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_51:
//                fixType_select = tag_51.getText().toString();
//                tag_51.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_52:
//                fixType_select = tag_52.getText().toString();
//                tag_52.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//            case R.id.tag_53:
//                fixType_select = tag_53.getText().toString();
//                tag_53.setTextColor(getResources().getColor(R.color.tag_pressed));
//                break;
//        }
//    }

    //以下是菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("选择维修类型");
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zh_blue));
        //返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            //注意是android.R
            case android.R.id.home:
                Intent i = new Intent();
                i.putExtra("select",select);
                setResult(1,i);
                finish();
        }
        return true;

    }

}
