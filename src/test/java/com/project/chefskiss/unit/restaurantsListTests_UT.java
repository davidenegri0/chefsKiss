package com.project.chefskiss.unit;

import com.project.chefskiss.Comparators;
import com.project.chefskiss.controllers.resturantsListController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(resturantsListController.class)
public class restaurantsListTests_UT {

    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private RistoranteDAO session_RistoDAO_mock;
    @Mock
    private SedeDAO session_SedeDAO_mock;

    @InjectMocks
    private resturantsListController controller;
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
    void viewResturantsList_noOrd_test(String cookie){
        //String cookie = "";
        int order = 0;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("Ristorante di prova 1");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("Ristorante di prova 2");

        Sede sede1 = new Sede();
        sede1.setCoordinate("123.456:789.012");
        sede1.setCitta("Sede di prova 1");
        sede1.setVia("Via di prova, 123");
        sede1.setID_Ristorante(1);

        Sede sede2 = new Sede();
        sede2.setCoordinate("123.456:789.345");
        sede2.setCitta("Sede di prova 2");
        sede2.setVia("Via di prova, 134");
        sede2.setID_Ristorante(1);

        List<Ristorante> ristoranti = List.of(risto1, risto2);
        List<Sede> sedi = List.of(sede1, sede2);

        // Mocking delle chiamate ai DAO
        when(session_RistoDAO_mock.getAll()).thenReturn(ristoranti);
        when(session_SedeDAO_mock.getAll()).thenReturn(sedi);

        System.out.println("Avvio test: viewResturantsList_noOrd_test");

        ModelAndView page = controller.viewResturantsList(order, cookie);

        System.out.println("Avvio test: viewResturantsList_sortByVote_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi"))
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void viewResturantsList_ordByNome_test(String cookie){
        //String cookie = "";
        int order = 1;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A");

        Sede sede1 = new Sede();
        sede1.setID_Ristorante(1);

        Sede sede2 = new Sede();
        sede2.setID_Ristorante(1);

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto1);
        ristoranti.add(risto2);
        List<Sede> sedi = List.of(sede1, sede2);

        // Mocking delle chiamate ai DAO
        when(session_RistoDAO_mock.getAll()).thenReturn(ristoranti);
        when(session_SedeDAO_mock.getAll()).thenReturn(sedi);

        System.out.println("Avvio test: viewResturantsList_ordByNome_test");

        ModelAndView page = controller.viewResturantsList(order, cookie);

