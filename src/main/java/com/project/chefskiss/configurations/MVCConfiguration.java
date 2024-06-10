package com.project.chefskiss.configurations;

import com.project.chefskiss.controllers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
//@EnableWebMvc
public class MVCConfiguration implements WebMvcConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/",
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolv() {
        return new StandardServletMultipartResolver();
    };

/*    @Bean
    public homepageController homepageController_bean() {
        return new homepageController();
    }

    @Bean
    public addPlateController addPlateController_bean() {
        return new addPlateController();
    }

    @Bean
    public addPrenotazioneController addPrenotazioneController_bean(){
        return new addPrenotazioneController();
    }

    @Bean
    public addRecensioneController addRecensioneController_bean(){
        return new addRecensioneController();
    }

    @Bean
    public addSedeController addSedeController_bean(){
        return new addSedeController();
    }

    @Bean
    public chefResourcesController chefResourcesController_bean(){
        return new chefResourcesController();
    }

    @Bean
    public deletePlateController deletePlateController_bean(){
        return new deletePlateController();
    }

    @Bean
    public deletePrenotazioneController deletePrenotazioneController_bean(){
        return new deletePrenotazioneController();
    }

    @Bean
    public deleteRecensioneController deleteRecensioneController_bean(){
        return new deleteRecensioneController();
    }

    @Bean
    public deleteSedeController deleteSedeController_bean(){
        return new deleteSedeController();
    }

    @Bean
    public deleteProfile deleteProfile_bean(){
        return new deleteProfile();
    }

    @Bean
    public editPlateController editPlateController_bean(){
        return new editPlateController();
    }

    @Bean
    public editPrenotazioneController editPrenotazioneController_bean(){
        return new editPrenotazioneController();
    }

    @Bean
    public editRecensioneController editRecensioneController_bean(){
        return new editRecensioneController();
    }

    @Bean
    public editSedeController editSedeController_bean(){
        return new editSedeController();
    }

    @Bean
    public loginController loginController_bean(){
        return new loginController();
    }

    @Bean
    public plateController plateController_bean(){
        return new plateController();
    }

    @Bean
    public prenotazioniListController prenotazioniListController_bean(){
        return new prenotazioniListController();
    }

    @Bean
    public profileController profileController_bean(){
        return new profileController();
    }

    @Bean
    public recipesListController recipesListController_bean(){
        return new recipesListController();
    }

    @Bean
    public registrationController registrationController_bean(){
        return new registrationController();
    }

    @Bean
    public restaurantController restaurantController_bean(){
        return new restaurantController();
    }

    @Bean
    public resturantsListController resturantsListController_bean(){
        return new resturantsListController();
    }

    @Bean
    public sedeController sedeController_bean(){
        return new sedeController();
    }*/
}