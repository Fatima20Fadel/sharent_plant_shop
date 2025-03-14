package com.proposal.Nature.Heaven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("experienceYears", "25");
        model.addAttribute("experienceText", "Years of Experience");
        model.addAttribute("homeDescription", "We Make Your Life A Heaven");
        model.addAttribute("homeDetails", "Welcome to SharentShop, your go-to online destination for all plants! We're not just a plant shop we are a life changer");
        model.addAttribute("awardTitle", "Award Winning");
        model.addAttribute("awardDescription", "We’re honored to receive many awards for our dedication");
        model.addAttribute("teamTitle", "Dedicated Team");
        model.addAttribute("teamDescription", "At SharentShop, our success is driven by a passionate and dedicated team of plant enthusiasts and profession");
        return "about";
    }

}