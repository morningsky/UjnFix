package firefly.ujnfix.user.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.User;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private LinearLayout ll_fix, ll_query, ll_tel;//绑定三个TABEL的LinearLayout控件，用于绑定监听事件
    private ImageView iv_fix, iv_query, iv_tel;//绑定三个ImageView，用于切换三个TABEL的图标颜色
    private TextView tv_fix, tv_query, tv_tel;//绑定三个TextView，用于切换三个TABEL的文字颜色
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;//适配器
    private List<Fragment> mFragments;//adapter的数据源
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = BmobUser.getCurrentUser(this,User.class);
        initView();
        initEvent();
        setSelect(0);
    }

    public void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.fragment_main);

        ll_fix = (LinearLayout) findViewById(R.id.ll_fix);        //初始化三个LinearLayout
        ll_query = (LinearLayout) findViewById(R.id.ll_query);
        ll_tel = (LinearLayout) findViewById(R.id.ll_tel);

        iv_fix = (ImageView) findViewById(R.id.iv_fix);           //初始化三个ImageView
        iv_query = (ImageView) findViewById(R.id.iv_query);
        iv_tel = (ImageView) findViewById(R.id.iv_tel);

        tv_fix = (TextView) findViewById(R.id.tv_fix);            //初始化三个TextView
        tv_query = (TextView) findViewById(R.id.tv_query);
        tv_tel = (TextView) findViewById(R.id.tv_tel);

        //初始化fragment
        mFragments = new ArrayList<Fragment>();
        Fragment tab01 = new FixFragment();
        Fragment tab02 = new QueryFragment();
        Fragment tab03 = new TelFragment();
        mFragments.add(tab01);
        mFragments.add(tab02);
        mFragments.add(tab03);

        //初始化adapter
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

        };

        //初始化mViewPager的是适配器
        mViewPager.setAdapter(mAdapter);
        //设置缓存两个相邻碎片
        mViewPager.setOffscreenPageLimit(2);
        //监听mViewPager的变化
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //int currentItem = mViewPager.getCurrentItem();
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //为三个LinearLayout绑定监听事件
    public void initEvent()
    {
        ll_fix.setOnClickListener(this);
        ll_query.setOnClickListener(this);
        ll_tel.setOnClickListener(this);
    }

    //相应linearLayout的点击响应事件
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ll_fix:
                setSelect(0);
                break;
            case R.id.ll_query:
                setSelect(1);
                break;
            case R.id.ll_tel:
                setSelect(2);
                break;
        }
    }


    //设置图片为亮色
    private void setSelect(int flag)
    {
        resetImage();
        switch (flag)
        {
            case 0:
                iv_fix.setImageResource(R.drawable.fix_in);
                tv_fix.setTextColor(getResources().getColor(R.color.zh_blue));
                break;
            case 1:
                iv_query.setImageResource(R.drawable.query_in);
                tv_query.setTextColor(getResources().getColor(R.color.zh_blue));
                break;
            case 2:
                iv_tel.setImageResource(R.drawable.tel_in);
                tv_tel.setTextColor(getResources().getColor(R.color.zh_blue));
                break;
        }
        //切换fragment
        mViewPager.setCurrentItem(flag);
    }


    //设置图片为灰色
    private void resetImage()
    {
        iv_fix.setImageResource(R.drawable.fix_pre);
        iv_query.setImageResource(R.drawable.query_pre);
        iv_tel.setImageResource(R.drawable.tel_pre);

        //设置文字为黑色
        tv_fix.setTextColor(Color.rgb(0,0,0));
        tv_query.setTextColor(Color.rgb(0,0,0));
        tv_tel.setTextColor(Color.rgb(0,0,0));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zh_blue));//设置ActionBar颜色
        actionBar.setDisplayShowHomeEnabled(true);//该三行用于处理ActionBar无法显示logo的问题
        actionBar.setLogo(R.drawable.fix_pre);
        actionBar.setDisplayUseLogoEnabled(true);

        //刷新按钮
        SubMenu subMenu_refresh = menu.addSubMenu("");
        MenuItem menuItem_refresh = subMenu_refresh.getItem();
        menuItem_refresh.setIcon(R.drawable.refresh);
        menuItem_refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mFragments.get(mViewPager.getCurrentItem()).onCreate(Bundle.EMPTY);
                return false;
            }
        });

        SubMenu subMenu = menu.addSubMenu("");  //添加子菜单
        subMenu.setIcon(R.drawable.more);
        subMenu.add("修改资料").setIcon(R.drawable.userinfo_edt).setOnMenuItemClickListener(userinfo_edtClick);//添加三个子item并绑定监听事件
        subMenu.add("关于").setIcon(R.drawable.about).setOnMenuItemClickListener(aboutClick);
        subMenu.add("退出").setIcon(R.drawable.exit).setOnMenuItemClickListener(exitClick);
        MenuItem menuItem = subMenu.getItem(); //实例化subMenu为菜单item
        menuItem.setIcon(R.drawable.more);  //设置subMenu的图标
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);//将subMenu放在与menu同级别的位置
        return super.onCreateOptionsMenu(menu);
    }
    //三个item的监听事件(未完成)
    MenuItem.OnMenuItemClickListener userinfo_edtClick = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    };

    MenuItem.OnMenuItemClickListener aboutClick = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent=new Intent(MainActivity.this,FeedBackActivity.class);
            startActivity(intent);
            return false;
        }
    };
    MenuItem.OnMenuItemClickListener exitClick = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("提醒")
                        .setMessage("是否退出程序")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //设置离线状态
                                user.setOnline(false);
                                user.update(MainActivity.this);
                                BmobUser.logOut(MainActivity.this);   //清除缓存用户对象
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create(); // 创建对话框
                alertDialog.show(); // 显示对话框
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
                return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        user.setOnline(false);
        user.update(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("sky","退出应用");
        //设置离线状态
        user.setOnline(false);
        user.update(this);
        BmobUser.logOut(this);   //清除缓存用户对象
    }
}

