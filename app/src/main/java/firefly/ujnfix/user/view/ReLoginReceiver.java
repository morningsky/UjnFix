package firefly.ujnfix.user.view;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

import firefly.ujnfix.R;

/**
 * Created by sky on 2015/8/22.
 */
public class ReLoginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(R.drawable.ic_launcher)
                .setTitle("Warning!")
                .setMessage("抱歉，您的帐号在别处登录！若非本人操作，请及时修改密码。")
                .setCancelable(false)
                .setNegativeButton("ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context,LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //标志在广播接收器里启动活动
                        context.startActivity(i);
                    }
                });
        AlertDialog alertDialog = dialog.create();
        //需要设置dialog的类型，保证在广播接收器中可以正常弹出
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
}
