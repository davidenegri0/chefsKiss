package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.editPrenotazioneController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(editPrenotazioneController.class)
public class editPrenotazioneTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PrenotazioneDAO sessionPrenotazioneDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;

    @InjectMocks
    private editPrenotazioneController controller;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }

    @AfterEach
    public void tearDown() {
        dao_factory_mock.close();
    }

    @Test
    @Tag("unit")
    void viewModifyPrenotazione_validUserAndPrenotazione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(99);
        prenotazione.setUtenteP(mario);

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.012");

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(sessionPrenotazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(sessionPrenotazioneDAO_mock.findById(99)).thenReturn(prenotazione);
        when(sessionSedeDAO_mock.findByCoordinate("123.456;789.012")).thenReturn(sede);

        // Act
        ModelAndView returnPage = controller.viewModifyPrenotazione(cookie, 99, "123.456;789.012");

        // Assert
        assertEquals("prenotazionePage", returnPage.getViewName(), "View name should be 'prenotazionePage'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertNotNull(returnPage.getModel().get("prenotazione"), "Prenotazione should not be null");
        assertNotNull(returnPage.getModel().get("sede"), "Sede should not be null");
        assertEquals((int) returnPage.getModel().get("type"), 2, "Type should be 2");
    }

    @Test
    @Tag("unit")
    void postModifyPrenotazione_validUserAndPrenotazione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(99);
        prenotazione.setUtenteP(mario);

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.012");

        Date data = new Date(System.currentTimeMillis());
        Time orario = new Time(System.currentTimeMillis());
        Integer n_posti = 2;

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(sessionPrenotazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(sessionPrenotazioneDAO_mock.isPrenotazioneUp(mario.getCF(), data, Time.valueOf("12:00:00"))).thenReturn(true);
        when(sessionSedeDAO_mock.findByCoordinate("123.456;789.012")).thenReturn(sede);

        // Act
        ModelAndView returnPage = controller.postModifyPrenotazione(cookie, 99, "123.456;789.012", data, "12:00", n_posti);

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertTrue(returnPage.getModel().get("url").toString().contains("/prenotazioniList?id=99"), "Should redirect to prenotazioniList");
    }

    @Test
    @Tag("unit")
    void viewModifyPrenotazione_invalidUser_test() {
        // Mock data
        String cookie = "";

        // Act
        ModelAndView returnPage = controller.viewModifyPrenotazione(cookie, 99, "123.456;789.012");

        // Assert
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
    }

    @Test
    @Tag("unit")
    void postModifyPrenotazione_invalidUser_test() {
        // Mock data
        String cookie = "";
        Date data = new Date(System.currentTimeMillis());
        Integer n_posti = 2;

        // Act
        ModelAndView returnPage = controller.postModifyPrenotazione(cookie, 99, "123.456;789.012", data, "12:00", n_posti);

        // Assert
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
    }

    @Test
    @Tag("unit")
    void postModifyPrenotazione_invalidPrenotazione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.012");

        Date data = new Date(System.currentTimeMillis());
        Time orario = new Time(System.currentTimeMillis());
        Integer n_posti = 2;

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(sessionPrenotazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(sessionPrenotazioneDAO_mock.isPrenotazioneUp(mario.getCF(), data, Time.valueOf("12:00:00"))).thenReturn(false);
        when(sessionSedeDAO_mock.findByCoordinate("123.456;789.012")).thenReturn(sede);

        // Act
        ModelAndView returnPage = controller.postModifyPrenotazione(cookie, 99, "123.456;789.012", data, "12:00", n_posti);

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertTrue(returnPage.getModel().get("url").toString().contains("/prenotazioniList?id=99&error=1"), "Should redirect to prenotazioniList with error");
    }
}