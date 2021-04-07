package com.lmm.card.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lmm.card.entity.Department;
import com.lmm.card.mapper.DepartmentMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentProvider {
    @Autowired
    private  DepartmentMapper departmentMapper;
    public void getDepartment(String token){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        Map<String,Object> map = new HashMap<>();
        map.put("access_token",token);
        map.put("page",1);
        map.put("page_size",5000);
        String json = com.alibaba.fastjson.JSON.toJSONString(map);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://open.wecard.qq.com/cgi-bin/user/org-edu-list?access_token=access_token")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(string);
            JSONArray organization = jsonObject.getJSONArray("organization");
            List<Department> departments = organization.toJavaList(Department.class);
            for (Department department : departments) {

                departmentMapper.insert(department);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
