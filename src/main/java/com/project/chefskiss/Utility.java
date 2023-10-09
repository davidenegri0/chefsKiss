package com.project.chefskiss;
import org.springframework.web.servlet.ModelAndView;

public class Utility {
    public static ModelAndView redirect(ModelAndView page, String path){
        page.setViewName("redirect_to");
        page.addObject("url", path);
        return page;
    }

}
