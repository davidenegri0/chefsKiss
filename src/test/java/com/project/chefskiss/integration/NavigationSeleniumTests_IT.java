package com.project.chefskiss.integration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.TestDescription;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import software.xdev.testcontainers.selenium.containers.browser.BrowserWebDriverContainer;
import software.xdev.testcontainers.selenium.containers.browser.CapabilitiesBrowserWebDriverContainer;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;


//@Disabled
@Testcontainers
@DisplayName("End to End Selenium Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NavigationSeleniumTests_IT {
    private WebDriver driver;
    private WebDriverWait wait;

    private static Network network = Network.newNetwork();

    @Container
    private static GenericContainer mysql = new GenericContainer(DockerImageName.parse("davidenegri01/chefskiss_db:latest"))
            .withExposedPorts(3306)
            .withNetwork(network)
            .withNetworkAliases("database")
            .waitingFor(Wait.forHealthcheck());

    @Container
    private static GenericContainer webapp = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromFile("Dockerfile", new File("Dockerfile"))
                    .withFileFromFile("chefsKiss-0.0.1-SNAPSHOT.war", new File("target/chefsKiss-0.0.1-SNAPSHOT.war"))
    )
            .withExposedPorts(8080)
            .withEnv("DB_HOSTNAME", "database")
            .withNetwork(mysql.getNetwork())
            .withNetworkAliases("webapp")
            .waitingFor(Wait.forHttp("/").forPort(8080))
            .dependsOn(mysql);

