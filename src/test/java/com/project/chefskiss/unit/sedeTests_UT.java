package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.sedeController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(sedeController.class)
public class sedeTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private SedeDAO session_sedeDAO_mock;
    @Mock
    private RistoranteDAO session_RistoDAO_mock;
    @Mock
    private UserDAO session_UserDAO_mock;
    @Mock
    private PiattoDAO session_PiattoDAO_mock;
    @Mock
    private ValutazioneDAO session_Valutazione_DAO_mock;


    @InjectMocks
    private sedeController controller;
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


    private static Stream<Arguments> caricaArgomenti() {
        return Stream.of(
                Arguments.of("CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi"),
                Arguments.of("")
        );
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void view_sede_test(String cookie){

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_sedeDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);
        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getValutazioneDAO(any())).thenReturn(session_Valutazione_DAO_mock);

        Sede sede = new Sede();
        sede.setCoordinate("123.456:789.012");
        sede.setCitta("Sede di prova");
        sede.setVia("Via di prova, 123");
        sede.setID_Ristorante(1);

        Ristorante risto = new Ristorante();
        risto.setID(1);
        risto.setNome("Ristorante di prova");

        sede.setRistoranteS(risto);

        User chef = new User();
        chef.setCF("ABCDEF12G34H567I");
        chef.setNome("Chef di prova");

        List<User> chefs = List.of(chef);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Piatto di prova 1");

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Piatto di prova 2");

        List<Piatto> piatti = List.of(piatto1, piatto2);

        Valutazione valutazione1 = new Valutazione();
        valutazione1.setVoto(5);
        valutazione1.setUtenteV(new User());
        valutazione1.getUtenteV().setCF("CF1");

        Valutazione valutazione2 = new Valutazione();
        valutazione2.setVoto(4);
        valutazione2.setUtenteV(new User());
        valutazione2.getUtenteV().setCF("CF2");

        List<Valutazione> valutazioni = List.of(valutazione1, valutazione2);

        User recensore1 = new User();
        recensore1.setCF("CF1");
        recensore1.setNome("Recensore 1");

        User recensore2 = new User();
        recensore2.setCF("CF2");
        recensore2.setNome("Recensore 2");

        // Mocking delle chiamate ai DAO
        when(session_sedeDAO_mock.findByCoordinate("123.456:789.012")).thenReturn(sede);
        when(session_RistoDAO_mock.findById(1)).thenReturn(risto);
        when(session_UserDAO_mock.findBySede(sede)).thenReturn(chefs);
        when(session_PiattoDAO_mock.findBySede(sede)).thenReturn(piatti);
        when(session_Valutazione_DAO_mock.findBySede(sede)).thenReturn(valutazioni);
        when(session_UserDAO_mock.findByCF1("CF1")).thenReturn(recensore1);
        when(session_UserDAO_mock.findByCF1("CF2")).thenReturn(recensore2);

        System.out.println("Avvio test: view_sede_test");

        ModelAndView page = controller.onSedeViewRequest("123.456:789.012", cookie);

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("sedePage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(sede, page.getModel().get("sede")),
                    () -> assertEquals(chefs, page.getModel().get("chefs")),
                    () -> assertEquals(piatti, page.getModel().get("piatti")),
                    () -> assertEquals(valutazioni, page.getModel().get("valutazioni"))
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("sedePage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals("Mario", ((User) page.getModel().get("user")).getNome()),
                    () -> assertEquals(sede, page.getModel().get("sede")),
                    () -> assertEquals(chefs, page.getModel().get("chefs")),
                    () -> assertEquals(piatti, page.getModel().get("piatti")),
                    () -> assertEquals(valutazioni, page.getModel().get("valutazioni"))
            );
        }

        System.out.println("Pagina reindirizzata correttamente, utente presente, oggetti passati corretti");
        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_view_onDeleteChefRequest_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_view_onDeleteChefRequest_test");

        ModelAndView page = controller.onDeleteChefRequest("CF1", "123.456:789.012", cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_onDeleteChefRequest_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User chef = new User();
        chef.setCF("CF");
        chef.setNome("Chef di prova");

        // Mocking delle chiamate ai DAO
        when(session_UserDAO_mock.findByCF("CF")).thenReturn(chef);
        //doThrow(new RuntimeException()).when(session_UserDAO_mock).update(chef);
        //TODO: decommentare riga sopra se si vuole testare anche eccezione

        System.out.println("Avvio test: view_onDeleteChefRequest_test");

        // Call method
        ModelAndView page = controller.onDeleteChefRequest(chef.getCF(), "123.456:789.012", cookie);
        verify(session_UserDAO_mock).update(chef);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user"))
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_view_onAddChefRequest_with_ID_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_view_onAddChefRequest_with_ID_test");

        ModelAndView page = controller.onAddChefRequest("123.456:789.012", cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_onAddChefRequest_with_ID_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String id = "123.456:789.012";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User chef1 = new User();
        chef1.setCF("CF1");
        chef1.setNome("Chef di prova 1");

        User chef2 = new User();
        chef2.setCF("CF2");
        chef1.setNome("Chef di prova 2");

        List<User> chefs = List.of(chef1, chef2);

        when(session_UserDAO_mock.getAllFreeChefs()).thenReturn(chefs);

        System.out.println("Avvio test: view_onAddChefRequest_with_ID_test");

        // Call method
        ModelAndView page = controller.onAddChefRequest(id, cookie);

        // Assertions
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("addChefPage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals(id, page.getModel().get("CoordSede")),
                () -> assertEquals(chefs, page.getModel().get("chefs"))
        );

        // Verify method calls
        verify(DatabaseDAO_mock).beginTransaction();
        verify(session_UserDAO_mock).getAllFreeChefs();
        verify(DatabaseDAO_mock).closeTransaction();

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void invalid_view_onAddChefRequest_with_Coord_CF_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_view_onAddChefRequest_with_Coord_CF_test");

        ModelAndView page = controller.onAddChefRequest("123.456:789.012", "CF", cookie);
        assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void view_onAddChefRequest_with_Coord_CF_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        String Coord = "123.456;789.012";
        String CF = "CF";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(session_UserDAO_mock);

        User chef = new User();
        chef.setCF("CF");
        chef.setNome("Chef di prova");

        Sede sede = new Sede();
        sede.setCoordinate(Coord);

        chef.setSedeU(sede);

        when(session_UserDAO_mock.findByCF(CF)).thenReturn(chef);

        System.out.println("Avvio test: view_onAddChefRequest_with_Coord_CF_test");

        // Call method
        ModelAndView page = controller.onAddChefRequest(Coord, CF, cookie);
        verify(session_UserDAO_mock).update(chef);
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user"))
        );

        System.out.println("Test passato");
    }
}
