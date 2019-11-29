package com.sherlocky.halo.web;

import com.sherlocky.halo.constant.HaloConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * view 控制器
 * @author: zhangcx
 * @date: 2019/7/8 11:18
 */
@Controller
public class HaloViewController {
    private static Logger logger = LoggerFactory.getLogger(HaloViewController.class);
    /**
     * 首页即为搜索页面
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String search(Model model){
        model.addAttribute("updatetime", HaloConstants.BLOG_UPDATE_TIME);
        return "/search";
    }
}
