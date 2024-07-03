package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.addPrenotazioneController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(addPrenotazioneController.class)
public class addPrenotazioneTest_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PrenotazioneDAO sessionPrenotazioneDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;
    @InjectMocks
    private addPrenotazioneController controller;
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
    void invalid_view_addPrenotazionePage_test() {

        // Mock data
        String cookie = "";

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddPrenotazione(cookie, "123.456:789.012");
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }


    @Test
    @Tag("unit")
    void view_addPrenotazionePage_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");
        sede.setCitta("Sede di prova");
        sede.setVia("Via di prova, 123");
        sede.setID_Ristorante(1);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate(any())).thenReturn(sede);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddPrenotazione(cookie, "123.456:789.012");
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("prenotazionePage", page.getViewName()),
                () -> assertInstanceOf(User.class, page.getModel().get("user")),
                () -> assertInstanceOf(Sede.class, page.getModel().get("sede")),
                () -> assertEquals(1, page.getModel().get("type"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void post_addPrenotazionePage_test_valid() {

        // Mock data
        Prenotazione prenotazione = new Prenotazione();
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");
        sede.setCitta("Sede di prova");
        sede.setVia("Via di prova, 123");
        sede.setID_Ristorante(1);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate(any())).thenReturn(sede);
        when(sessionPrenotazioneDAO_mock.create(any(), any(), any(), any(), anyInt())).thenReturn(prenotazione);
        when(sessionPrenotazioneDAO_mock.verifica_posti_disponibili(anyString(), any(), any())).thenReturn(5);
        when(sessionPrenotazioneDAO_mock.isPrenotazioneUp(anyString(), any(), any())).thenReturn(true);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postPrenotazione(
                cookie,
                "123.456:789.012",
                "validCF",
                Date.valueOf("2022-12-31"),
                "12:00:00",
                1
        );
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertNull(page.getModel().get("errorCode"))
            );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void post_addPrenotazionePage_test_already_up() {

        // Mock data
        Prenotazione prenotazione = new Prenotazione();
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");
        sede.setCitta("Sede di prova");
        sede.setVia("Via di prova, 123");
        sede.setID_Ristorante(1);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate(any())).thenReturn(sede);
        when(sessionPrenotazioneDAO_mock.create(any(), any(), any(), any(), anyInt())).thenReturn(prenotazione);
        when(sessionPrenotazioneDAO_mock.verifica_posti_disponibili(anyString(), any(), any())).thenReturn(5);
        when(sessionPrenotazioneDAO_mock.isPrenotazioneUp(anyString(), any(), any())).thenReturn(false);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postPrenotazione(
                cookie,
                "123.456:789.012",
                "validCF",
                Date.valueOf("2022-12-31"),
                "12:00:00",
                1
        );
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals(4, page.getModel().get("errorCode"))
        );

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @CsvSource({
            "'', 1, index, 0",
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi, 10, redirect_to, 4",
    })
    @Tag("unit")
    void post_addPrenotazionePage_test_invalid(String cookie, int n_posti, String pageName, int errorCode) {

        // Mock data
        Prenotazione prenotazione = new Prenotazione();

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");
        sede.setCitta("Sede di prova");
        sede.setVia("Via di prova, 123");
        sede.setID_Ristorante(1);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate(any())).thenReturn(sede);
        when(sessionPrenotazioneDAO_mock.create(any(), any(), any(), any(), anyInt())).thenReturn(prenotazione);
        when(sessionPrenotazioneDAO_mock.verifica_posti_disponibili(anyString(), any(), any())).thenReturn(5);
        when(sessionPrenotazioneDAO_mock.isPrenotazioneUp(anyString(), any(), any())).thenReturn(true);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postPrenotazione(
                cookie,
                "123.456:789.012",
                "validCF",
                Date.valueOf("2022-12-31"),
                "12:00:00",
                n_posti
        );

        if (errorCode==0) {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals(pageName, page.getViewName()),
                    () -> assertNull(page.getModel().get("errorCode"))
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals(pageName, page.getViewName()),
                    () -> assertEquals(errorCode, page.getModel().get("errorCode"))
            );
        }

        System.out.println("Test passato");
    }
}