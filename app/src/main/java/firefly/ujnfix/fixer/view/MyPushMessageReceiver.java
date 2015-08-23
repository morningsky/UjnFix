package firefly.ujnfix.fixer.view;
/*
发送推送
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;
import firefly.ujnfix.R;

public class MyPushMessageReceiver extends BroadcastReceiver {
    private  int tag;
    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
        //解析JSON
        JSONTokener jsonTokener = new JSONTokener(message);
        try {
            JSONObject object = (JSONObject) jsonTokener.nextValue();
            message = object.getString("alert");
            tag = object.getInt("tag");
            Log.d("where","" + tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 发送通知
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //维修员之间通讯
        if (tag == 1)
        {
            Notification n = new Notification();
            n.icon = R.drawable.ic_launcher;
            n.tickerText = "您有一条新消息";
            n.when = System.currentTimeMillis();
            Intent i = new Intent(context, FixerChatDialog.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            n.setLatestEventInfo(context, "您有一条新消息", message, pi);
            n.defaults |= Notification.DEFAULT_ALL;
            n.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(1, n);
        }
        //提交报修
        else if (tag == 2)
        {
            Notification n = new Notification();
            n.icon = R.drawable.ic_launcher;
            n.tickerText = "您有一条维修任务";
            n.when = System.currentTimeMillis();
            Intent i = new Intent(context, FixerMainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            n.setLatestEventInfo(context, "您有一条维修任务", message, pi);
            n.defaults |= Notification.DEFAULT_ALL;
            n.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(1, n);

        }

    }
}

