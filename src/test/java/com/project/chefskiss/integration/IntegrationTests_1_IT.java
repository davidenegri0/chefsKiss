package com.project.chefskiss.integration;

import com.project.chefskiss.configurations.Config;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@Testcontainers
@DisplayName("Integration Tests")
public class IntegrationTests_1_IT {
    @Container
    public static GenericContainer mysql = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_db:latest"))
            .withExposedPorts(3306)
            .waitingFor(Wait.forHealthcheck());

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        mysql.start();
        System.out.println("Connecting to port " + mysql.getMappedPort(3306).toString());
        Config.DATABASE_PORT = mysql.getMappedPort(3306).toString();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
        Config.DATABASE_URL = "jdbc:mysql://localhost:3306/chefskiss?user=root&password=segretissima&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=" + Config.SERVER_TIMEZONE;
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

/*    @Test
    @DisplayName("Testa se il contesto dell'applicazione è stato caricato correttamente")
    @Tag("integration")
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("homepageController"));
    }*/

    @Test
    @DisplayName("Testa se la homepage viene caricata correttamente")
    @Tag("integration")
    public void integrationHomePageTest() throws Exception {

        this.mockMvc.perform(get("/homepage")).andDo(print())
                .andExpect(view().name("homepagePage"))
                .andExpect(model().attributeExists("listaPiatti"));
    }

    @Test
    @DisplayName("Testa se la pagina di login viene caricata correttamente")
    @Tag("integration")
    public void integrationLoginPageTest() throws Exception {

        this.mockMvc.perform(get("/login")).andDo(print())
                .andExpect(view().name("loginPage"));
    }

    @Test
    @DisplayName("Testa se il login viene effettuato correttamente")
    @Tag("integration")
    public void integrationLoginPageTest_validLogin() throws Exception {

        this.mockMvc.perform(
                get("/login")
                        .param("email", "mario@example.com")
                        .param("pssw", "securepass"))
                .andDo(print())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", "Mario Rossi"));
    }

    @Test
    @DisplayName("Testa se la lista dei piatti viene caricata correttamente")
    @Tag("integration")
public void integrationPiattiListTest() throws Exception {

        this.mockMvc.perform(
                get("/recipesView"))
                .andDo(print())
                .andExpect(view().name("recipesListPage"))
                .andExpect(model().attributeExists("listaPiatti"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Testa se la lista dei piatti viene ordinata come richiesto")
    @Tag("integration")
    public void integrationPiattiListTest_ordered(int order) throws Exception {

        this.mockMvc.perform(
                        get("/recipesView")
                                .param("ord", String.valueOf(order)))
                .andDo(print())
                .andExpect(view().name("recipesListPage"))
                .andExpect(model().attributeExists("listaPiatti"));
    }

    @ParameterizedTest
    @CsvSource({"Pizza, 1", "Pomodoro, 2"})
    @DisplayName("Testa se le ricerche nella lista dei piatti funzionano correttamente")
    @Tag("integration")
    public void integrationPiattiListTest_search(String nome, int type) throws Exception {

        this.mockMvc.perform(
                        get("/recipesView")
                                .param("search", nome)
                                .param("searchType", String.valueOf(type)))
                .andDo(print())
                .andExpect(view().name("recipesListPage"))
                .andExpect(model().attributeExists("listaPiatti"))
                .andExpect(model().attribute("searched", true));
    }

    @Test
    @DisplayName("Testa se la lista dei ristoranti viene caricata correttamente")
    @Tag("integration")
    public void integrationResturantListTest() throws Exception {

        this.mockMvc.perform(
                        get("/resturantsList"))
                .andDo(print())
                .andExpect(view().name("resturantsListPage"))
                .andExpect(model().attributeExists("ristoranti"))
                .andExpect(model().attributeExists("sedi"));
    }

    @ParameterizedTest
    @CsvSource({"Trattoria, 1", "Bologna, 2"})
    @DisplayName("Testa se le ricerche nella lista dei ristoranti funzionano correttamente")
    @Tag("integration")
    public void integrationResturantListTest_search(String nome, int type) throws Exception {

        this.mockMvc.perform(
                        get("/resturantsList")
                                .param("search", nome)
                                .param("searchType", String.valueOf(type)))
                .andDo(print())
                .andExpect(view().name("resturantsListPage"))
                .andExpect(model().attributeExists("ristoranti"))
                .andExpect(model().attributeExists("sedi"))
                .andExpect(model().attribute("searched", true));
    }
}
