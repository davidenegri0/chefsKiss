package com.project.chefskiss.unit;

import com.project.chefskiss.controllers.addPlateController;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(addPlateController.class)
public class addPlateTests_UT {
    @Mock
    private DAOFactory DatabaseDAO_mock;
    @Mock
    private PiattoDAO sessionPiattoDAO_mock;
    @Mock
    private ContieneDAO sessionContieneDAO_mock;
    @Mock
    private UserDAO sessionUserDAO_mock;
    @Mock
    private IngredienteDAO sessionIngredientiDAO_mock;
    @InjectMocks
    private addPlateController controller;
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
    @EmptySource
    @ValueSource(strings = {"CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi"})
    @Tag("unit")
    void view_unvalid_addPlatePage_test(String usr_data) {

        // Mock data
        User mario = new User();
        Sede sede = new Sede();
        sede.setCoordinate("123.456, 789.012");
        mario.setSedeU(sede);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(sessionIngredientiDAO_mock);

        when(sessionUserDAO_mock.findByCF(any())).thenReturn(mario);
        when(sessionIngredientiDAO_mock.getAllIngredients()).thenReturn(null);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.getAddPlate(usr_data);
        //assertEquals("addPlatePage", controller.getAddPlate(usr_data).getViewName());
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("index", page.getViewName())
        );

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&true&false&true&false&mario_rossi",
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&true&true&false&mario_rossi"
    })
    @Tag("unit")
    void view_valid_addPlatePage_test(String usr_data) {

        // Mock data
        User mario = new User();
        Sede sede = new Sede();
        sede.setCoordinate("123.456;789.012");
        mario.setSedeU(sede);

        List<Ingrediente> lista_ingredienti = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setNome("Ingrediente" + i);
            ingrediente.setGruppo_Allergenico("Gruppo" + i);
            lista_ingredienti.add(ingrediente);
        }

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getUserDAO(any())).thenReturn(sessionUserDAO_mock);
        when(DatabaseDAO_mock.getIngredienteDAO(any())).thenReturn(sessionIngredientiDAO_mock);

        when(sessionUserDAO_mock.findByCF(any())).thenReturn(mario);
        when(sessionIngredientiDAO_mock.getAllIngredients()).thenReturn(lista_ingredienti);

        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.getAddPlate(usr_data);
        //assertEquals("addPlatePage", controller.getAddPlate(usr_data).getViewName());
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("addPlatePage", page.getViewName()),
                () -> assertInstanceOf(User.class, page.getModel().get("user")),
                () -> assertInstanceOf(List.class, page.getModel().get("listaIngredienti")),
                () -> assertEquals(4, ((List<Ingrediente>) page.getModel().get("listaIngredienti")).size())
        );

        var Lista = ((List<Ingrediente>) page.getModel().get("listaIngredienti")).iterator();
        while(Lista.hasNext()){
            Ingrediente ingrediente = Lista.next();
            System.out.println(ingrediente.getNome());
            System.out.println(ingrediente.getGruppo_Allergenico());
        }

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @Tag("unit")
    @CsvSource({
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&true&false&true&false&mario_rossi, Pasta al sugo, Cuoci la pasta e aggiungi il sugo",
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&true&true&false&mario_rossi, Pasta al pesto, Cuoci la pasta e aggiungi il pesto",
    })
    void post_addPlatePage_test_noSede(
            String usr_data,
            String nomePiatto,
            String preparazione
    ) {

        // Mock data
        Piatto piatto = new Piatto();
        piatto.setNome(nomePiatto);
        piatto.setPreparazione(preparazione);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getContieneDAO(any())).thenReturn(sessionContieneDAO_mock);

        when(sessionPiattoDAO_mock.create(anyString(), anyString(), any(), any())).thenReturn(piatto);
        when(sessionContieneDAO_mock.create(any(), any(), anyInt())).thenReturn(null);


        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddPlate(
                usr_data,
                nomePiatto,
                preparazione,
                new MockMultipartFile("file", new byte[0]),
                List.of("Ingrediente1", "Ingrediente2"),
                List.of(3, 2),
                ""
                );
        //assertEquals("addPlatePage", controller.getAddPlate(usr_data).getViewName());
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user"))
        );

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @Tag("unit")
    @CsvSource({
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&true&false&true&false&mario_rossi, Pasta al sugo, Cuoci la pasta e aggiungi il sugo"
    })
    void post_addPlatePage_test_withSede(
            String usr_data,
            String nomePiatto,
            String preparazione
    ) {

        // Mock data
        Piatto piatto = new Piatto();
        piatto.setNome(nomePiatto);
        piatto.setPreparazione(preparazione);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getContieneDAO(any())).thenReturn(sessionContieneDAO_mock);

        when(sessionPiattoDAO_mock.create(anyString(), anyString(), any(), any())).thenReturn(piatto);
        when(sessionContieneDAO_mock.create(any(), any(), anyInt())).thenReturn(null);


        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddPlate(
                usr_data,
                nomePiatto,
                preparazione,
                new MockMultipartFile("file", new byte[0]),
                List.of("Ingrediente1", "Ingrediente2"),
                List.of(3, 2),
                "123.456;789.012"
        );
        //assertEquals("addPlatePage", controller.getAddPlate(usr_data).getViewName());
        assertAll("Caratteristiche della pagina caricata",
                () -> assertEquals("redirect_to", page.getViewName()),
                () -> assertNotNull(page.getModel().get("user"))
        );

        System.out.println("Test passato");
    }

    @ParameterizedTest
    @Tag("unit")
    @CsvSource({
            "CF12345678901234&Mario&Rossi&mario@example.com&1985-05-15&1234567890&2023-08-19&true&false&false&false&true&false&mario_rossi, Pasta al sugo, Cuoci la pasta e aggiungi il sugo",
            "'', Pasta al sugo, Cuoci la pasta e aggiungi il sugo"
    })
    void post_addPlatePage_invalid_test(
            String usr_data,
            String nomePiatto,
            String preparazione
    ){
        // Mock data
        Piatto piatto = new Piatto();
        piatto.setNome(nomePiatto);
        piatto.setPreparazione(preparazione);

        // Mocked methods
        dao_factory_mock.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(DatabaseDAO_mock);

        when(DatabaseDAO_mock.getPiattoDAO(any())).thenReturn(sessionPiattoDAO_mock);
        when(DatabaseDAO_mock.getContieneDAO(any())).thenReturn(sessionContieneDAO_mock);

        when(sessionPiattoDAO_mock.create(anyString(), anyString(), any(), any())).thenReturn(piatto);
        when(sessionContieneDAO_mock.create(any(), any(), anyInt())).thenReturn(null);


        System.out.println("Avvio test");

        // Call method
        ModelAndView page = controller.postAddPlate(
                usr_data,
                nomePiatto,
                preparazione,
                new MockMultipartFile("file", new byte[0]),
                List.of("Ingrediente1", "Ingrediente2"),
                List.of(3, 2),
        ""
        );

        assertEquals("index", page.getViewName());

        System.out.println("Test passato");
    }
}
