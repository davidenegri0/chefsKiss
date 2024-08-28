package com.project.chefskiss.integration;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Integration Tests")
public class IntegrationTests_Generic_IT {
    @Container
    public static GenericContainer mysql = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_db:latest"))
            .withExposedPorts(3306)
            .waitingFor(Wait.forHealthcheck());

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    static byte[] customBytes = new byte[256];
    static {
        for (int i = 0; i < customBytes.length; i++) {
            customBytes[i] = (byte) (i % 128); // Inizializza con valori specifici
        }
    }
    static MockMultipartFile empty_image = new MockMultipartFile("file", new byte[0]);
    static MockMultipartFile full_image = new MockMultipartFile("file", customBytes);

    static Cookie userCookie = new Cookie("loggedUser","CF123ABCZYX&Nome&Cognome&example99@email.com&2003-01-01&9988776655&2024-08-28&false&false&false&false&false&false&null");

    static Cookie elenaCooke = new Cookie("loggedUser", "CF67890123456789&Elena&Martini&elena@example.com&1982-12-18&3456789012&2023-08-19&true&false&true&true&false&false&elena_martini");

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
    @Order(1)
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
    @DisplayName("Testa se la pagina di modifica dei dati viene caricata correttamente")
    @Tag("integration")
    @Order(2)
    public void integrationEditProfilePageTest() throws Exception {

        this.mockMvc.perform(get("/updateProfile")
                        .cookie(userCookie)
                )
                .andDo(print())
                .andExpect(view().name("updatePage"))
                .andExpect(model().attributeExists("utente"));
    }

    private static Stream<Arguments> caricaArgomenti() {
        return Stream.of(
                Arguments.of(full_image),
                Arguments.of(empty_image)
        );
    }
    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @DisplayName("Testa se è possibile modificare i dati dell'utente")
    @Tag("integration")
    @Order(3)
    public void integrationEditProfileTest(MockMultipartFile image) throws Exception {

        this.mockMvc.perform(multipart("/updateProfile")
                        .file(image)
                        .cookie(elenaCooke)
                        .param("email", "example99@email.com")
                        .param("telefono", "9988776655")
                        .param("username", "username")
                )
                .andDo(print())
                .andExpect(view().name("profilePage"))
                .andExpect(model().attributeExists("utente"))
                .andExpect(model().attributeExists("imgPath"))
                .andExpect(model().attribute("imgPath", "profile/profileImg.jpg"))
                .andExpect(cookie().exists("loggedUser"));
    }

    @Test
    @DisplayName("Testa se la pagina di modifica della password viene visualizzata correttamente")
    @Tag("integration")
    @Order(4)
    public void integrationEditPasswordPageTest() throws Exception {

        this.mockMvc.perform(get("/changePassword")
                        .cookie(userCookie)
                )
                .andDo(print())
                .andExpect(view().name("updatePasswordPage"))
                .andExpect(model().attributeExists("errorCode"))
                .andExpect(model().attribute("errorCode", 0));
    }

    @Test
    @DisplayName("Testa se la password viene aggiornata correttamente")
    @Tag("integration")
    @Order(5)
    public void integrationEditPasswordTest() throws Exception {

        this.mockMvc.perform(post("/changePassword")
                        .cookie(userCookie)
                        .param("oldPassword", "password")
                        .param("newPassword", "newpassword")
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attributeExists("user"));
    }


    @Test
    @DisplayName("Testa se la cancellazione dell'utente avviene correttamente")
    @Tag("integration")
    @Order(6)
    public void integrationDeleteProfileTest() throws Exception {

        this.mockMvc.perform(get("/deleteProfile")
                        .cookie(userCookie)
                )
                .andDo(print())
                .andExpect(view().name("redirect_to"))
                .andExpect(model().attributeExists("user"))
                .andExpect(cookie().maxAge("loggedUser", 0));
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
