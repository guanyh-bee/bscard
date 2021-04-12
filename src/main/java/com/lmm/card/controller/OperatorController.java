package com.lmm.card.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmm.card.entity.Information;
import com.lmm.card.entity.Operator;
import com.lmm.card.mapper.InformationMapper;
import com.lmm.card.mapper.OperatorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/op")
@Controller
public class OperatorController {
    @Autowired
    private OperatorMapper operatorMapper;
    @Autowired
    private InformationMapper informationMapper;

    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(Operator operator, Model model, HttpSession session){
        QueryWrapper<Operator> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_name",operator.getOperatorName().trim());
        Operator operator1 = operatorMapper.selectOne(queryWrapper);
        boolean b = DigestUtil.bcryptCheck(operator.getPassword(), operator1.getPassword());

        if(b){
            session.setAttribute("user",operator1);
            QueryWrapper<Information> informationQueryWrapper = new QueryWrapper<>();
            informationQueryWrapper.eq("status",0);
            informationQueryWrapper.eq("school",operator1.getSchool());
            informationQueryWrapper.orderByAsc("apply_time");

            List<Information> information = informationMapper.selectList(informationQueryWrapper);
            model.addAttribute("info",information);

            return "main";
        }else {
            model.addAttribute("msg","用户名或密码错误！");
            return "login";
        }
    }

    @GetMapping("/confirm")
    public String confirm(Integer id,HttpSession session,Model model){

        Operator user = (Operator)session.getAttribute("user");
        Information information = new Information();
        information.setId(id).setStatus(1);
        information.setHandleTime(LocalDateTime.now());
        information.setOperator(user.getOperatorName());
        informationMapper.updateById(information);

        return "redirect:/op/main";
    }
    @GetMapping("/search")
    public String search(String key,Model model,HttpSession session){
        Operator user = (Operator)session.getAttribute("user");

        QueryWrapper<Information> informationQueryWrapper = new QueryWrapper<>();
        informationQueryWrapper.eq("school",user.getSchool());
        informationQueryWrapper.orderByAsc("status");
        informationQueryWrapper.orderByDesc("handle_time");
        List<Information> information = new ArrayList<>();
        if(key == null || "".equals(key)){

        }else {
            informationQueryWrapper.like("name",key).or().like("name",key);
        }
        information = informationMapper.selectList(informationQueryWrapper);
        model.addAttribute("info",information);
        return "main";
    }

    @GetMapping("/handled")
    public String handled(HttpSession session,Model model){
        Operator user = (Operator)session.getAttribute("user");
        QueryWrapper<Information> informationQueryWrapper = new QueryWrapper<>();
        informationQueryWrapper.eq("school",user.getSchool());
        informationQueryWrapper.eq("status",1);
        informationQueryWrapper.orderByDesc("handle_time");
        List<Information> information = informationMapper.selectList(informationQueryWrapper);
        model.addAttribute("info",information);
        return "main";
    }

    @GetMapping("/toChangePass")
    public String toChangePass(){
        return "changePass";
    }

    @PostMapping("/changePass")
    public String changePass(String newPassword,String srcPassword,Model model,HttpSession session){
        Operator user = (Operator)session.getAttribute("user");
        boolean b = DigestUtil.bcryptCheck(srcPassword, user.getPassword());
        if(b){
            user.setPassword(DigestUtil.bcrypt(newPassword));
            operatorMapper.updateById(user);
            model.addAttribute("msg","修改成功");
        }else {
            model.addAttribute("msg","修改失败，请检查密码");
        }
        return "changePass";
    }

    @GetMapping("/main")
    public String firstPage(HttpSession session,Model model){
        Operator user = (Operator)session.getAttribute("user");
        QueryWrapper<Information> informationQueryWrapper = new QueryWrapper<>();
        informationQueryWrapper.eq("school",user.getSchool());
        informationQueryWrapper.eq("status",0);
        informationQueryWrapper.orderByAsc("apply_time");
        List<Information> information = informationMapper.selectList(informationQueryWrapper);
        model.addAttribute("info",information);
        return "main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/op/toLogin";
    }
}
