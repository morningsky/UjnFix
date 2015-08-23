package firefly.ujnfix.user.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FeedBack;
import firefly.ujnfix.user.model.User;

public class FeedBackActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        Button btn_feedback =(Button) findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(btn_feedbackClick);
    }

    View.OnClickListener btn_feedbackClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //加载对话框视图
            LayoutInflater inflater = LayoutInflater.from(FeedBackActivity.this);
            View view = inflater.inflate(R.layout.dialog_feedback, null);
            final TextView et_title =(TextView) view.findViewById(R.id.et_title);
            final TextView et_details =(TextView) view.findViewById(R.id.et_details);

            AlertDialog.Builder feedback = new AlertDialog.Builder(FeedBackActivity.this);
            feedback.setTitle("反馈")
                    .setView(view)
                    .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FeedBack feedBack=new FeedBack();
                            feedBack.setSubmitUser(BmobUser.getCurrentUser(FeedBackActivity.this,User.class));
                            feedBack.setTitle(et_title.getText().toString().trim());
                            feedBack.setDetails(et_details.getText().toString().trim());
                            feedBack.save(FeedBackActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(FeedBackActivity.this,"反馈提交成功，我们将及时处理您的信息",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(FeedBackActivity.this,"反馈提交失败"+s,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("返回", null)
                    .show();
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

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {// android.不能省略
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
