package firefly.ujnfix.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import firefly.ujnfix.R;

/**
 * Created by 清晨 on 2015/7/27.
 */
public class RegisterRequestPhoneActivity extends ActionBarActivity {
    public static final String  PHONE_NUMBER = "user_phone_number";
    private EditText edt_phoneNumber;
    private Button btn_next;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_request_phone);
        edt_phoneNumber = (EditText) findViewById(R.id.edt_phoneNumber);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = edt_phoneNumber.getText().toString().trim();
                BmobSMS.requestSMSCode(RegisterRequestPhoneActivity.this,phoneNumber,"注册账号", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null)
                        {
                            Toast.makeText(RegisterRequestPhoneActivity.this,"短信发送成功，请检查手机号是否合法",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterRequestPhoneActivity.this,RegisterActivity.class);
                            //Log.d("ee",i.toString());
                            i.putExtra(PHONE_NUMBER,phoneNumber);
                            startActivity(i);
                        } else
                        {
                            Log.d("ee",e.toString());
                            Toast.makeText(RegisterRequestPhoneActivity.this,"短信发送失败，请检查手机号是否合法",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

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

        if (item.getItemId() == android.R.id.home) {// android.不能省略
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
