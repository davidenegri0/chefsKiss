package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.addSedeController;
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

@WebMvcTest(addSedeController.class)
public class addSedeTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RistoranteDAO sessionRistoDAO_mock;
    @Mock
    private SedeDAO sessionSedeDAO_mock;
    @Mock
    private UserDAO sessionUserDAO_mock;
    @InjectMocks
    private addSedeController controller;
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
    @ValueSource(strings = {"", "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&false&false&mario_rossi"})
    void invalid_view_addRistoPage_test(String cookie) {

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRisto(cookie);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        if (!cookie.isEmpty()) System.out.println("Utente non ristoratore, redirezione corretta");
        else System.out.println("Cookie vuoto, redirezione corretta");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_view_addSedePage_test() {

        // Mock data
        String cookie = "";

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddSede(
                cookie,
                "ViaDiProva",
                1,
                "CittaImportante",
                10,
                99,
                "123.456;789.123");
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_post_addRistoPage_test() {

        // Mock data
        String cookie = "";

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddRisto(
                cookie,
                "RistoDiProva",
                "ViaDiProva",
                1,
                "CittaImportante",
                99,
                "123.456;789.123");
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_post_addSedePage_test() {

        // Mock data
        String cookie = "";

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddSede(cookie);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_addRistoPage_test() {
        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddRisto(cookie);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("addRistoPage", page.getViewName()),
                () -> assertInstanceOf(User.class, page.getModel().get("user"))
        );

        System.out.println("Viewname corretto, utente presente nella pagina caricata");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void post_addRistoPage_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        Ristorante risto = new Ristorante();
        risto.setID(99);
        Sede sede = new Sede();

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(sessionRistoDAO_mock);

        when(sessionRistoDAO_mock.create(anyString(), anyString())).thenReturn(risto);
        when(sessionSedeDAO_mock.create(anyString(), anyString(), anyString(), anyInt(), any(), anyList())).thenReturn(sede);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddRisto(
                cookie,
                "RistoDiProva",
                "ViaDiProva",
                1,
                "CittaImportante",
                99,
                "123.456;789.123"
        );
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertTrue(((String)page.getModel().get("url")).contains("/restaurant?id=99"))
        );

        System.out.println("Viewname corretto, utente presente nella pagina caricata e redirezione corretta");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_addSedePage_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        Ristorante risto = new Ristorante();
        risto.setID(1);
        risto.setNome("ristorante1");

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(sessionRistoDAO_mock);

        when(sessionRistoDAO_mock.findByRistoratore(anyString())).thenReturn(risto);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.viewAddSede(cookie);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("addSedePage", page.getViewName()),
                () -> assertInstanceOf(User.class, page.getModel().get("user")),
                () -> assertInstanceOf(Ristorante.class, page.getModel().get("Risto"))
        );

        System.out.println("Viewname corretto, utente e ristorante presenti nella pagina caricata");

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void post_addSedePage_test() {

        // Mock data
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
/*
        User mario = new User();
        mario.setCF("CF12345678901234");
        mario.setPrivileges(true, false, true, true, true);
*/
        Ristorante risto = new Ristorante();
        Sede sede = new Sede();

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(sessionSedeDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(sessionRistoDAO_mock);

        when(sessionRistoDAO_mock.findById(anyInt())).thenReturn(risto);
        when(sessionSedeDAO_mock.create(anyString(), anyString(), anyString(), anyInt(), any(), anyList())).thenReturn(sede);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddSede(
                cookie,
                "ViaDiProva",
                1,
                "CittaImportante",
                10,
                99,
                "123.456;789.123"
        );
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertTrue(((String)page.getModel().get("url")).contains("/restaurant?id=99"))
        );

        System.out.println("Viewname corretto, utente presente nella pagina caricata e redirezione corretta");

        System.out.println("Test passato");
    }
}