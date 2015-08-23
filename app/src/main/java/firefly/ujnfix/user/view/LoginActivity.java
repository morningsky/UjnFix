package firefly.ujnfix.user.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import firefly.ujnfix.R;
import firefly.ujnfix.fixer.view.FixerMainActivity;
import firefly.ujnfix.user.model.MyInstallation;
import firefly.ujnfix.user.model.User;

/**
 * Created by 清晨 on 2015/7/26.
 */
public class LoginActivity extends Activity {
    private EditText edt_username;
    private EditText edt_password;
    private Button btn_login;
    private TextView btn_register;
    private TextView tv_forgetPassword;
    private CheckBox  cb_savePassword;
    private SharedPreferences sp_userInfo;
    private String username;
    private String password;
    private MyInstallation myInstallation;
    private User curUser;

    public void init()
    {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (TextView) findViewById(R.id.btn_register);

        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
        cb_savePassword = (CheckBox) findViewById(R.id.cb_savePassword);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "d16784e1a735567bf7da8758bea9ca4a");
        //BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this,"d16784e1a735567bf7da8758bea9ca4a");
        setContentView(R.layout.activity_login);
        sp_userInfo = this.getSharedPreferences("userInfo",CONTEXT_IGNORE_SECURITY);//存储用户密码 账号名
        init();
        //注册
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this,RegisterRequestPhoneActivity.class);
                startActivity(i);
            }
        });

        //登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //忘记密码
        tv_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LoginActivity.this,RePasswordActivity.class);
                startActivity(j);
            }
        });

        cb_savePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            //监听checkbox是否被选中
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_savePassword.isChecked())
                {
                    sp_userInfo.edit().putBoolean("IS_CHECKED",true).commit();
                }else
                {
                    sp_userInfo.edit().putBoolean("IS_CHECKED",false).commit();
                }
            }
        });

        if (sp_userInfo.getBoolean("IS_CHECKED",false))
        {
            //设置默认为记住密码状态
            cb_savePassword.setChecked(true);
            edt_username.setText(sp_userInfo.getString("USER_NAME",""));
            edt_password.setText(sp_userInfo.getString("USER_PASSWORD",""));
        }
    }

    public void login()
    {
        User user = new User();
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();
        forceOffline(username);  //若有人登录 则强制其下线

        user.setUsername(username);
        user.setPassword(password);
        user.login(this,new SaveListener() {
            @Override
            public void onSuccess() {
                if (cb_savePassword.isChecked())
                {
                    //用户名与密码本地化
                    SharedPreferences.Editor editor = sp_userInfo.edit();
                    editor.putString("USER_NAME",username);
                    editor.putString("USER_PASSWORD",password);
                    editor.commit();
                }
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                curUser = BmobUser.getCurrentUser(LoginActivity.this,User.class);
                curUser.setOnline(true);
                //将状态改为在线
                curUser.update(LoginActivity.this);
                //根据是否是维修员判断进入不同的界面
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                Intent j = new Intent(LoginActivity.this, FixerMainActivity.class);

                if (curUser.isFixer()){
                    //为了方便推送
                    myInstallation = new MyInstallation(LoginActivity.this);
                    String s = BmobInstallation.getCurrentInstallation(LoginActivity.this).getInstallationId();
                    myInstallation = new MyInstallation(LoginActivity.this);
                    myInstallation.setInstallationId(s);
                    myInstallation.setUsername(username);
                    Log.d("where", myInstallation.getUsername());
                    myInstallation.setFixer(true);
                    Log.d("where","" + myInstallation.isFixer());
                    myInstallation.save();
                    //维修员界面
                    startActivity(j);
                }else
                {
                    //用户界面
                    startActivity(i);
                }
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, "登录失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forceOffline(String name)
    {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username",name);
        query.findObjects(this,new FindListener<User>() {
            @Override
            public void onSuccess(List<User> bmobUsers) {
                User user = (User) bmobUsers.get(0);
                if (user.isOnline())
                {
                    Intent k = new Intent("com.ujnfix.RE_LOGIN");
                    sendBroadcast(k);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("sky", s);
            }
        });
    }
}
