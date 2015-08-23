package firefly.ujnfix.user.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FixEvent;
import firefly.ujnfix.user.model.Fixer;

public class DetailActivity extends ActionBarActivity {

    private FixEvent fixEvent;
    private Fixer fixer;
    BmobQuery<FixEvent> myQuery;
    String ObID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ObID = getIntent().getStringExtra("ObID");

        myQuery = new BmobQuery<FixEvent>();
        myQuery.include("solvedFixer");
        Log.d("where", ObID);
        myQuery.getObject(this, ObID, new GetListener<FixEvent>() {
            @Override
            public void onSuccess(FixEvent fE) {
                fixEvent = fE;
                Log.d("where", fixEvent.toString());
                fixer=fE.getSolvedFixer();
//                    Log.d("where",fixer.getUsername());
                iniDetails();
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });


    }

    private void iniDetails() {
        TextView tv_location = (TextView) findViewById(R.id.tv_location);
        TextView tv_scene = (TextView) findViewById(R.id.tv_scene);
        TextView tv_fixType = (TextView) findViewById(R.id.tv_fixType);
        TextView tv_detailsInputted = (TextView) findViewById(R.id.tv_detailsInputted);
        TextView tv_fixerName = (TextView) findViewById(R.id.tv_fixerName);
        TextView tv_fixerTel = (TextView) findViewById(R.id.tv_fixerTel);
        ImageView iv_solvedState = (ImageView) findViewById(R.id.iv_solvedState);
        LinearLayout ll_eventPicked = (LinearLayout) findViewById(R.id.ll_eventPicked);

        tv_location.setText(fixEvent.getLocation());
        tv_scene.setText(fixEvent.getFixScene());
        tv_fixType.setText(fixEvent.getFixType());
        tv_detailsInputted.setText(fixEvent.getDetails());
        if (fixEvent.getSolvedState()!=1)
        {
            tv_fixerName.setText(fixer.getUsername());
            tv_fixerTel.setText(fixer.getMobilePhoneNumber());
        }

        Button btn_rating = (Button) findViewById(R.id.btn_rating);
        Button btn_complain = (Button) findViewById(R.id.btn_complain);
        btn_rating.setOnClickListener(btn_ratingClick);
        btn_complain.setOnClickListener(btn_complainClick);


        //加载图片
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        if(fixEvent.getImageFile1()!=null){
            fixEvent.getImageFile1().loadImage(this, (ImageView) findViewById(R.id.iv_fist), width, height/3);
            if (fixEvent.getImageFile2()!=null){
                fixEvent.getImageFile2().loadImage(this, (ImageView) findViewById(R.id.iv_second), width, height/3);
                if (fixEvent.getImageFile3()!=null){
                    fixEvent.getImageFile3().loadImage(this, (ImageView) findViewById(R.id.iv_third), width, height/3);
                }
            }
        }

        switch (fixEvent.getSolvedState()) {
            case 1:
                iv_solvedState.setImageResource(R.drawable.unprogressed);
                ll_eventPicked.setVisibility(View.GONE);
                btn_rating.setVisibility(View.GONE);
                break;
            case 2:
                iv_solvedState.setImageResource(R.drawable.progressing);
                ll_eventPicked.setVisibility(View.GONE);
                btn_rating.setVisibility(View.GONE);
                break;
            case 3:
                iv_solvedState.setImageResource(R.drawable.solved);
                break;
            default:
                Toast.makeText(this, "Get solvedState failed" + fixEvent.getSolvedState(), Toast.LENGTH_SHORT).show();
        }


    }

    View.OnClickListener btn_ratingClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DetailActivity.this, RatingActivity.class);
            intent.putExtra("ObID", ObID);
            startActivityForResult(intent, 1);
        }
    };
    View.OnClickListener btn_complainClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //加载对话框视图
            LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
            View view = inflater.inflate(R.layout.dialog_complaint, null);
            ImageView iv_complaintEmail = (ImageView) view.findViewById(R.id.iv_complaintEmail);
            ImageView iv_complaintCall = (ImageView) view.findViewById(R.id.iv_complaintCall);
            iv_complaintEmail.setOnClickListener(iv_complaintEmailClick);
            iv_complaintCall.setOnClickListener(iv_complaintCallClick);

            AlertDialog.Builder complain = new AlertDialog.Builder(DetailActivity.this);
            complain.setTitle("投诉")
                    .setView(view)
                    .setNegativeButton("返回", null)
                    .show();
        }
    };

    View.OnClickListener iv_complaintEmailClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View view = DetailActivity.this.getLayoutInflater().inflate(R.layout.dialog_complaint, null);
            TextView tv_complaintEmail = (TextView) view.findViewById(R.id.tv_complaintEmail);
            Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + tv_complaintEmail.getText().toString()));
            startActivity(intent);
        }
    };

    View.OnClickListener iv_complaintCallClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View view = DetailActivity.this.getLayoutInflater().inflate(R.layout.dialog_complaint, null);
            TextView tv_complaintCall = (TextView) view.findViewById(R.id.tv_complaintCall);
            Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_complaintCall.getText().toString()));
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {// android.不能省略
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
