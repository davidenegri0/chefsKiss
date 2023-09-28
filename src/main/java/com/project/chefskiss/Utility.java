package com.project.chefskiss;

import com.project.chefskiss.configurations.Config;
import org.springframework.web.servlet.ModelAndView;

public class Utility {
    public static ModelAndView redirect(ModelAndView page, String url){
        page.setViewName("redirect_to");
        page.addObject("url", Config.SERVER_ADDRESS_PORT+url);
        return page;
    }
}
