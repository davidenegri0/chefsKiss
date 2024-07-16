package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.recipesListController;
import com.project.chefskiss.controllers.sedeController;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.IngredienteDAO;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AbstractSoftAssertions.assertAll;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(recipesListController.class)
public class recipesListTests_UT {
    @Mock
    private DAOFactory DatabaseDAO_mock;

    @Mock
    private PiattoDAO session_PiattoDAO_mock;
    @Mock
    private IngredienteDAO session_IngredienteDAO_mock;

    @InjectMocks
    private recipesListController controller;
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
    void viewRecipesList_sortByNome_test(String cookie){
        int order = 1;
        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Piatto2");

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Piatto1");

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni = new ArrayList<>();
        allergeni.add(allergene1);
        allergeni.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Ingrediente1");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Ingrediente2");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_PiattoDAO_mock.findMostRecent()).thenReturn(piatti);
        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);

        ModelAndView page = controller.viewRecipesList(order, cookie);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Piatto1", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Piatto2", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(false, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Piatto1", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Piatto2", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(false, page.getModel().get("searched"))
            );
        }
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void viewRecipesList_sortByVoto_test(String cookie){
        int order = 2;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Piatto2");
        piatto1.setVotoMedio(3.5f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Piatto1");
        piatto2.setVotoMedio(4.0f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni = new ArrayList<>();
        allergeni.add(allergene1);
        allergeni.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Ingrediente1");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Ingrediente2");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_PiattoDAO_mock.findMostRecent()).thenReturn(piatti);
        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);

        ModelAndView page = controller.viewRecipesList(order, cookie);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals(4.0f, ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getVotoMedio()),
                    () -> assertEquals(3.5f, ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getVotoMedio()),
                    () -> assertEquals(false, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals(4.0f, ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getVotoMedio()),
                    () -> assertEquals(3.5f, ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getVotoMedio()),
                    () -> assertEquals(false, page.getModel().get("searched"))
            );
        }
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_test(String cookie){
        /*String search = "Pasta";
        int type = 1;
        int order = 0;
        List<String> allergeni = new ArrayList<>();
        allergeni.add("lattosio");
        allergeni.add("glutine");

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni = new ArrayList<>();
        allergeni.add(allergene1);
        allergeni.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_PiattoDAO_mock.findByName(search)).thenReturn(piatti);
        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni);

        if (cookie.equals("")){
            assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
    }*/

    }
    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_searchByName_sortByName_test(String cookie){
        String search = "Pasta";
        int type = 1;
        int order = 1;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = new ArrayList<>();
        allergeni1.add(allergene1);
        allergeni1.add(allergene2);
        List<String> allergeni2 = new ArrayList<>();

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findByName(search.toLowerCase(), allergeni1)).thenReturn(piatti);

        System.out.println("Avvio test: searchRecipes_searchByName_sortByName_test");
        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_searchByName_sortByVoto_test(String cookie){
        String search = "Pasta";
        int type = 1;
        int order = 2;


        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = new ArrayList<>();
        allergeni1.add(allergene1);
        allergeni1.add(allergene2);
        List<String> allergeni2 = new ArrayList<>();

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findByName(search.toLowerCase(), allergeni1)).thenReturn(piatti);


        System.out.println("Avvio test: searchRecipes_searchByName_sortByVoto_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_searchByIngredient_sortByName_test(String cookie){
        String search = "Pasta";
        int type = 2;
        int order = 1;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = new ArrayList<>();
        allergeni1.add(allergene1);
        allergeni1.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");
        Ingrediente ingrediente3 = new Ingrediente();

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findByIngediente(any(), any())).thenReturn(piatti);

        System.out.println("Avvio test: searchRecipes_searchByIngredient_sortByName_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_searchByIngredient_sortByVoto_test(String cookie){
        String search = "Pasta";
        int type = 2;
        int order = 2;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = new ArrayList<>();
        allergeni1.add(allergene1);
        allergeni1.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");
        Ingrediente ingrediente3 = new Ingrediente();

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findByIngediente(any(), any())).thenReturn(piatti);

        System.out.println("Avvio test: searchRecipes_searchByIngredient_sortByVoto_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_searchByDate_sortByName_test(String cookie){
        String search = "Pasta";
        int type = 0;
        int order = 1;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = new ArrayList<>();
        allergeni1.add(allergene1);
        allergeni1.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");
        Ingrediente ingrediente3 = new Ingrediente();

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findMostRecent()).thenReturn(piatti);

        System.out.println("Avvio test: searchRecipes_searchByDate_sortByName_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @MethodSource("caricaArgomenti")
    @Tag("unit")
    void searchRecipes_searchByDate_sortByVoto_test(String cookie){
        String search = "";
        int type = 0;
        int order = 2;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = new ArrayList<>();
        allergeni1.add(allergene1);
        allergeni1.add(allergene2);

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");
        Ingrediente ingrediente3 = new Ingrediente();

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findMostRecent()).thenReturn(piatti);

        System.out.println("Avvio test: searchRecipes_searchByDate_sortByVoto_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void searchRecipes_searchByIngredient_noAllergeni_test(){
        String cookie = "";
        String search = "Pasta";
        int type = 2;
        int order = 2;

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        List<String> allergeni1 = null;

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findByIngediente(any())).thenReturn(piatti);

        System.out.println("Avvio test: searchRecipes_searchByIngredient_sortByVoto_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void searchRecipes_searchByName_noAllergeni_test(){
        String cookie = "";
        String search = "Pasta";
        int type = 1;
        int order = 2;


        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(session_IngredienteDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti1 = new ArrayList<>();
        piatti1.add(piatto1);
        piatti1.add(piatto2);

        String allergene1 = "lattosio";
        String allergene2 = "glutine";
        List<String> allergeni1 = null;

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setNome("Pasta");
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setNome("Pesto");

        List<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente1);
        ingredienti.add(ingrediente2);

        when(session_IngredienteDAO_mock.getAllAllergeni()).thenReturn(allergeni1);
        when(session_IngredienteDAO_mock.getAllIngredients()).thenReturn(ingredienti);
        when(session_PiattoDAO_mock.findByName(any())).thenReturn(piatti1);

        System.out.println("Avvio test: searchRecipes_searchByName_sortByVoto_test");

        ModelAndView page = controller.searchRecipes(cookie, search, type, order, allergeni1);

        if (cookie.equals("")){
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti1, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        } else {
            Assertions.assertAll("Caratteristiche della pagina caricata",
                    () -> assertEquals("recipesListPage", page.getViewName()),
                    () -> assertNotNull(page.getModel().get("user")),
                    () -> assertEquals(allergeni1, page.getModel().get("allergeni")),
                    () -> assertEquals(ingredienti, page.getModel().get("ingredienti")),
                    () -> assertEquals(piatti1, page.getModel().get("listaPiatti")),
                    () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                    () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome()),
                    () -> assertEquals(true, page.getModel().get("searched"))
            );
        }

        System.out.println("Test passato");
    }
