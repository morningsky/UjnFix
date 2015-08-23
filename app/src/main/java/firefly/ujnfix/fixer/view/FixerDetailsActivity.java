package firefly.ujnfix.fixer.view;
/*
    可接单的详情页
     */
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FixEvent;
import firefly.ujnfix.user.model.Fixer;
import firefly.ujnfix.user.model.User;

public class FixerDetailsActivity extends ActionBarActivity {
    TextView tv_location, tv_scene, tv_tag, tv_details, tv_username, tv_userphone;//地点，场景，标签，详情，维修员电话
    ImageView fixer_iv_state, iv_imagefile, fix_iv_call;
    Button btn_jiedan,btn_finish;
    String ObID;
    Fixer curFixer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixer_kejiedan_details);
        ObID = getIntent().getStringExtra("ObID");
        init();
        getData();
        listen();
        curFixer = BmobUser.getCurrentUser(FixerDetailsActivity.this, Fixer.class);
    }
public void getData(){
    BmobQuery<FixEvent> myQuery = new BmobQuery<>();
    myQuery.include("submitUser");
    myQuery.getObject(this, ObID, new GetListener<FixEvent>() {
        @Override
        public void onSuccess(FixEvent fixEvent) {
            tv_location.setText(fixEvent.getLocation());
            tv_scene.setText(fixEvent.getFixScene());
            tv_tag.setText(fixEvent.getFixType());
            tv_details.setText(fixEvent.getDetails());

            User submitUser = fixEvent.getSubmitUser();
            tv_username.setText(submitUser.getUsername());
            tv_userphone.setText(submitUser.getMobilePhoneNumber());

            switch (fixEvent.getSolvedState()) {
                case 1:
                    fixer_iv_state.setImageResource(R.drawable.unprogressed);
                    break;
                case 2:
                    fixer_iv_state.setImageResource(R.drawable.progressing);
                    break;
                case 3:
                    fixer_iv_state.setImageResource(R.drawable.solved);
                    break;
            }
        }
        @Override
        public void onFailure(int i, String s) {
            Toast.makeText(getApplicationContext(), "ERROR" + s, Toast.LENGTH_LONG).show();
        }
    });
}
    public void init() {
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_scene = (TextView) findViewById(R.id.tv_scene);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_userphone = (TextView) findViewById(R.id.tv_userphone);
        fixer_iv_state = (ImageView) findViewById(R.id.fixer_iv_state);
        iv_imagefile = (ImageView) findViewById(R.id.iv_imagefile);
        fix_iv_call = (ImageView) findViewById(R.id.fix_iv_call);
        btn_jiedan = (Button) findViewById(R.id.btn_jiedan);
        btn_finish= (Button) findViewById(R.id.btn_finish);
        btn_jiedan.setVisibility(View.INVISIBLE);
        btn_finish.setVisibility(View.INVISIBLE);
        switch (getIntent().getIntExtra("tag", 0)) {
            case 1:
                btn_jiedan.setVisibility(View.VISIBLE);
                break;
            case 2:
                btn_finish.setVisibility(View.VISIBLE);
        }

    }

    public void listen() {
        //拨号的点击事件
        fix_iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) findViewById(R.id.tv_userphone);
                String s = text.getText().toString().trim();
                Intent myIntentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + s));
                startActivity(myIntentDial);
            }
        });
        //接单的点击事件
        btn_jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FixEvent fixEvent= new FixEvent();
                fixEvent.setObjectId(getIntent().getStringExtra("ObID"));
                fixEvent.setSolvedState(2);
                fixEvent.setSolvedFixer(curFixer);
                fixEvent.update(FixerDetailsActivity.this,fixEvent.getObjectId(),new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplication(),"接单成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getApplication(),"接单失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
       //完成修理的点击事件
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FixEvent fixEvent= new FixEvent();
                fixEvent.setObjectId(getIntent().getStringExtra("ObID"));
                fixEvent.setSolvedState(3);
                fixEvent.update(FixerDetailsActivity.this,fixEvent.getObjectId(),new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplication(),"提交成功，恭喜你完成一个任务！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getApplication(),"提交成功，请检查网络再试一次",Toast.LENGTH_SHORT).show();
                    }
                });
                }
        });
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
