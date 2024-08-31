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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@Testcontainers
@DisplayName("Integration Tests for Resturants")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests_Resturants_IT {
    @Container
    public static GenericContainer mysql = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_db:latest"))
            .withExposedPorts(3306)
            .waitingFor(Wait.forHealthcheck());

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    static Cookie marioCookie = new Cookie("loggedUser","CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&true&false&true&false&mario_rossi");
    static Cookie userCookie = new Cookie("loggedUser","prova&prova&prova&prova.com&1221-12-12&1234&2024-08-29&false&false&false&false&true&false&null");
    static Cookie simonaCookie = new Cookie("loggedUser","CF01234567890123&Simona&Leoni&simona@example.com&1991-08-28&6543210983&2023-08-19&true&true&true&false&true&false&simona_leoni");

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
    @DisplayName("Testa se è possibile cancellare una sede")
    @Tag("integration")
    public void integrationDeleteSedeTest() throws Exception {
        this.mockMvc.perform(
                get("/deleteSede")
                        .cookie(marioCookie)
                        .param("coord", "40.7567908;14.4431885")
                        .param("idR", "2")
        )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("url", "/restaurant?id=2"));
    }

    @Test
    @DisplayName("Testa se viene visualizzata correttamente la pagina di aggiunta di una sede al ristorante")
    @Tag("integration")
    public void integrationgetIdRistoranteTest() throws Exception {
        this.mockMvc.perform(
                        get("/addSede")
                                .cookie(marioCookie)
                )
                .andDo(print())
                .andExpect(view().name("addSedePage"))
                .andExpect(model().attributeExists("Risto"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Testa se è possibile aggiungere una valutazione ad una sede")
    @Tag("integration")
    public void integrationAddRecensioneTest() throws Exception {
        //http://localhost:8080/addRecensione?type=2&id=45.0606258;7.6840466
        this.mockMvc.perform(
                post("/addRecensione")
                        .cookie(simonaCookie)
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
    @DisplayName("Testa se è possibile aggiornare una valutazione ad una sede")
    @Tag("integration")
    public void integrationUpdateValutazioneTest() throws Exception {

        this.mockMvc.perform(
                post("/modifyRecensione")
                        .cookie(simonaCookie)
                        .param("type", "4")
                        .param("ID", "45.0606258;7.6840466")
                        .param("voto", "4")
                        .param("commento", "Ottimo ristorante!")
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attribute("url", "/sede?id=45.0606258;7.6840466"))
                .andExpect(model().attributeExists("user"));
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
                        .param("n_posti", "4")
                        .param("CF", "CF12345678901234")
        )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("url", "/sede?id=45.0606258;7.6840466"));
    }

    @Test
    @DisplayName("Testa se è possibile cancellare una valutazione")
    @Tag("integration")
    public void integrationDeleteValutazioneTest() throws Exception {

        this.mockMvc.perform(
                get("/deleteRecensione")
                        .cookie(simonaCookie)
                        .param("type", "4")
                        .param("id", "45.0606258;7.6840466")
        )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attribute("url", "/sede?id=45.0606258;7.6840466"))
                .andExpect(model().attributeExists("user"));
    }

    public void integrationRegistrationPageTest() throws Exception {

        this.mockMvc.perform(post("/registration")
                        .param("nome", "prova")
                        .param("cognome", "prova")
                        .param("cf", "prova")
                        .param("email", "prova.com")
                        .param("telefono", "1234")
                        .param("nascita", "1221-12-12")
                        .param("pssw", "prova")
                        .param("ristoratore", "true")
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Testa se è possibile aggiungere un ristorante")
    @Tag("integration")
    @Order(1)
    public void integrationAddRistoranteTest() throws Exception {
        integrationRegistrationPageTest();
        this.mockMvc.perform(
                post("/addRistorante&Sede")
                        .cookie(userCookie)
                        .param("nome_risto", "Ristorante di Prova")
                        .param("via", "Via Francesco Zanardi")
                        .param("n_civ", "56")
                        .param("citta", "Bologna")
                        .param("nposti", "100")
                        .param("coord", "44.5140741,11.3248745")
        )
                .andDo(print())
                .andExpect(view().name("redirect_to"));
    }

    @Test
    @DisplayName("Testa se è possibile visualizzare gli chef liberi")
    @Tag("integration")
    @Order(2)
    public void integrationChefListTest() throws Exception {
        this.mockMvc.perform(
                get("/addChef")
                        .cookie(userCookie)
                        .param("ID", "44.5140741,11.3248745")
        )
                .andDo(print())
                .andExpect(view().name("addChefPage"))
                .andExpect(model().attributeExists("CoordSede"))
                .andExpect(model().attribute("CoordSede", "44.5140741,11.3248745"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("chefs"));
    }
}