//
    @Test
    @Tag("unit")
    void invalid_viewMyRecipesList_test(){
        String cookie = "";

        System.out.println("Avvio Test: invalid_viewMyRecipesList_test");

        ModelAndView page = controller.viewMyRecipesList(0, cookie);
        Assertions.assertAll( "Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @Test
    @Tag("unit")
    void viewMyRecipesList_sortByName_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        when(session_PiattoDAO_mock.findByCF(any())).thenReturn(piatti);

        System.out.println("Avvio Test: viewMyRecipesList_sortByName_test");

        ModelAndView page = controller.viewMyRecipesList(1, cookie);

        Assertions.assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("myRecipesPage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome())
        );

        System.out.println("Test passato");
    }


    @Test
    @Tag("unit")
    void viewMyRecipesList_sortByVoto_test(){
        String cookie = "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi";

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(session_PiattoDAO_mock);

        Piatto piatto1 = new Piatto();
        piatto1.setNome("Pasta al pesto");
        piatto1.setVotoMedio(4.7f);

        Piatto piatto2 = new Piatto();
        piatto2.setNome("Pasta al ragù");
        piatto2.setVotoMedio(4.9f);

        List<Piatto> piatti = new ArrayList<>();
        piatti.add(piatto1);
        piatti.add(piatto2);

        when(session_PiattoDAO_mock.findByCF(any())).thenReturn(piatti);

        System.out.println("Avvio Test: viewMyRecipesList_sortByVoto_test");

        ModelAndView page = controller.viewMyRecipesList(2, cookie);

        Assertions.assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("myRecipesPage", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user")),
                () -> assertEquals(piatti, page.getModel().get("listaPiatti")),
                () -> assertEquals("Pasta al ragù", ((List<Piatto>) page.getModel().get("listaPiatti")).get(0).getNome()),
                () -> assertEquals("Pasta al pesto", ((List<Piatto>) page.getModel().get("listaPiatti")).get(1).getNome())
        );

        System.out.println("Test passato");
    }

}
