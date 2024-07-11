package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.editSedeController;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(editSedeController.class)
public class editSedeTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;
    @Mock
    private RistoranteDAO sessionRistoranteDAO_mock;

    @InjectMocks
    private editSedeController controller;
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
    void viewSede_validUserAndSede_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.012");
        sede.setID_Ristorante(99);

        Ristorante ristorante = new Ristorante();
        ristorante.setNome("Ristorante di prova");
        ristorante.setID(99);

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(null)).thenReturn(sessionRistoranteDAO_mock);
        when(sessionSedeDAO_mock.findByCoordinate("123.456;789.012")).thenReturn(sede);
        when(sessionRistoranteDAO_mock.findById(99)).thenReturn(ristorante);

        // Act
        ModelAndView returnPage = controller.deleteSede(cookie, "123.456;789.012");

        // Assert
        assertEquals("editSedePage", returnPage.getViewName(), "View name should be 'editSedePage'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertNotNull(returnPage.getModel().get("sede"), "Sede should not be null");
        assertNotNull(returnPage.getModel().get("ristorante"), "Ristorante should not be null");
    }

    @Test
    @Tag("unit")
    void postAddSede_validUserAndSede_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setNome("Mario");
        mario.setCognome("Rossi");

        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.012");
        sede.setID_Ristorante(99);

        // Mock methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);

        // Act
        ModelAndView returnPage = controller.postAddSede(cookie, "Via di prova", "Città di prova", 10, "123.456;789.012", 99);

        // Assert
        assertEquals("redirect_to", returnPage.getViewName(), "View name should be 'redirect_to'");
        assertNotNull(returnPage.getModel().get("user"), "User should not be null");
        assertTrue(returnPage.getModel().get("url").toString().contains("/restaurant?id=99"), "Should redirect to restaurant page");
    }

    @Test
    @Tag("unit")
    void deleteSede_invalidUser_test() {
        // Mock data
        String cookie = "";

        // Act
        ModelAndView returnPage = controller.deleteSede(cookie, "123.456;789.012");

        // Assert
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
    }

    @Test
    @Tag("unit")
    void postAddSede_invalidUser_test() {
        // Mock data
        String cookie = "";

        // Act
        ModelAndView returnPage = controller.postAddSede(cookie, "Via di prova", "Città di prova", 10, "123.456;789.012", 99);

        // Assert
        assertEquals("index", returnPage.getViewName(), "View name should be 'index'");
    }
}