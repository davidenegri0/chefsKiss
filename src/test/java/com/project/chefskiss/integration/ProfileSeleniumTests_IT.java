package com.project.chefskiss.integration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.TestDescription;
import org.testcontainers.utility.DockerImageName;
import software.xdev.testcontainers.selenium.containers.browser.BrowserWebDriverContainer;
import software.xdev.testcontainers.selenium.containers.browser.CapabilitiesBrowserWebDriverContainer;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


//@Disabled
@Testcontainers
@DisplayName("End to End Selenium Tests")
public class ProfileSeleniumTests_IT {
    // TODO: decommentare righe container e togliere primi sleep
    private WebDriver driver;

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
            .withNetworkAliases("chrome")
            .withRecordingMode(BrowserWebDriverContainer.RecordingMode.RECORD_ALL)
            .withRecordingDirectory(Path.of("target/site"))
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
        chrome.afterTest(new TestDescription()
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
        }, Optional.empty());
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
    }

    @Test
    @Tag("integration")
    @Order(1)
    public void registrazioneUtente() throws InterruptedException {
        
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/homepage");
//        driver.get("http://localhost:8080/homepage");

        sleep(1000);

        driver.findElement(By.id("login")).click();

        sleep(1000);

        // Clicca sul link per registrarsi
        driver.findElement(By.id("registration")).click();

        // inserimento dati
        driver.findElement(By.id("nome")).sendKeys("Francesco");
        driver.findElement(By.id("cognome")).sendKeys("Rossi");
        driver.findElement(By.id("cf")).sendKeys("RSSFRN00A01H501A");
        driver.findElement(By.id("email")).sendKeys("francesco.rossi@example.com");
        driver.findElement(By.id("telefono")).sendKeys("3331234567");
        WebElement dataNascita = driver.findElement(By.id("nascita"));
        dataNascita.click();
        dataNascita.sendKeys("2000-01-01");
        driver.findElement(By.id("pssw")).sendKeys("francesco123");
        driver.findElement(By.id("c_pssw")).sendKeys("francesco123");

        sleep(1000);

        driver.findElement(By.className("conferma")).click();

        sleep(1000);

        // Verifica che l'utente sia registrato correttamente
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains("Bentornato/a Francesco Rossi!"));
    }

    @Test
    @Tag("integration")
    @Order(2)
    public void visualizzazioneProfilo() throws InterruptedException {

        sleep(5000);
        login();
        //driver.get("http://localhost:8080/homepage");

        sleep(1000);

        driver.findElement(By.id("profile")).click();

        sleep(1000);

        // visualizzazione dati utente
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains("Francesco Rossi's Profile"));

    }

    @Test
    @Tag("integration")
    @Order(3)
    public void modificaProfilo() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        sleep(5000);
        login();
        //driver.get("http://localhost:8080/homepage");

        sleep(1000);

        WebElement profileElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile")));
        profileElement.click();

        sleep(1000);

        WebElement modificaEl= wait.until(ExpectedConditions.elementToBeClickable(By.id("modifica")));
        modificaEl.click();
        //sleep(1000);
        //driver.findElement(By.id("modifica")).click();

        sleep(1000);

        // modifica dati utente
        driver.findElement(By.id("tel")).clear();
        driver.findElement(By.id("tel")).sendKeys("3337654721");


        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains("3337654721"));
    }

    @Test
    @Tag("integration")
    @Order(4)
    public void testLogin() throws InterruptedException {
//        driver.get("http://localhost:8080/login");
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/login");

        sleep(1000);

        // Inserisci le credenziali dell'utente
        driver.findElement(By.id("em")).sendKeys("francesco.rossi@example.com");
        driver.findElement(By.id("ps")).sendKeys("francesco123");

        sleep(1000);

        // Invia il modulo di login
        driver.findElement(By.id("loginButton")).click();

        sleep(1000);

        // Verifica che l'utente sia loggato correttamente
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assert(bodyText.contains("Bentornato/a francesco rossi!"));
    }

    @Test
    @Tag("integration")
    @Order(5)
    public void cancellaProfilo() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        sleep(5000);
        login();
        //driver.get("http://localhost:8080/homepage");

        sleep(1000);

        WebElement profileElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile")));
        profileElement.click();

        sleep(1000);

        WebElement cancellaEl = wait.until(ExpectedConditions.elementToBeClickable(By.id("cancella")));
        cancellaEl.click();
        //sleep(1000);
        //driver.findElement(By.id("cancella")).click();

        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        alert.accept();

        sleep(1000);

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains("Benvenuti in Chef's Kiss!!"));
    }

    public void login() throws InterruptedException {
//        driver.get("http://localhost:8080/login");
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/login");

        sleep(1000);

        // Inserisci le credenziali dell'utente
        driver.findElement(By.id("em")).sendKeys("francesco.rossi@example.com");
        driver.findElement(By.id("ps")).sendKeys("francesco123");

        sleep(1000);

        // Invia il modulo di login
        driver.findElement(By.id("loginButton")).click();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
