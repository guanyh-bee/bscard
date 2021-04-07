package com.lmm.card.provider;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class InfoProvider {
    public String getNumber(String token){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        Map<String,String> map = new HashMap<>();
        map.put("access_token",token);
        String json = com.alibaba.fastjson.JSON.toJSONString(map);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://open.wecard.qq.com/connect/oauth/get-user-info")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            return string;

        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }
    }
}
