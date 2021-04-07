package com.lmm.card.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserProvider {
    public String getToken(String wxcode,String state){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        Map<String,String> map = new HashMap<>();
        map.put("wxcode",wxcode);
        map.put("app_key","68D7464EDF49F8AD");
        map.put("app_secret","4116719275EE1076027EEA297EAFEB18");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://www.ca-falcon.com/callback");
        String json = com.alibaba.fastjson.JSON.toJSONString(map);


        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://open.wecard.qq.com/connect/oauth2/token")
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
