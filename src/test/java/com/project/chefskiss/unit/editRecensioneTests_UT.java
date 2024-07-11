package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.editRecensioneController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(editRecensioneController.class)
public class editRecensioneTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RecensioneDAO sessionRecensioneDAO_mock;
    @Mock
    private ValutazioneDAO sessionValutazioneDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;
    @Mock
    private PiattoDAO sessionPiattoDAO_mock;
    @Mock
    private UserDAO sessionUserDAO_mock;

    @InjectMocks
    private editRecensioneController controller;
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
    void viewModifyRecensione_validUserAndRecensione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Piatto piatto = new Piatto();
        piatto.setID(99);
        piatto.setNome("Piatto di prova");
        piatto.setUtenteP(mario);

        Recensione recensione = new Recensione();
        recensione.setPiattoR(piatto);
        recensione.setUtenteR(mario);

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(null)).thenReturn(sessionUserDAO_mock);

        when(sessionRecensioneDAO_mock.checkRecensione(mario.getCF(), piatto.getId())).thenReturn(true);
        when(sessionRecensioneDAO_mock.findByPiatto_Utente(piatto.getId(), mario.getCF())).thenReturn(recensione);
        when(sessionPiattoDAO_mock.findByIDPiatto(piatto.getId())).thenReturn(piatto);
        when(sessionUserDAO_mock.findByCF(mario.getCF())).thenReturn(mario);

        // Act
        ModelAndView returnPage = controller.viewModifyRecensione(cookie, 3, "99");

        // Assert
        assertEquals("addRecensionePage", returnPage.getViewName(), "View name should be 'addRecensionePage'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertNotNull(returnPage.getModel().get("piatto"), "Piatto should not be null");
        assertNotNull(returnPage.getModel().get("recensione"), "Recensione should not be null");
    }

    @Test
    @Tag("unit")
    void viewModifyValutazione_validUserAndValutazione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.0123");

        Valutazione valutazione = new Valutazione();

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(null)).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);

        when(sessionValutazioneDAO_mock.checkValutazione(mario.getCF(), sede.getCoordinate())).thenReturn(true);
        when(sessionSedeDAO_mock.findByCoordinate(sede.getCoordinate())).thenReturn(sede);
        when(sessionValutazioneDAO_mock.findByCF_Coordinate(mario.getCF(), sede.getCoordinate())).thenReturn(valutazione);
        when(sessionUserDAO_mock.findByCF(mario.getCF())).thenReturn(mario);

        // Act
        ModelAndView returnPage = controller.viewModifyRecensione(cookie, 4, "123.456;789.0123");

        // Assert
        assertEquals("addRecensionePage", returnPage.getViewName(), "View name should be 'addRecensionePage'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertNotNull(returnPage.getModel().get("sede"), "Sede should not be null");
        assertNotNull(returnPage.getModel().get("valutazione"), "Valutazione should not be null");
    }

    @Test
    @Tag("unit")
    void postModifyRecensione_validUserAndRecensione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Piatto piatto = new Piatto();
        piatto.setID(99);
        piatto.setNome("Piatto di prova");
        piatto.setUtenteP(mario);

        Recensione recensione = new Recensione();
        recensione.setPiattoR(piatto);
        recensione.setUtenteR(mario);

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);

        when(sessionPiattoDAO_mock.findByIDPiatto(99)).thenReturn(piatto);
        when(sessionRecensioneDAO_mock.checkRecensione(mario.getCF(), piatto.getId())).thenReturn(true);
        when(sessionRecensioneDAO_mock.findByPiatto_Utente(piatto.getId(), mario.getCF())).thenReturn(recensione);

        // Act
        ModelAndView returnPage = controller.postModifyRecensione(cookie, 3, "99", 5, "Great!");

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertTrue(returnPage.getModel().get("url").toString().contains("/plate?id=99"), "Should redirect to plate page");
    }

    @Test
    @Tag("unit")
    void postModifyValutazione_validUserAndValutazione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.0123");

        Valutazione valutazione = new Valutazione();
        valutazione.setUtenteV(mario);
        valutazione.setSedeV(sede);

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate("123.456;789.0123")).thenReturn(sede);

        // Act
        ModelAndView returnPage = controller.postModifyRecensione(cookie, 4, "123.456;789.0123", 5, "Great!");

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertTrue(returnPage.getModel().get("url").toString().contains("/sede?id=123.456;789.0123"), "Should redirect to sede page");
    }

    @Test
    @Tag("unit")
    void viewModifyRecensione_invalidUser_test() {
        // Mock data
        String cookie = "";

        // Act
        ModelAndView returnPage = controller.viewModifyRecensione(cookie, 3, "99");

        // Assert
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
    }

    @Test
    @Tag("unit")
    void postModifyRecensione_invalidUser_test() {
        // Mock data
        String cookie = "";

        // Act
        ModelAndView returnPage = controller.postModifyRecensione(cookie, 3, "99", 5, "Great!");

        // Assert
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
    }

    @Test
    @Tag("unit")
    void viewModifyRecensione_invalidRecensione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(sessionRecensioneDAO_mock.checkRecensione(anyString(), anyInt())).thenReturn(false);

        // Act
        ModelAndView returnPage = controller.viewModifyRecensione(cookie, 3, "99");

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertTrue(returnPage.getModel().get("url").toString().contains("/plate?id=99&error=2"), "Should redirect to plate page with error");
    }

    @Test
    @Tag("unit")
    void viewModifyValutazione_invalidValutazione_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.0123");

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(null)).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);

        when(sessionValutazioneDAO_mock.checkValutazione(mario.getCF(), sede.getCoordinate())).thenReturn(false);


        // Act
        ModelAndView returnPage = controller.viewModifyRecensione(cookie, 4, "123.456;789.0123");

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertTrue(returnPage.getModel().get("url").toString().contains("/sede?id=123.456;789.0123&error=2"), "Valutazione should not be null");
    }
}