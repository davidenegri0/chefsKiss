package com.project.chefskiss.integration;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.User;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@Testcontainers
@DisplayName("Integration Tests")
public class IntegrationTests_Generic_IT {
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
                .andExpect(model().attribute("user", "Mario Rossi"))
                .andExpect(cookie().exists("loggedUser"));
    }

    @Test
    @DisplayName("Testa se è possibile registrarsi come utente")
    @Tag("integration")
    public void integrationRegistrationPageTest() throws Exception {

        this.mockMvc.perform(post("/registration")
                        .param("nome", "Nome")
                        .param("cognome", "Cognome")
                        .param("cf", "CF123ABCZYX")
                        .param("email", "example99@email.com")
                        .param("telefono", "9988776655")
                        .param("nascita", "2003-01-01")
                        .param("pssw", "password")
                )
                .andDo(print())
                .andExpect(view().name("index"))
                .andExpect(cookie().exists("loggedUser"));
    }

    @Test
    @DisplayName("Testa se è possibile visualizzare le prenotazioni")
    @Tag("integration")
    public void integrationPrenotazioniPageTest() throws Exception {

        ResultActions res = this.mockMvc.perform(get("/prenotazioniList")
                        .cookie(marioCookie)
                )
                .andDo(print())
                .andExpect(view().name("prenotazioniListPage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("prenotazioni"));

        User utente = (User) res.andReturn().getModelAndView().getModel().get("user");
        List<Prenotazione> prenotazioni = (List<Prenotazione>) res.andReturn().getModelAndView().getModel().get("prenotazioni");
        assertTrue(utente.getNome().equals("Mario"));
        assertTrue(utente.getCognome().equals("Rossi"));
        assertTrue(prenotazioni.size() > 0);
    }

    @ParameterizedTest
    @DisplayName("Testa se è possibile aggiungere recensioni/valutazioni/prenotazioni")
    @CsvSource({
            "1, 8, 3",
            "2, 43.3180529;11.3319483, 1"
    })
    @Tag("integration")
    public void integrationAddRecensioneTest(int type, String ID, int voto) throws Exception {

        this.mockMvc.perform(get("/addRecensione")
                        .cookie(marioCookie)
                        .param("type", String.valueOf(type))
                        .param("id", ID)
                        .param("voto", String.valueOf(voto))
                )
                .andDo(print())
                .andExpect(view().name("addRecensionePage"));
    }
}
