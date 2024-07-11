package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.deletePlateController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(deletePlateController.class)
public class deletePlateTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PiattoDAO sessionPiattoDAO_mock;
    @Mock
    private ContieneDAO sessionContieneDAO_mock;
    @Mock
    private RecensioneDAO sessionRecensioneDAO_mock;

    @InjectMocks
    private deletePlateController controller;
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

    @ParameterizedTest
    @Tag("unit")
    @ValueSource(strings = {"", "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi"})
    void invalid_deletePlate_test(String cookie) {
        // Arrange
        // Mock data
        User mario = new User();
        mario.setCF("CF99999999999999");
        mario.setNome("NonMario");
        mario.setCognome("NonRossi");

        Piatto piatto = new Piatto();
        piatto.setID(99);
        piatto.setNome("Piatto di prova");
        piatto.setUtenteP(mario);

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);

        when(sessionPiattoDAO_mock.findByIDPiatto(99)).thenReturn(piatto);

        // Act & Assert
        System.out.println("Test avviato");

        ModelAndView returnPage = controller.onDeletePlateRequest(cookie, 99);
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
        if (!cookie.isEmpty()) assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        else assertNull(returnPage.getModel().get("user"), "User should be null");

        System.out.println("Viewname corretto");
        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void deletePlate_test() {
        // Arrange
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

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(null)).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getContieneDAO(null)).thenReturn(sessionContieneDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(null)).thenReturn(sessionRecensioneDAO_mock);

        when(sessionPiattoDAO_mock.findByIDPiatto(99)).thenReturn(piatto);

        // Act & Assert
        System.out.println("Test avviato");

        ModelAndView returnPage = controller.onDeletePlateRequest(cookie, 99);
        assertAll("Delete page check",
                () -> assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'"),
                () -> assertNotNull(returnPage.getModel().get("user"), "User should not be null"),
                () -> assertTrue(returnPage.getModel().get("url").toString().contains("/recipesView"), "page should redirect to '/recipesView'")
        );

        System.out.println("Viewname corretto, utente non nullo, redirect corretto");
        System.out.println("Test passato");
    }
}