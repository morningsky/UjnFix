package firefly.ujnfix.fixer.view;
/*
给其他修理员发送信息
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.User;

public class FixerChatDialog extends ActionBarActivity {
    BmobPushManager bmobPush;
    EditText edit_shouxinren, edit_content;
    Button btn_sendmessage;
    BmobUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bmobPush = new BmobPushManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixer_chat_dialog);
        curUser = BmobUser.getCurrentUser(this);
        edit_shouxinren = (EditText) findViewById(R.id.edt_shouxinren);
        edit_content = (EditText) findViewById(R.id.edt_content);
        btn_sendmessage = (Button) findViewById(R.id.btn_sendmessage);
        btn_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushAndroidMessage(curUser.getUsername().toString() + " : " + edit_content.getText().toString(),
                        edit_shouxinren.getText().toString());
                showNormalDia();
            }
        });
    }

    private void showNormalDia() {
        AlertDialog.Builder normalDia = new AlertDialog.Builder(FixerChatDialog.this);
        normalDia.setIcon(R.drawable.ic_launcher);
        normalDia.setTitle("发送成功");
        normalDia.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        normalDia.create().show();
    }

    private void pushAndroidMessage(String message, String username) {
        BmobQuery<User> query = new BmobQuery<User>();
        //query.addWhereEqualTo("username", username);
        //bmobPush.setQuery(query);
        JSONObject json = new JSONObject();
        try {
            json.put("alert",message);
            json.put("tag",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bmobPush.pushMessage(json);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
