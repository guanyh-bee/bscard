package com.lmm.card.provider;

import com.alibaba.fastjson.JSONObject;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {
    public String getAccessToken(){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        Map<String,String> para = new HashMap<>();

        para.put("app_key","68D7464EDF49F8AD");
        para.put("app_secret","4116719275EE1076027EEA297EAFEB18");
        para.put("grant_type","client_credentials");
        para.put("scope","base");
        para.put("ocode","1011932261");
        String json = com.alibaba.fastjson.JSON.toJSONString(para);

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("https://open.wecard.qq.com/cgi-bin/oauth2/token")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(string);
            String access_token = jsonObject.getString("access_token");
            System.out.println("access_token = " + access_token);
            return access_token;

        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }

    }
}
