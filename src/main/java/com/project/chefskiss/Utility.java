package com.project.chefskiss;

import com.project.chefskiss.configurations.Config;
import org.springframework.web.servlet.ModelAndView;

public class Utility {
    public static ModelAndView redirect(ModelAndView page, String path){
        page.setViewName("redirect_to");
        page.addObject("url", Config.SERVER_ADDRESS_PORT+path);
        return page;
    }
}
