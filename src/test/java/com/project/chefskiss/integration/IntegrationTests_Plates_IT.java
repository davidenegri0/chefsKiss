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
import org.testcontainers.containers.Container.ExecResult;
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
@DisplayName("Integration Tests for Plates")
public class IntegrationTests_Plates_IT {
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
    @DisplayName("Testa se una pagina di un piatto viene caricata correttamente")
    @Tag("integration")
    public void integrationPiattoPageTest() throws Exception {

        this.mockMvc.perform(
                        get("/plate")
                                .param("id", "10"))
                .andDo(print())
                .andExpect(view().name("platePage"))
                .andExpectAll(
                        model().attributeExists("piatto_passato"),
                        model().attributeExists("ingredienti"),
                        model().attributeExists("utente_post"),
                        model().attributeExists("sedi"),
                        model().attributeExists("ristoranti"),
                        model().attributeExists("recensioni"),
                        model().attributeExists("utenti_recensori")
                );
    }

    @Test
    @DisplayName("Testa se è possibile postare un piatto")
    @Tag("integration")
    public void integration_PostPiatto_Test() throws Exception {

        this.mockMvc.perform(
                        post("/addPlate")
                                .cookie(marioCookie)
                                .param("nomePiatto", "Piatto di prova")
                                .param("preparazione", "Descrizione di prova")
                                .param("ingredienti", "Farina,Olio d'oliva,Sale")
                                .param("quantita", "1,2,3")
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpectAll(
                        model().attributeExists("user"),
                        model().attribute("url", "/recipesView")
                );
    }

    @Test
    @DisplayName("Testa se è possibile recensire un piatto")
    @Tag("integration")
    public void integration_PostRecensionePiatto_Test() throws Exception {

        this.mockMvc.perform(
                        post("/addRecensione")
                                .cookie(marioCookie)
                                .param("type", "1")
                                .param("ID", "2")
                                .param("voto", "4")
                                .param("commento", "Recensione di prova")
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpectAll(
                        model().attributeExists("user"),
                        model().attribute("url", "/plate?id=2")
                );
    }
}
