package com.lmm.card.provider;




import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageProvider {
    public Integer sendMessage(String access_token,String name,String number,Integer who,String operatorNumber,Integer type){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
         OkHttpClient client = new OkHttpClient();

        Map<String,String> para = new HashMap<>();
        List<String> cardList = new ArrayList<>();

        if(operatorNumber == null){
            cardList.add(number);
        }else {
            cardList.add(operatorNumber);
        }

        String cards = com.alibaba.fastjson.JSON.toJSONString(cardList);
        para.put("access_token",access_token);
        para.put("cards",cards);
        para.put("title","申请补卡成功");


        if(type == 0){
            if(who == 0){
                para.put("content","姓名："+name+"，卡号："+number+";已经成功申请补办饭卡！请等待补办结束后，携带20元现金到学校续卡室领取！");
            }else {
                para.put("content","姓名："+name+"，卡号："+number+";已经成功申请补办饭卡！请登陆后台处理");
            }
        }else {
            para.put("content","姓名："+name+"，卡号："+number+";已经补卡完成，请携带20元现金到学校续卡室领取！");

        }
        para.put("sender","巴蜀中学膳食科");
        String url = "https://open.wecard.qq.com/cgi-bin/notice/send";
        String json = com.alibaba.fastjson.JSON.toJSONString(para);
        System.out.println("json para= " + json);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println("stringBody = " + string);
            JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(string);
            System.out.println("MessageJsonObject = " + jsonObject);
            Integer code = jsonObject.getInteger("code");

            return code;

        }catch (IOException e){
            e.printStackTrace();
            return 10000;
        }

    }
}
