package com.lmm.card.controller;

import com.lmm.card.entity.Information;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CardController {
    @PostMapping("/card")
    public String card(Information information){
        System.out.println("information = " + information);
        return null;
    }
}