        System.out.println("Avvio test: viewResturantsList_sortByVote_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("B", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome()),
                    () -> assertEquals(sedi, page.getModel().get("sedi"))
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("B", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome()),
                    () -> assertEquals(sedi, page.getModel().get("sedi"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void viewResturantsList_sortByVote_test(String cookie){
        //String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";
        int order = 2;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Sede sede1 = new Sede();
        sede1.setVotoMedio(3.0f);

        Sede sede2 = new Sede();
        sede2.setVotoMedio(5.0f);

        List<Ristorante> ristoranti = List.of(new Ristorante());
        List<Sede> sedi = new ArrayList<>();
        sedi.add(sede1);
        sedi.add(sede2);

        when(session_RistoDAO_mock.getAll()).thenReturn(ristoranti);
        when(session_SedeDAO_mock.getAll()).thenReturn(sedi);

        ModelAndView page = controller.viewResturantsList(order, cookie);

        System.out.println("Avvio test: viewResturantsList_sortByVote_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(5.0f, ((List<Sede>) page.getModel().get("sedi")).get(0).getVotoMedio()),
                    () -> assertEquals(3.0f, ((List<Sede>) page.getModel().get("sedi")).get(1).getVotoMedio())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(5.0f, ((List<Sede>) page.getModel().get("sedi")).get(0).getVotoMedio()),
                    () -> assertEquals(3.0f, ((List<Sede>) page.getModel().get("sedi")).get(1).getVotoMedio())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_noOrd_byName_test(String cookie){
        int order = 0;
        int searchType = 1;
        String search = "A";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A2");

        Ristorante risto3 = new Ristorante();
        risto3.setID(3);
        risto3.setNome("A1");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto2);
        ristoranti.add(risto3);

        when(session_RistoDAO_mock.findByName(search)).thenReturn(ristoranti);

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_noOrd_byName_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_sortByName_byName_test(String cookie){
        int order = 1;
        int searchType = 1;
        String search = "A";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A2");

        Ristorante risto3 = new Ristorante();
        risto3.setID(3);
        risto3.setNome("A1");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto2);
        ristoranti.add(risto3);

        when(session_RistoDAO_mock.findByName(search)).thenReturn(ristoranti);

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_sortByName_byName_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_sortByVote_byName_test(String cookie){
        int order = 2;
        int searchType = 1;
        String search = "A";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A2");

        Ristorante risto3 = new Ristorante();
        risto3.setID(3);
        risto3.setNome("A1");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto2);
        ristoranti.add(risto3);

        Sede sede1 = new Sede();
        sede1.setVotoMedio(3.0f);

        Sede sede2 = new Sede();
        sede2.setVotoMedio(5.0f);

        List<Sede> sedi = new ArrayList<>();
        sedi.add(sede1);
        sedi.add(sede2);

        when(session_RistoDAO_mock.findByName(search)).thenReturn(ristoranti);
        when(session_SedeDAO_mock.getAll()).thenReturn(sedi);

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_sortByVote_byName_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome()),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(5.0f, ((List<Sede>) page.getModel().get("sedi")).get(0).getVotoMedio()),
                    () -> assertEquals(3.0f, ((List<Sede>) page.getModel().get("sedi")).get(1).getVotoMedio())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome()),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(5.0f, ((List<Sede>) page.getModel().get("sedi")).get(0).getVotoMedio()),
                    () -> assertEquals(3.0f, ((List<Sede>) page.getModel().get("sedi")).get(1).getVotoMedio())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_noOrd_byCitta_test(String cookie){
        int order = 0;
        int searchType = 2;
        String search = "Bologna";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A2");

        Ristorante risto3 = new Ristorante();
        risto3.setID(3);
        risto3.setNome("A1");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto1);
        ristoranti.add(risto2);
        ristoranti.add(risto3);

        Sede sede1 = new Sede();
        sede1.setVotoMedio(3.0f);
        sede1.setCitta("Bologna");

        Sede sede2 = new Sede();
        sede2.setVotoMedio(5.0f);
        sede2.setCitta("Mantova");

        Sede sede3 = new Sede();
        sede3.setVotoMedio(4.0f);
        sede3.setCitta("Bologna");

        List<Sede> sedi = new ArrayList<>();
        sedi.add(sede1);
        sedi.add(sede3);

        when(session_SedeDAO_mock.findByCitta(search)).thenReturn(sedi);
        when(session_RistoDAO_mock.getAll()).thenReturn(ristoranti);

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_noOrd_byCitta_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(0).getCitta()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(1).getCitta())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(0).getCitta()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(1).getCitta())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_sortByName_byCitta_test(String cookie){
        int order = 1;
        int searchType = 2;
        String search = "Bologna";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A2");

        Ristorante risto3 = new Ristorante();
        risto3.setID(3);
        risto3.setNome("A1");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto1);
        ristoranti.add(risto2);
        ristoranti.add(risto3);

        Sede sede1 = new Sede();
        sede1.setVotoMedio(3.0f);
        sede1.setCitta("Bologna");

        Sede sede2 = new Sede();
        sede2.setVotoMedio(5.0f);
        sede2.setCitta("Mantova");

        Sede sede3 = new Sede();
        sede3.setVotoMedio(4.0f);
        sede3.setCitta("Bologna");

        List<Sede> sedi = new ArrayList<>();
        sedi.add(sede1);
        sedi.add(sede3);

        when(session_SedeDAO_mock.findByCitta(search)).thenReturn(sedi);
        when(session_RistoDAO_mock.getAll()).thenReturn(ristoranti);

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_sortByName_byCitta_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome()),
                    () -> assertEquals("B",  ((List<Ristorante>) page.getModel().get("ristoranti")).get(2).getNome()),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(0).getCitta()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(1).getCitta())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals("A1", ((List<Ristorante>) page.getModel().get("ristoranti")).get(0).getNome()),
                    () -> assertEquals("A2", ((List<Ristorante>) page.getModel().get("ristoranti")).get(1).getNome()),
                    () -> assertEquals("B",  ((List<Ristorante>) page.getModel().get("ristoranti")).get(2).getNome()),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(0).getCitta()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(1).getCitta())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_sortByVote_byCitta_test(String cookie){
        int order = 2;
        int searchType = 2;
        String search = "Bologna";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);
        when(DatabaseDAO_mock.getRistoDAO(any())).thenReturn(session_RistoDAO_mock);
        when(DatabaseDAO_mock.getSedeDAO(any())).thenReturn(session_SedeDAO_mock);

        Ristorante risto1 = new Ristorante();
        risto1.setID(1);
        risto1.setNome("B");

        Ristorante risto2 = new Ristorante();
        risto2.setID(2);
        risto2.setNome("A2");

        Ristorante risto3 = new Ristorante();
        risto3.setID(3);
        risto3.setNome("A1");

        List<Ristorante> ristoranti = new ArrayList<>();
        ristoranti.add(risto1);
        ristoranti.add(risto2);
        ristoranti.add(risto3);

        Sede sede1 = new Sede();
        sede1.setVotoMedio(3.0f);
        sede1.setCitta("Bologna");

        Sede sede2 = new Sede();
        sede2.setVotoMedio(5.0f);
        sede2.setCitta("Mantova");

        Sede sede3 = new Sede();
        sede3.setVotoMedio(4.0f);
        sede3.setCitta("Bologna");

        List<Sede> sedi = new ArrayList<>();
        sedi.add(sede1);
        sedi.add(sede3);

        when(session_SedeDAO_mock.findByCitta(search)).thenReturn(sedi);
        when(session_RistoDAO_mock.getAll()).thenReturn(ristoranti);

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_sortByVote_byCitta_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(4.0f, ((List<Sede>) page.getModel().get("sedi")).get(0).getVotoMedio()),
                    () -> assertEquals(3.0f, ((List<Sede>) page.getModel().get("sedi")).get(1).getVotoMedio()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(0).getCitta()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(1).getCitta())
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("resturantsListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(ristoranti, page.getModel().get("ristoranti")),
                    () -> assertEquals(sedi, page.getModel().get("sedi")),
                    () -> assertEquals(4.0f, ((List<Sede>) page.getModel().get("sedi")).get(0).getVotoMedio()),
                    () -> assertEquals(3.0f, ((List<Sede>) page.getModel().get("sedi")).get(1).getVotoMedio()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(0).getCitta()),
                    () -> assertEquals("Bologna", ((List<Sede>) page.getModel().get("sedi")).get(1).getCitta())
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void getSearchedResturants_sort_noSearchType_test(String cookie){
        int order = 0;
        int searchType = 0;
        String search = "";

        ModelAndView page = controller.getSearchedResturants(cookie, searchType, search, order);

        System.out.println("Avvio test: getSearchedResturants_sortByVote_byCitta_test");

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("redirect_to", page.getViewName()),
                    () -> assertNull(page.getModel().get("user"))
            );
        } else {
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("redirect_to", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user"))
            );
        }
    }
}