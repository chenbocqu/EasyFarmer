package com.cqu.expstuer.utils;


import android.content.Context;

import com.cqu.expstuer.bean.ContactAddress;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.listener.OnRequestListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class DataMaker {


    public static List<ContactAddress> getAddresses() {
        ArrayList<ContactAddress> data = new ArrayList<>();

        ContactAddress contactAddress = new ContactAddress();
        for (int i = 0; i < 10; i++)
            data.add(contactAddress);

        return data;
    }

    /**
     * 更新用户信息到本地
     *
     * @param context
     */
    public static void updateUserInfo(Context context) {
        updateUserInfo(context, null);
    }

    public static void updateUserInfo(Context context, final OnRequestListener l) {

        final CommonTools myTool = new CommonTools(context);

        OkHttpUtils.get().url(myTool.getServAdd() + "consumerUserQuery.do")
                .addParams("userID", myTool.getUserId())
//                .addParams("signature", myTool.getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.log("Update User Info ,err = " + e.getMessage());
                        if (l != null) l.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.log(response);
                        if (response == null) return;
                        try {
                            JSONObject obj = new JSONObject(response);
                            User user = new User();

                            user.setSeId(obj.getString("userID"));
                            user.setPwd(obj.getString("userPassword"));
                            user.setRegisterTime(obj.getString("userRegisterTime"));

                            user.setState(obj.getString("userState"));
                            user.setIntegral(obj.getInt("userIntegration"));
                            user.setGrade(obj.getString("userQualityRating"));
                            user.setBalance(obj.getDouble("remainingSum"));

                            if (obj.has("userNickname"))
                                user.setNickName(obj.getString("userNickname"));

                            if (obj.has("userImageUrl"))
                                user.setImageUrl(obj.getString("userImageUrl"));

                            if (obj.has("userBackGroundImageUrl"))
                                user.setBgUrl(obj.getString("userBackGroundImageUrl"));

                            if (obj.has("userSex")) user.setSex(obj.getString("userSex"));

                            if (obj.has("userSignature"))
                                user.setSignature(obj.getString("userSignature"));

                            myTool.setUserInfo(user);

                        } catch (JSONException e) {
                            myTool.log("Parse User Info ,err = " + e.getMessage());
                            if (l != null) l.onError(e);
                        }
                        if (l != null) l.onResponse(response);
                    }
                });
    }
}
