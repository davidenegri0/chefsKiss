package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.prenotazioniListController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.Prenotazione;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(prenotazioniListController.class)
public class prenotazioniListTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PrenotazioneDAO session_PrenotazioneDAO_mock;


    @InjectMocks
    private prenotazioniListController controller;
    private MockedStatic<DAOFactory> dao_factory_mock;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        dao_factory_mock = Mockito.mockStatic(DAOFactory.class);
    }

    @AfterEach
    public void tearDown() {
        dao_factory_mock.close();
    }


    @Test
    @Tag("unit")
    void invalid_viewPrenotazioniList_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_viewPrenotazioniList_test");

        ModelAndView page = controller.viewPrenotazioniList(cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName()),
                () -> assertNull(page.getModel().get("user"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void valid_viewPrenotazioniList_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPrenotazioneDAO(null)).thenReturn(session_PrenotazioneDAO_mock);

        Prenotazione prenotazione1 = new Prenotazione();
        List<Prenotazione> prenotazioni = List.of(prenotazione1);

        when(session_PrenotazioneDAO_mock.findByUser(any())).thenReturn(prenotazioni);
        System.out.println("Avvio Test: valid_viewPrenotazioniList_test");

        ModelAndView page = controller.viewPrenotazioniList(cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("prenotazioniListPage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals(prenotazioni, page.getModel().get("prenotazioni"))

        );

        System.out.println("Test passato");
    }
}
