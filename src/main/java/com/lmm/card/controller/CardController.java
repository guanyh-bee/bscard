package com.lmm.card.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmm.card.entity.Information;
import com.lmm.card.mapper.InformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CardController {
    @Autowired
    private InformationMapper informationMapper;
    @PostMapping("/card")
    public String card(Information information, Model model){
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
            model.addAttribute("msg","你已经提交过申请，请等待");


        }else {

            informationMapper.insert(information);
            model.addAttribute("departName",information.getDepartment());
            model.addAttribute("name",information.getName());
            model.addAttribute("cardNumber",information.getNumber());
            model.addAttribute("msg","申请成功！");

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
}
