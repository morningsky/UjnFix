package firefly.ujnfix.fixer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.Fixer;

public class FixerMainActivity extends ActionBarActivity implements View.OnClickListener {
    private LinearLayout linear1, linear2, linear3;
    private ImageView iv_kejiedan, iv_bukejie, iv_finish;
    private TextView tv_kejiedan, tv_bukejie, tv_finish;
    private ViewPager   mViewPager;
    private FragmentPagerAdapter mAdapter;//适配器
    private List<Fragment>      mFragments;//adapter的数据源
    private Fixer curFixer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixer_activity_main);
        curFixer = BmobUser.getCurrentUser(this,Fixer.class);
        Log.d("where",curFixer.isOnDuty() + "");
        initView();
        initEvent();
        select(0);
    }


    //初始化视图
    public void initView() {
        linear1 = (LinearLayout) findViewById(R.id.linear_1);
        linear2 = (LinearLayout) findViewById(R.id.linear_2);
        linear3 = (LinearLayout) findViewById(R.id.linear_3);
        iv_kejiedan = (ImageView) findViewById(R.id.iv_kejiedan);
        iv_bukejie = (ImageView) findViewById(R.id.iv_bukejie);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        tv_kejiedan = (TextView) findViewById(R.id.tv_kejiedan);
        tv_bukejie = (TextView) findViewById(R.id.tv_bukejie);
        tv_finish = (TextView) findViewById(R.id.tv_finish);

        //初始化fragment
        mFragments = new ArrayList<>();
        Fragment tab1 = new KejiedanFragment();
        Fragment tab2 = new YijiedanFragment();
        Fragment tab3 = new FinishFragment();
        mFragments.add(tab1);
        mFragments.add(tab2);
        mFragments.add(tab3);

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        //加载viewpager视图
        mViewPager = (ViewPager) findViewById(R.id.fixer_fragment_main);
        mViewPager.setAdapter(mAdapter);
        //设置缓存两个相邻碎片
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //初始化三个linearLayout监听事件
    public void initEvent()
    {
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        linear3.setOnClickListener(this);
    }


    //响应tab标签点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.linear_1:
                select(0);
                break;
            case R.id.linear_2:
                select(1);
                break;
            case R.id.linear_3:
                select(2);
                break;
        }
    }

    //根据选中状态切换颜色
    private void select(int flag)
    {
        restImage();
        switch (flag)
        {
            case 0:
                tv_kejiedan.setTextColor(getResources().getColor(R.color.on));
                iv_kejiedan.setSelected(true);
                break;
            case 1:
                tv_bukejie.setTextColor(getResources().getColor(R.color.on));
                iv_bukejie.setSelected(true);
                break;
            case 2:
                tv_finish.setTextColor(getResources().getColor(R.color.on));
                iv_finish.setSelected(true);
                break;
        }
        mViewPager.setCurrentItem(flag);
    }



    //设置图片为暗色
    private void restImage()
    {
        //设置文字为暗色
        tv_bukejie.setTextColor(getResources().getColor(R.color.off));
        tv_finish.setTextColor(getResources().getColor(R.color.off));
        tv_kejiedan.setTextColor(getResources().getColor(R.color.off));

        iv_kejiedan.setSelected(false);
        iv_finish.setSelected(false);
        iv_bukejie.setSelected(false);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_launcher);
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


        //添加聊天subMenu
        SubMenu subMenu_chat = menu.addSubMenu("");
        MenuItem item_chat = subMenu_chat.getItem();
        item_chat.setIcon(R.drawable.fixer_chat);
        item_chat.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item_chat.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(FixerMainActivity.this, FixerchatActivity.class);
                startActivity(intent);
                return true;
            }
        });


        //添加更多submenu
        final SubMenu subMenu = menu.addSubMenu("");


        if (curFixer.isOnDuty())
        {
            //在线 子菜单
            subMenu.add("切换状态").setIcon(R.drawable.fixer_online)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder changeState = new AlertDialog.Builder(FixerMainActivity.this);
                            changeState.setTitle("您辛苦了，请切换状态进入休息状态吧")
                                    .setIcon(R.drawable.fixer_offline)
                                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            curFixer.setOnDuty(false);
                                            curFixer.update(FixerMainActivity.this,curFixer.getObjectId(),new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    Toast.makeText(FixerMainActivity.this, "状态更新成功", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    Toast.makeText(FixerMainActivity.this,"状态更新失败" + s,Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            subMenu.getItem(0).setIcon(R.drawable.fixer_offline);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            return true;
                        }
                    });
        }else
        {
            //离线子菜单
            subMenu.add("切换状态").setIcon(R.drawable.fixer_offline)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder changeState = new AlertDialog.Builder(FixerMainActivity.this);
                            changeState.setTitle("开始工作吧")
                                    .setIcon(R.drawable.fixer_online)
                                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            curFixer.setOnDuty(false);
                                            curFixer.update(FixerMainActivity.this,curFixer.getObjectId(),new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    Toast.makeText(FixerMainActivity.this, "状态更新成功", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    Toast.makeText(FixerMainActivity.this,"状态更新失败" + s,Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            subMenu.getItem(0).setIcon(R.drawable.fixer_online);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            return true;
                        }
                    });
        }


        subMenu.add("关于").setIcon(R.drawable.about).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        subMenu.add("退出").setIcon(R.drawable.exit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FixerMainActivity.this);
                builder.setTitle("确认").setMessage("确定退出本程序？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", null).show();
                return true;
            }
        });

        MenuItem item = subMenu.getItem();
        item.setIcon(R.drawable.more);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }



}
