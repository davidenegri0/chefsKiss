package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.addRecensioneController;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(addRecensioneController.class)
public class addRecensioneTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RecensioneDAO sessionRecensioneDAO_mock;
    @Mock
    private ValutazioneDAO sessionValutazioneDAO_mock;
    @Mock
    private PrenotazioneDAO sessionPrenotazioneDAO_mock;
    @Mock
    private UserDAO sessionUserDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;
    @Mock
    private PiattoDAO sessionPiattoDAO_mock;
    @InjectMocks
    private addRecensioneController controller;
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
        ModelAndView page = controller.viewAddRecensione(cookie, 1, "1");
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_post_addPrenotazionePage_test() {

        // Mock data
        String cookie = "";

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postRecensione(cookie, 1, "1", 5, "validComment");
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_addRecensionePage_RecensioneIsUp_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        Integer typeCode = 1;
        String id = "1";

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionRecensioneDAO_mock.checkRecensione(anyString(), anyInt())).thenReturn(true);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRecensione(cookie, typeCode, id);
        String redirection_url = (String) page.getModel().get("url");

        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertTrue(redirection_url.contains("error=1") && redirection_url.contains("id="+id))
        );

        System.out.println("Pagina reindirizzata correttamente, utente presente, errorCode corretto");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_addRecensionePage_ValutazioneIsUp_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        Integer typeCode = 2;
        String id = "123.456:789.012";

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionValutazioneDAO_mock.checkValutazione(anyString(), anyString())).thenReturn(true);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRecensione(cookie, typeCode, id);
        String redirection_url = (String) page.getModel().get("url");

        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertNotNull(page.getModel().get("errorCode")),
                () -> assertTrue(redirection_url.contains("error=1") && redirection_url.contains("id="+id))
        );

        System.out.println("Pagina reindirizzata correttamente, utente presente, errorCode corretto");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_addRecensionePage_PrenotazioneIsNotUp_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        Integer typeCode = 2;
        String id = "123.456:789.012";

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);

        when(sessionValutazioneDAO_mock.checkValutazione(anyString(), anyString())).thenReturn(false);
        when(sessionPrenotazioneDAO_mock.checkPrenotazione(anyString())).thenReturn(false);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRecensione(cookie, typeCode, id);
        String redirection_url = (String) page.getModel().get("url");

        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertNotNull(page.getModel().get("errorCode")),
                () -> assertTrue(redirection_url.contains("error=4") && redirection_url.contains("id="+id))
        );

        System.out.println("Pagina reindirizzata correttamente, utente presente, errorCode corretto");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_addRecensionePage_wrongTypeCode_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id = "1";
        Integer typeCode = 3;

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);

        when(sessionPiattoDAO_mock.findByIDPiatto(anyInt())).thenReturn(piatto);
        when(sessionUserDAO_mock.findByCF(anyString())).thenReturn(utente);
        when(sessionSedeDAO_mock.findByCoordinate(anyString())).thenReturn(sede);

        when(sessionRecensioneDAO_mock.checkRecensione(anyString(), anyInt())).thenReturn(false);
        when(sessionValutazioneDAO_mock.checkValutazione(anyString(), anyString())).thenReturn(false);
        when(sessionPrenotazioneDAO_mock.checkPrenotazione(anyString())).thenReturn(true);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRecensione(cookie, typeCode, id);
        assertEquals("index", page.getViewName());

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void post_addRecensionePage_wrongTypeCode_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id = "1";
        Integer typeCode = 3;

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate(any())).thenReturn(sede);
        when(sessionPiattoDAO_mock.findByIDPiatto(anyInt())).thenReturn(piatto);
        when(sessionRecensioneDAO_mock.create(any(), any(), anyInt(), anyString())).thenReturn(new Recensione());
        when(sessionValutazioneDAO_mock.create(any(), any(), anyInt())).thenReturn(new Valutazione());

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postRecensione(
                cookie,
                typeCode,
                id,
                5,
                "validComment"
        );

        assertEquals("index", page.getViewName());

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @Tag("unit")
    @ValueSource(ints = {1, 2})
    void view_addRecensionePage_test(Integer typeCode) {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id;
        if (typeCode == 1) {
            id = "1";
        }
        else {
            id = "123.456:789.012";
        }

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);
        when(DatabaseDAO_mock.getPrenotazioneDAO(any())).thenReturn(sessionPrenotazioneDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);

        when(sessionPiattoDAO_mock.findByIDPiatto(anyInt())).thenReturn(piatto);
        when(sessionUserDAO_mock.findByCF(anyString())).thenReturn(utente);
        when(sessionSedeDAO_mock.findByCoordinate(anyString())).thenReturn(sede);

        when(sessionRecensioneDAO_mock.checkRecensione(anyString(), anyInt())).thenReturn(false);
        when(sessionValutazioneDAO_mock.checkValutazione(anyString(), anyString())).thenReturn(false);
        when(sessionPrenotazioneDAO_mock.checkPrenotazione(anyString())).thenReturn(true);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRecensione(cookie, typeCode, id);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("addRecensionePage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals(typeCode, page.getModel().get("typecode"))
        );

        System.out.println("Pagina addRecensione caricata correttamente, utente presente, typecode corretto");

        if (typeCode == 1){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertNotNull(page.getModel().get("piatto")),
                    () -> assertNull(page.getModel().get("sede"))
            );

            System.out.println("Piatto presente, sede non presente");
        }
        else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertNotNull(page.getModel().get("sede")),
                    () -> assertNull(page.getModel().get("piatto"))
            );

            System.out.println("Sede presente, piatto non presente");
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @Tag("unit")
    void post_addRecensionePage_test(Integer typeCode) {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id;
        if (typeCode == 1) {
            id = "1";
        }
        else {
            id = "123.456:789.012";
        }

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");

        User utente = new User();
        utente.setCF("CF12345678901234");

        Piatto piatto = new Piatto();
        piatto.setUtenteP(utente);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getRecensioneDAO(any())).thenReturn(sessionRecensioneDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(sessionValutazioneDAO_mock);

        when(sessionSedeDAO_mock.findByCoordinate(any())).thenReturn(sede);
        when(sessionPiattoDAO_mock.findByIDPiatto(anyInt())).thenReturn(piatto);
        when(sessionRecensioneDAO_mock.create(any(), any(), anyInt(), anyString())).thenReturn(new Recensione());
        when(sessionValutazioneDAO_mock.create(any(), any(), anyInt())).thenReturn(new Valutazione());

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postRecensione(
                cookie,
                typeCode,
                id,
                5,
                "validComment"
        );
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertNull(page.getModel().get("errorCode"))
        );

        System.out.println("Pagina corretta, utente presente, nessun errore");

        String redirection_url = (String) page.getModel().get("url");
        if (typeCode == 1){
            assertTrue(redirection_url.contains("plate"));

            System.out.println("Piatto presente, sede non presente");
        }
        else {
            assertTrue(redirection_url.contains("sede"));

            System.out.println("Sede presente, piatto non presente");
        }

        System.out.println("Test passato");
    }
}