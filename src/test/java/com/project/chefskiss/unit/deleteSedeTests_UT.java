package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.deleteSedeController;
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

@WebMvcTest(deleteSedeController.class)
public class deleteSedeTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;
    @Mock
    private ValutazioneDAO sessionValutazioneDAO_mock;
    @Mock
    private PrenotazioneDAO sessionPrenotazioneDAO_mock;

    @InjectMocks
    private deleteSedeController controller;
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
    void invalid_deleteSede_test() {
        // Arrange
        String cookie = "";

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.deleteSede(cookie, "123.456;789.123", 99);
        assertEquals("index", page.getViewName());

        System.out.println("Viewname index corretto");
        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void deleteSede_test() {
        // Arrange
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String coord = "123.456;789.123";
        int idR = 99;

        Sede sede = new Sede();
        sede.setCoordinate(coord);

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(null)).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(null)).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(sessionPrenotazioneDAO_mock);

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.deleteSede(cookie, coord, idR);
        assertAll("Test deleteSede",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertTrue(page.getModel().get("url").toString().contains("/restaurant?id="+idR))
        );

        System.out.println("Viewname corretto, redirezione alla pagina corretta: " + page.getModel().get("url").toString());
        System.out.println("Test passato");
    }
}