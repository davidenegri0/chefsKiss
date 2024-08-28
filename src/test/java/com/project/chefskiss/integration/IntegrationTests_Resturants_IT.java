package com.project.chefskiss.integration;

import com.project.chefskiss.configurations.Config;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@Testcontainers
@DisplayName("Integration Tests for Resturants")
public class IntegrationTests_Resturants_IT {
    @Container
    public static GenericContainer mysql = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_db:latest"))
            .withExposedPorts(3306)
            .waitingFor(Wait.forHealthcheck());

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    static Cookie marioCookie = new Cookie("loggedUser","CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&true&false&true&false&mario_rossi");

    @BeforeAll
    static void beforeAll() {
        mysql.start();
        if (mysql.isHealthy()) System.out.println("DB Ready! Connecting to port " + mysql.getMappedPort(3306).toString());
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

    @Test
    @DisplayName("Testa se la pagina di un ristorante viene caricata correttamente")
    @Tag("integration")
    public void integrationResturantPageTest() throws Exception {

        this.mockMvc.perform(
                        get("/restaurant")
                                .param("id", "1"))
                .andDo(print())
                .andExpect(view().name("restaurantPage"))
                .andExpectAll(
                        model().attributeExists("ristorante"),
                        model().attributeExists("sedi"),
                        model().attributeExists("ristoratore")
                );
    }

    @Test
    @DisplayName("Testa se la pagina di una sede viene caricata correttamente")
    @Tag("integration")
    public void integrationSedePageTest() throws Exception {
        //http://localhost:8080/sede?id=45.548048;9.3881801
        this.mockMvc.perform(
                        get("/sede")
                                .param("id", "45.548048;9.3881801"))
                .andDo(print())
                .andExpect(view().name("sedePage"))
                .andExpectAll(
                        model().attributeExists("sede"),
                        model().attributeExists("piatti"),
                        model().attributeExists("valutazioni")
                );
    }

    @Test
    @DisplayName("Testa se è possibile aggiungere una sede ad un ristorante")
    @Tag("integration")
    public void integrationAddSedeTest() throws Exception {
        this.mockMvc.perform(
                post("/addSede")
                        .cookie(marioCookie)
                        .param("via", "Via di Prova")
                        .param("n_civ", "1")
                        .param("citta", "Roma")
                        .param("nposti", "100")
                        .param("idristo", "2")
                        .param("coord", "11.11;22.22")
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attribute("url", "/restaurant?id=2"));
    }

    @Test
    @DisplayName("Testa se è possibile aggiungere una recensione ad una sede")
    @Tag("integration")
    public void integrationAddRecensioneTest() throws Exception {
        //http://localhost:8080/addRecensione?type=2&id=45.0606258;7.6840466
        this.mockMvc.perform(
                post("/addRecensione")
                        .cookie(marioCookie)
                        .param("type", "2")
                        .param("ID", "45.0606258;7.6840466")
                        .param("voto", "5")
                        .param("commento", "Ottimo ristorante!")
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attribute("url", "/sede?id=45.0606258;7.6840466"));
    }

    @Test
    @DisplayName("Testa se è possibile aggiungere una prenotazione ad una sede")
    @Tag("integration")
    public void integrationAddPrenotazioneTest() throws Exception {
        //http://localhost:8080/addPrenotazione?coordinate=45.0606258;7.6840466&data=2023-08-19&orario=12:00&n_posti=2&CF=CF12345678901234
        this.mockMvc.perform(
                post("/addPrenotazione")
                        .cookie(marioCookie)
                        .param("coordinate", "45.0606258;7.6840466")
                        .param("data", "2023-10-19")
                        .param("orario", "12:00")
                        .param("n_posti", "2")
                        .param("CF", "CF12345678901234")
        )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attribute("url", "/sede?id=45.0606258;7.6840466"));
    }
}
