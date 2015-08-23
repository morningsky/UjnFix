package firefly.ujnfix.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.MyInstallation;
import firefly.ujnfix.user.model.User;

/**
 * Created by 清晨 on 2015/7/26.
 */
public class RegisterActivity extends ActionBarActivity {
    private User user;
    private EditText edt_username;
    private EditText edt_password;
    private EditText edt_password_sure;
    private EditText edt_smsCode;
    private Button   btn_sendSms;//重新发送验证码
    private Button   btn_submit;//确认信息，启动注册
    private String phoneNumber;
    private String password;//注册密码
    private String username;//注册姓名
    private String password_sure;//第二次输入密码
    private String smsCode;
    private MyInstallation myInstallation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();//注册各个控件
        user= new User();
        phoneNumber = getIntent().getStringExtra(RegisterRequestPhoneActivity.PHONE_NUMBER);
        user.setMobilePhoneNumber(phoneNumber);
        MyInstallation.getCurrentInstallation(this).save();
        myInstallation = new MyInstallation(this);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = edt_password.getText().toString().trim();
                username = edt_username.getText().toString().trim();
                password_sure = edt_password_sure.getText().toString();
                smsCode = edt_smsCode.getText().toString().trim();

                if (password_sure.equals(password))
                {
                    BmobSMS.verifySmsCode(RegisterActivity.this,phoneNumber,smsCode, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                register();
                            }else
                            {
                                Toast.makeText(RegisterActivity.this,"验证码匹配错误，请重新确认验证码"+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else
                {
                    Toast.makeText(RegisterActivity.this,"前后密码不一致，请重新确认密码",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(RegisterActivity.this, phoneNumber, "注册账号", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "验证码已成功发送，请注意查收", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "验证码发送失败，请检查手机号是否合法", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void init()
    {
        edt_smsCode = (EditText) findViewById(R.id.edt_smsCode);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_password_sure = (EditText) findViewById(R.id.edt_password_sure);
        btn_sendSms = (Button) findViewById(R.id.btn_sendSms);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    //注册账号
    public void register()
    {
        user.setFixer(false);//只有普通用户可以注册，维修员不可注册
        user.setUsername(username);
        user.setPassword(password);


        //方便发推送
        myInstallation.setUsername(username);
        myInstallation.setFixer(false);

        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "恭喜,注册成功！", Toast.LENGTH_SHORT).show();
                //返回登录页
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegisterActivity.this, "不好意思，注册失败，您的手机号或者姓名已被注册"+s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
