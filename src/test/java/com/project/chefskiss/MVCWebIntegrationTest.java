package com.project.chefskiss;
import com.project.chefskiss.controllers.homepageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
@WebAppConfiguration
public class MVCWebIntegrationTest {

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new homepageController())
                .alwaysExpect(status().isOk())
                .alwaysExpect(content().contentType(MediaType.TEXT_HTML))
                .build();
    }

    /*
    @Test
    void TestHomepageAvanzato() throws Exception{
        mockMvc.perform(get("/homepage"))
                .andExpectAll(
                        status().isOk(),
                        model().hasNoErrors(),
                        model().attributeExists("listaPiatti")
                );
    }
     */
}
