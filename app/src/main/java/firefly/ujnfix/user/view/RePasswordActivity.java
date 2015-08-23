package firefly.ujnfix.user.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.User;

/**
 * Created by 清晨 on 2015/7/28.
 */
public class RePasswordActivity extends ActionBarActivity {
    private EditText edt_phoneNumber;//输入手机号
    private EditText edt_smsCode;//输入验证码
    private Button btn_rePassword;//发送短信验证码
    private Button btn_sureSmsCode;//提交验证码
    private static String phoneNumber;
    private String smsCode;
    private User   user;
    private String newPassword;
    private String sure_newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repassword);
        init();

        btn_rePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = edt_phoneNumber.getText().toString();
                BmobSMS.requestSMSCode(RePasswordActivity.this,phoneNumber,"找回密码",new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null)
                        {
                            toast("短信验证码发送成功，请注意查收");
                        }else
                        {
                            toast("发送失败，异常为" + e.toString());
                        }
                    }
                });
            }
        });

        btn_sureSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsCode = edt_smsCode.getText().toString();
                BmobSMS.verifySmsCode(RePasswordActivity.this,phoneNumber,smsCode,new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null)
                        {
                            toast("验证成功");
                            BmobQuery<User> query = new BmobQuery<User>();
                            query.addWhereEqualTo("mobilePhoneNumber",phoneNumber);
                            query.findObjects(RePasswordActivity.this,new FindListener<User>() {
                                @Override
                                public void onSuccess(List<User> users) {
                                    toast("查到此人："+users.size());
                                    user = users.get(0);
                                    saveNewPassword(user);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    toast("抱歉没有这个人，请重新注册" + s);
                                }
                            });

                        }else
                        {
                            toast("发送失败，异常为"+e.toString());
                        }
                    }
                });
            }
        });
    }

    public void init()
    {
        edt_phoneNumber = (EditText) findViewById(R.id.edt_phoneNumber);
        edt_smsCode = (EditText) findViewById(R.id.edt_smsCode);
        btn_rePassword = (Button) findViewById(R.id.btn_rePassword);
        btn_sureSmsCode = (Button) findViewById(R.id.btn_sureSmsCode);
    }

    public void toast(String flag)
    {
        Toast.makeText(RePasswordActivity.this,flag,Toast.LENGTH_SHORT).show();
    }

    public void saveNewPassword(final User mUser)
    {
        //加载对话框视图
        LayoutInflater inflater =LayoutInflater.from(RePasswordActivity.this);
        View v = inflater.inflate(R.layout.dialog_repassword,null);
        final EditText edt_newPassword = (EditText) v.findViewById(R.id.edt_newPassword);
        final EditText edt_sure_newPassword = (EditText) v.findViewById(R.id.edt_sure_newPassword);

        AlertDialog.Builder dia_rePassword = new AlertDialog.Builder(RePasswordActivity.this);
        dia_rePassword.setTitle("重置密码")
                .setCancelable(false)
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        newPassword = edt_newPassword.getText().toString();
                        sure_newPassword = edt_sure_newPassword.getText().toString();
                        if (newPassword != null) {
                            if (sure_newPassword.equals(newPassword)) {
                                mUser.setPassword(newPassword);
                                mUser.update(RePasswordActivity.this, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        toast("密码重置成功");
                                        Intent i = new Intent(RePasswordActivity.this,LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        toast("密码重置失败，抱歉" + s);
                                    }
                                });
                            } else {
                                toast("前后密码不一致，要细心喔");
                            }
                        } else {
                            toast("m密码不能为空");
                        }


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("点击了取消");
                    }
                })
                .show();
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
