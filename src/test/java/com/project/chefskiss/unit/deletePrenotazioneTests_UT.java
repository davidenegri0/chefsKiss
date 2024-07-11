package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.deletePrenotazioneController;
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

@WebMvcTest(deletePrenotazioneController.class)
public class deletePrenotazioneTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PrenotazioneDAO sessionPrenotazioneDAO_mock;

    @InjectMocks
    private deletePrenotazioneController controller;
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
    void invalid_deletePrenotazione_test() {
        // Arrange
        String cookie = "";

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeletePrenotazione(cookie, 99);
        assertEquals("index", page.getViewName());

        System.out.println("Viewname index corretto");
        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void deletePrenotazione_test() {
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        Prenotazione pre = new Prenotazione();
        pre.setId(99);
        pre.setUtenteP(User.decodeUserData(cookie));

        // Mock methods

        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionPrenotazioneDAO_mock.findById(99)).thenReturn(pre);

        // Act & Assert
        System.out.println("Avvio test");

        ModelAndView page = controller.DeletePrenotazione(cookie, 99);
        assertAll("Test deletePrenotazione",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertTrue(page.getModel().get("url").toString().contains("/prenotazioniList?id=CF12345678901234"))
            );

        System.out.println("Viewname corretto, redirezione alla pagina corretta");
        System.out.println("Test passato");
    }
}