package com.lmm.card.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmm.card.entity.Information;
import com.lmm.card.mapper.InformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CardController {
    @Autowired
    private InformationMapper informationMapper;
    @PostMapping("/card")
    public String card(Information information,String imgCode, Model model,HttpSession session){
        String code = (String)session.getAttribute("code");
        if(!imgCode.toUpperCase().equals(code)){
            model.addAttribute("departName",information.getDepartment());
            model.addAttribute("name",information.getName());
            model.addAttribute("cardNumber",information.getNumber());
            model.addAttribute("school",information.getSchool());
            model.addAttribute("msg","验证码输入错误");
        }else {
            information.setStatus(0);
            information.setApplyTime(LocalDateTime.now());
            QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("number",information.getNumber());
            queryWrapper.eq("status",0);
            List<Information> information1 = informationMapper.selectList(queryWrapper);
            if(information1.size()>0){

                model.addAttribute("departName",information.getDepartment());
                model.addAttribute("name",information.getName());
                model.addAttribute("cardNumber",information.getNumber());
                model.addAttribute("school",information.getSchool());
                model.addAttribute("msg","你已经提交过申请，请等待");


            }else {

                informationMapper.insert(information);
                model.addAttribute("departName",information.getDepartment());
                model.addAttribute("name",information.getName());
                model.addAttribute("cardNumber",information.getNumber());
                model.addAttribute("school",information.getSchool());
                model.addAttribute("msg","申请成功！");

            }
        }




        return "index";
    }

    @GetMapping("/findMy")
    public String findMy(String cardNumber,Model model){
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number",cardNumber);
        queryWrapper.orderByDesc("id");
        List<Information> information = informationMapper.selectList(queryWrapper);
        model.addAttribute("information",information);
        return "findMy";
    }

    @GetMapping("getCode")
    @ResponseBody
    public void createCode(HttpServletResponse response ,HttpSession session){
        //产生验证码图片的。图片的宽是116，高是36，验证码的长度是4，干扰线的条数是20
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 20);
        //获取验证码图片中的字符串
        String code1 = lineCaptcha.getCode();

        //把这个图片交给response相应给浏览器。
        //获取到response的响应流。


        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //把图片放入到response的相应流中。
            lineCaptcha.write(outputStream);
            outputStream.close();
            //把验证码图片中的字符串放入session
            session.setAttribute("code",code1.toUpperCase());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
