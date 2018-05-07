package com.cqu.expstuer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.Order;
import com.cqu.expstuer.data.ExpStuerClient;
import com.cqu.expstuer.listener.OnInfoListener;
import com.cqu.expstuer.ui.setting.SettingActivity;
import com.cqu.expstuer.utils.CommonTools;
import com.cqu.expstuer.utils.JsonUtils;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class OrderService extends Service implements ExpStuerClient.OnClientListener {

    private OrderBinder mBinder = new OrderBinder();
    private CommonTools myTool;
    private int cnt = 0;
    ExpStuerClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        myTool = new CommonTools(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startWebSocketConnect();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startWebSocketConnect() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                connectWithService();
            }
        }.start();
    }

    private void connectWithService() {
        try {
            myTool.log("+ Websocket 正在建立连接 ：+\n[ userId, " + myTool.getUserId() + " ] \n[ signature, " + myTool.getToken() + " ]");

            client = new ExpStuerClient(new URI(myTool.getSocketAdd() + "controlServer?" + "userId=" + myTool.getUserId()));

            client.connectBlocking();
            myTool.log("+       Websocket 连接成功...       +");

            client.setOnClientListener(this);
        } catch (Exception e) {
            myTool.log(e.getMessage());
            // TODO: 重新连接
//            tryConnectAgain();
        }
    }

    // 尝试重新连接WebSocket
    private void tryConnectAgain() {
        myTool.log("+ 与服务器的Websocket连接失败，正在准备重新连接 +");
        client = null;

        int cnt = 10; // 10秒重连
        while (cnt > 0) {
            try {
                Thread.sleep(1000);
//                myTool.log("+       Websocket将在[ " + cnt + " s ]之后重新连接...       +");
                cnt--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        myTool.log("+       Websocket 重新连接中...       +");
        connectWithService();
    }

    @Override
    public void onDestroy() {

        if (client != null)
            client.close();

        myTool.log("+     onDestroy    +");

        try {
            Thread.sleep(2000); // 延时2s退出
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    public OrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // 监听Websocket,关闭关闭重连
    @Override
    public void onClose(int i, String s, boolean b) {
        myTool.log("Service 已关闭 ···");
//        // Token存在则重新连接
//        if (!myTool.isErrorToken()) {
//            myTool.log("Token正确，正在重连···");
//            tryConnectAgain();
//        } else {
//            myTool.log("Token错误，没有回复···");
//        }
    }

    @Override
    public void onMessage(String s) {
        if (s == null || s.length() == 0) return;

        // 判断Token是否过期
        if (isTokenError(s)) {
            myTool.log("TOKEN 错误，Service 已经关闭，等到登录时重新连接···");
            stopSelf();
            return;
        }

        showNotification(s);
    }

    private void showNotification(String s) {

        if (!JsonUtils.isJSONObj(s)) return;

        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
            if (!obj.has("farmId")) return;
            if (!obj.has("response")) return;

            myTool.log("服务器正在推送的任务信息 [farmId:" + obj.getString("farmId") + "]...");
            showNotificationV2("Title", "contents", 2);
        } catch (JSONException e) {
            myTool.log(e.getMessage());
        }
    }

    // 任务推送
    private void showNotificationV2(String title, String info, int notificationId) {

//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Task.class.getName(), task);
//
        Intent intent = new Intent(this, SettingActivity.class);
//        intent.putExtras(bundle);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(info)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.xuedilaila_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.xuedilaila_logo))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        manager.notify(notificationId, notification);

        myTool.log("There is a Order Push Notification !");
    }


    private boolean isTokenError(String s) {

        if (!"{".equals(s.charAt(0)))
            return false;

        // 进行JSON转换
        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
            String response = "";

            if (obj.has("response"))
                response = obj.getString("response");

            // TODO: 确认 ERROR TOKEN 的返回值
            if ("error token".equals(response))
                return true;

        } catch (JSONException e) {
            myTool.log("JSON Error!");
        }
        return false;
    }


    public class OrderBinder extends Binder {

        public void grabOrder(Order order) {
            if (client == null) {
                myTool.log("Task Service ERROR : WebSocket initial fialed !");
                return;
            }
            JSONObject obj = new JSONObject();
            try {
//                obj.put("farmId", order.getFarmId());
                obj.put("farmId", "xxx");
                client.send(obj.toString());
            } catch (JSONException e) {
                myTool.log("JSON Error for grabOrder()!");
            } catch (WebsocketNotConnectedException e) {
                myTool.log("socket 连接失败 ！[ " + e.getMessage() + " ]");
            }
        }

        public void setOnTaskMsgListener(OnInfoListener<String> l) {
            if (client != null)
                client.setOnTaskMsgListener(l);
        }
    }
}
