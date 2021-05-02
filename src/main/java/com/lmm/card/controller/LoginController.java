package com.lmm.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lmm.card.mapper.DepartmentMapper;
import com.lmm.card.provider.InfoProvider;
import com.lmm.card.provider.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@Slf4j
public class LoginController {
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private InfoProvider infoProvider;
//    @Autowired
//    private DepartmentProvider departmentProvider;

    @Autowired
    private DepartmentMapper departmentMapper;
    @GetMapping("/callback")
    public String callback(String wxcode, String state, Model model, HttpServletResponse response) throws IOException {
        String token = userProvider.getToken(wxcode, state);
        JSONObject jsonObject = JSON.parseObject(token);
        String access_token = jsonObject.getString("access_token");
        if(access_token == null){
            response.sendRedirect("https://msg.weixiao.qq.com/t/611546b3e15cc31162f0a3f503d44d31");
            return null;
        }else {
            String info = infoProvider.getNumber(access_token);
            JSONObject jsonObject1 = JSON.parseObject(info);
            String name = jsonObject1.getString("name");
            String cardNumber = jsonObject1.getString("card_number");

            JSONArray organization = jsonObject1.getJSONArray("organization");

            Integer id = organization.getInteger(organization.size() - 1);
            String departName = departmentMapper.selectById(id).getName();


            model.addAttribute("departName",departName);
            model.addAttribute("name",name);
            model.addAttribute("cardNumber",cardNumber);
            return "index";
        }


    }
}