/*    @Container
    private static BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>(DockerImageName.parse("selenium/standalone-chrome:4.8.3"))
            .withCapabilities(new ChromeOptions())
            .withNetwork(webapp.getNetwork())
            .withNetworkAliases("chrome")
            //.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("/home/runner/work/chefsKiss/chefsKiss/target/site"), VncRecordingContainer.VncRecordingFormat.MP4)
            .dependsOn(webapp);*/

    @Container
    private static BrowserWebDriverContainer<?> chrome = new CapabilitiesBrowserWebDriverContainer<>(new ChromeOptions())
            .withNetwork(webapp.getNetwork())
            .withCopyFileToContainer(MountableFile.forHostPath("src/main/resources/static/img/chef'skiss_logo.png"), "/test.jpg")
            .withNetworkAliases("chrome")
            /*.withRecordingMode(BrowserWebDriverContainer.RecordingMode.RECORD_ALL)
            .withRecordingDirectory(Path.of("target/site"))*/
            .dependsOn(webapp);

    @BeforeAll
    static void beforeAll() {
        mysql.start();
        //webapp.addEnv("DB_HOSTNAME", (String)mysql.getNetworkAliases().get(1));
        webapp.addEnv("DB_PORT", Integer.toString(3306));
        webapp.start();
        chrome.start();

    }

    @AfterAll
    static void afterAll() {
        mysql.close();
        webapp.close();
        chrome.close();
        mysql.stop();
        webapp.stop();
        chrome.stop();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Configura il percorso del ChromeDriver
//        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver-win64/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        driver = new ChromeDriver(options);
        //driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        driver = new RemoteWebDriver(chrome.getSeleniumAddressURI().toURL(), new ChromeOptions());
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @Tag("integration")
    public void visualizzaSede() throws InterruptedException {

        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/homepage");

        // Vai alla pagina della lista dei ristoranti

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/resturantsList']"))).click();

        // Vai alla pagina di un ristorante

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/restaurant?id=3']"))).click();

        // Vai alla pagina della singola sede

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/sede?id=44.4940084;11.3431645']"))).click();

        // Verifica che la ricetta sia stata trovata

        List<WebElement> recipes = driver.findElements(By.className("list-group-item"));
        WebElement valutazione = driver.findElement(By.id("valutazioneBlock"));

        assertTrue(recipes.size() > 0);
        assertNotNull(valutazione);

        System.out.println("Test visualizzaSede passato");
    }

    @Test
    @Tag("integration")
    @Order(4)
    public void recensisciSede() throws InterruptedException {

        login();

        // Vai alla pagina della lista dei ristoranti

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/resturantsList']"))).click();

        // Vai alla pagina di un ristorante

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/restaurant?id=5']"))).click();

        // Vai alla pagina della singola sede

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/sede?id=45.0606258;7.6840466']"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/addRecensione?type=2&id=45.0606258;7.6840466']"))).click();

        sleep(1000);

        WebElement slider = driver.findElement(By.cssSelector("input[type='range']"));
        slider.sendKeys(Keys.RIGHT);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='submit']"))).click();

        sleep(1000);

        // Verifica che la ricetta sia stata trovata

        List<WebElement> valutazione = driver.findElements(By.id("valutazioneBlock"));

        assertEquals(2, valutazione.size());

        assertTrue(valutazione.get(1).getText().contains("Mario Rossi"));
        System.out.println("Test recensisciSede passato");
    }

    @Test
    @Tag("integration")
    @Order(5)
    public void modificaRecensioneSede() throws InterruptedException {

        login();

        // Vai alla pagina della lista dei ristoranti

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/resturantsList']"))).click();

        // Vai alla pagina di un ristorante

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/restaurant?id=5']"))).click();

        // Vai alla pagina della singola sede

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/sede?id=45.0606258;7.6840466']"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/modifyRecensione?type=4&id=45.0606258;7.6840466']"))).click();

        sleep(1000);

        WebElement slider = driver.findElement(By.cssSelector("input[type='range']"));
        slider.sendKeys(Keys.END);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='submit']"))).click();

        sleep(1000);

        // Verifica che la ricetta sia stata trovata

        List<WebElement> valutazione = driver.findElements(By.id("valutazioneBlock"));

        assertEquals(2, valutazione.size());

        assertTrue(valutazione.get(1).getText().contains("Mario Rossi"));
        System.out.println("Test modificaRecensioneSede passato");
    }

    @Test
    @Tag("integration")
    @Order(6)
    public void cancellaRecensioneSede() throws InterruptedException {

        login();

        // Vai alla pagina della lista dei ristoranti

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/resturantsList']"))).click();

        // Vai alla pagina di un ristorante

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/restaurant?id=5']"))).click();

        // Vai alla pagina della singola sede

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/sede?id=45.0606258;7.6840466']"))).click();

//        WebElement cancellaEl = wait.until(ExpectedConditions.elementToBeClickable(By.id("cancella")));
//        cancellaEl.click();
        WebElement cancella = wait.until(ExpectedConditions.elementToBeClickable(By.id("cancella")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancella);

        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        alert.accept();

        // Verifica che la ricetta sia stata trovata

        List<WebElement> valutazione = driver.findElements(By.id("valutazioneBlock"));

        assertEquals(1, valutazione.size());

        System.out.println("Test cancellaRecensioneSede passato");
    }

    @Test
    @Tag("integration")
    public void ricercaRistorante() throws InterruptedException {

        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/homepage");

        // Vai alla pagina della lista delle ricette

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/resturantsList']"))).click();

        // Inserisci il nome della ricetta da cercare

        sleep(1000);

        WebElement selectElement = driver.findElement(By.id("searchType"));
        Select select = new Select(selectElement);
        select.selectByValue("2");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("search"))).sendKeys("Bologna");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

        sleep(1000);

        // Verifica che la ricetta sia stata trovata

        List<WebElement> recipes = driver.findElements(By.id("sediBlock"));

        assertEquals(2, recipes.size());
        assertTrue(recipes.get(0).getText().contains("Bologna"));
        assertTrue(recipes.get(1).getText().contains("Bologna"));

        System.out.println("Test ricercaRistorante passato");
    }

    @Test
    @Tag("integration")
    public void ricercaRicette() throws InterruptedException {

        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/homepage");

        // Vai alla pagina della lista delle ricette

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/recipesView']"))).click();

        // Inserisci il nome della ricetta da cercare

        wait.until(ExpectedConditions.elementToBeClickable(By.id("search"))).sendKeys("Pasta al pomodoro");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

        sleep(1000);

        // Verifica che la ricetta sia stata trovata

        List<WebElement> recipes = driver.findElements(By.id("recipeBlock"));

        assertEquals(1, recipes.size());
        assertTrue(recipes.get(0).getText().contains("Pasta al pomodoro"));

        System.out.println("Test ricercaRicette passato");
    }

    @Test
    @Tag("integration")
    @Order(1)
    public void recensisciRicetta() throws InterruptedException {

        login();

        // Vai alla pagina della lista delle ricette

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/recipesView']"))).click();

        // Scegli il piatto da recensire

        WebElement piatto = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/plate?id=15']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", piatto);
        //wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/plate?id=15']"))).click();

        /*
        Actions actions = new Actions(driver);
        actions.moveToElement(elem).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/plate?id=15']"))).click();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("scroll(0, 250)"); // if the element is on bottom.
        elem.click();

        WebElement elem = driver.findElement(By.cssSelector("a[href='/plate?id=15']"));
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        jse2.executeScript("arguments[0].scrollIntoView()", elem);
        elem.click();
        */

        // Vai alla pagina per recensire il piatto
        WebElement add = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/addRecensione?type=1&id=15']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);
        //wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/addRecensione?type=1&id=15']"))).click();

        // Imposta la valutazione

        sleep(1000);

        WebElement slider = driver.findElement(By.cssSelector("input[type='range']"));
        slider.sendKeys(Keys.RIGHT);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("commento"))).sendKeys("Ottima ricetta");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='submit']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        //wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='submit']"))).click();

        // Verifica che la recensione sia stata inserita

        List<WebElement> recipes = driver.findElements(By.id("reviewBlock"));

        assertEquals(2, recipes.size());
        assertTrue(recipes.get(1).getText().contains("Mario Rossi"));

        System.out.println("Test recensisciRicetta passato");
    }

    @Test
    @Tag("integration")
    @Order(2)
    public void modificaRecensione() throws InterruptedException {

        login();

        // Vai alla pagina della lista delle ricette

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/recipesView']"))).click();

        // Scegli il piatto da recensire

        WebElement piatto = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/plate?id=15']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", piatto);

        // Vai alla pagina per recensire il piatto
        WebElement add = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/modifyRecensione?type=3&id=15']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);

        // Imposta la valutazione

        sleep(1000);

        WebElement slider = driver.findElement(By.cssSelector("input[type='range']"));
        slider.sendKeys(Keys.END);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("commento"))).sendKeys("Buona ricetta");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='submit']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        // Verifica che la recensione sia stata modificata

        List<WebElement> recipes = driver.findElements(By.id("reviewBlock"));

        assertEquals(2, recipes.size());
        assertTrue(recipes.get(1).getText().contains("Mario Rossi"));
        assertTrue(recipes.get(1).getText().contains("Buona ricetta"));

        System.out.println("Test modificaRecensione passato");
    }

    @Test
    @Tag("integration")
    @Order(3)
    public void cancellaRecensione() throws InterruptedException {

        login();

        // Vai alla pagina della lista delle ricette

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/recipesView']"))).click();

        // Scegli il piatto da recensire

        WebElement piatto = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/plate?id=15']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", piatto);

        WebElement cancella = wait.until(ExpectedConditions.elementToBeClickable(By.id("cancella")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancella);
//        cancella.click();

        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        alert.accept();

        // Verifica che la recensione sia stata cancellata

        List<WebElement> recipes = driver.findElements(By.id("reviewBlock"));

        assertEquals(1, recipes.size());

        System.out.println("Test cancellaRecensione passato");
    }
    /*
    @Test
    @Tag("integration")
    @Order(1)
    public void caricaRicetta() throws InterruptedException, IOException {

        login();

        // Vai alla pagina della lista delle ricette

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/recipesView']"))).click();

        sleep(1000);

        // Click sul pulsante per caricare una ricetta

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn btn-success']"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("nome_piatto"))).sendKeys("Prova");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("preparazione"))).sendKeys("Preparazione di prova");
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(By.id("ingrediente1"))));
        select.selectByVisibleText("Pomodoro");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("quantita1"))).sendKeys("2");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("immagine"))).sendKeys("/test.jpg");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Ok']"))).click();

        // Verifica che la ricetta sia stata trovata

        wait.until(ExpectedConditions.elementToBeClickable(By.id("search"))).sendKeys("Prova");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

        sleep(1000);

        List<WebElement> recipes = driver.findElements(By.id("recipeBlock"));

        assertEquals(1, recipes.size());

        System.out.println("Test passato");
    }
*/

    @Test
    @Tag("integration")
    @Disabled
    public void prenotaInSede() throws InterruptedException {

        login();

        // Vai alla pagina della lista dei ristoranti

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/resturantsList']"))).click();

        // Vai alla pagina di un ristorante

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/restaurant?id=3']"))).click();

        // Vai alla pagina della singola sede

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/sede?id=44.4940084;11.3431645']"))).click();

        // Aggiungi una prenotazione
        //http://localhost:8080/addPrenotazione?coordinate=44.4940084;11.3431645

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/addPrenotazione?coordinate=44.4940084;11.3431645']"))).click();

        // Inscerisci i dati della prenotazione e conferma

        wait.until(ExpectedConditions.elementToBeClickable(By.id("data"))).sendKeys("15/10/2025");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("orario"))).sendKeys("20:00");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("posti"))).sendKeys("4");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("conferma"))).click();

        // Verifica che la prenotazione sia stata inserita

        sleep(1000);

        System.out.println(driver.getPageSource());

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/prenotazioniList']"))).click();

        sleep(1000);

        String page = driver.getPageSource();
        assertAll(
                () -> assertTrue(page.contains("Data: 2025-10-15")),
                () -> assertTrue(page.contains("Orario: 20:00:00")),
                () -> assertTrue(page.contains("Posti: 4"))
        );

        System.out.println("Test visualizzaSede passato");
    }

    public void login() throws InterruptedException {
//        driver.get("http://localhost:8080/login");
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/login");

        sleep(1000);

        // Inserisci le credenziali dell'utente
        driver.findElement(By.id("em")).sendKeys("mario@example.com");
        driver.findElement(By.id("ps")).sendKeys("securepass");

        sleep(1000);

        // Invia il modulo di login
        driver.findElement(By.id("loginButton")).click();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();

/*            chrome.afterTest(new TestDescription()
            {
                @Override
                public String getTestId()
                {
                    return "demo-" + (new ChromeOptions()).getBrowserName();
                }

                @Override
                public String getFilesystemFriendlyName()
                {
                    return "demo-" + (new ChromeOptions()).getBrowserName();
                }
            }, Optional.empty());*/
        }
    }
}
