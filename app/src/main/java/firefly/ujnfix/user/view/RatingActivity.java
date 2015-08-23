package firefly.ujnfix.user.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FixEvent;

public class RatingActivity extends ActionBarActivity {

    private BmobUser user;
    private FixEvent fixEvent;
    private BmobUser fixer;
    BmobQuery<FixEvent> myQuery;
    String ObID;
    private int level;
    private int speed;
    private int attitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ObID = getIntent().getStringExtra("ObID");
        user = BmobUser.getCurrentUser(this);
        myQuery = new BmobQuery<>();
        myQuery.getObject(this, ObID, new GetListener<FixEvent>() {
            @Override
            public void onSuccess(FixEvent fE) {
                fixEvent=fE;
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        RatingBar rb_level = (RatingBar) findViewById(R.id.rb_level);
        RatingBar rb_speed = (RatingBar) findViewById(R.id.rb_speed);
        RatingBar rb_attitude = (RatingBar) findViewById(R.id.rb_attitude);
        btn_confirm.setOnClickListener(btn_confirmClick);
        rb_level.setOnRatingBarChangeListener(rb_levelChange);
        rb_speed.setOnRatingBarChangeListener(rb_speedChange);
        rb_attitude.setOnRatingBarChangeListener(rb_attitudeChange);
    }

    View.OnClickListener btn_confirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (level == 0 || speed == 0 || attitude == 0) {
                Toast.makeText(RatingActivity.this, "请先评分", Toast.LENGTH_SHORT).show();
            }
            else {
                fixEvent.setVaLevel(level);
                fixEvent.setVaSpeed(speed);
                fixEvent.setVaAttitude(attitude);
                fixEvent.update(RatingActivity.this,ObID, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Toast.makeText(RatingActivity.this,"更新成功：" + fixEvent.getUpdatedAt(),Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(RatingActivity.this,"更新失败：" + msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
            finish();
        }
    };

    //监听三个RatingBar
    RatingBar.OnRatingBarChangeListener rb_levelChange = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            level = (int) rating;
            Toast.makeText(RatingActivity.this, Float.toString(rating), Toast.LENGTH_SHORT).show();
        }
    };
    RatingBar.OnRatingBarChangeListener rb_speedChange = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            speed = (int) rating;
            Toast.makeText(RatingActivity.this, Float.toString(rating), Toast.LENGTH_SHORT).show();
        }
    };
    RatingBar.OnRatingBarChangeListener rb_attitudeChange = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            attitude = (int) rating;
            Toast.makeText(RatingActivity.this, Float.toString(rating), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zh_blue));//设置ActionBar颜色
        actionBar.setDisplayShowHomeEnabled(true);//该三行用于处理ActionBar无法显示logo的问题
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == android.R.id.home) {// android.不能省略
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
