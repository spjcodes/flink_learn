package cn.jiayeli.dataLeaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test {

    @RequestMapping("test")
    public String test(ModelMap map) {
        map.addAttribute("context","hello thymeleaf");
        return "index";
    }


}
