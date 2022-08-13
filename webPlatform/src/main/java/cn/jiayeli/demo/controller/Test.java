package cn.jiayeli.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("test")
@Controller
public class Test {

    @GetMapping("test")
    public String test(Model module) {
        System.out.println("hello test man(:");
        module.addAttribute("user", "pjs");
        return "test";

    }
}
