package com.project.chefskiss.integration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.GenericContainer;

//import org.testcontainers.containers.BrowserWebDriverContainer;

import software.xdev.testcontainers.selenium.containers.browser.BrowserWebDriverContainer;
import software.xdev.testcontainers.selenium.containers.browser.CapabilitiesBrowserWebDriverContainer;
import org.testcontainers.lifecycle.TestDescription;
import java.util.Optional;

import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Disabled
@Testcontainers
public class SeleniumTests_IT {

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
        //System.setProperty("webdriver.chrome.driver", "src/test/chromedriver-win64/chromedriver.exe");
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        //driver = new ChromeDriver(options);
        //driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        driver = new RemoteWebDriver(chrome.getSeleniumAddressURI().toURL(), new ChromeOptions());
        driver.manage().window().maximize();
    }

    @Test
    @Tag("integration")
    public void testHomepageJsp() {
        // Avvia il server Spring Boot prima di questo test
        //driver.get("http://localhost:8080/homepage");
        //org.testcontainers.Testcontainers.exposeHostPorts(8080);
        //chrome.start();
        //driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        try {
            driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/homepage");
            sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(driver.getPageSource());
        System.out.println(webapp.getLogs());
        // Verifica il titolo della pagina
        String pageTitle = driver.getTitle();
        assertEquals("Homepage", pageTitle);
    }

    @Test
    @Tag("integration")
    public void testHomepage_Piatti_list () throws InterruptedException {
        //driver.get("http://localhost:8080/homepage");
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/homepage");

        sleep(2000);

        List<WebElement> piatti = driver.findElements(By.cssSelector("[id^='recipeBlock']"));
        System.out.println(piatti.size());
        assertTrue(piatti.size() == 4);
    }

    @Test
    @Tag("integration")
    public void testLogin() throws InterruptedException {
        //driver.get("http://localhost:8080/login");
        driver.get("http://" + webapp.getNetworkAliases().get(1) + ":" + "8080" + "/login");

        sleep(2000);

        // Inserisci le credenziali dell'utente
        driver.findElement(By.id("em")).sendKeys("simona@example.com");
        driver.findElement(By.id("ps")).sendKeys("simona321");

        sleep(2000);

        // Invia il modulo di login
        driver.findElement(By.id("loginButton")).click();

        sleep(2000);

        // Verifica che l'utente sia loggato correttamente
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assert(bodyText.contains("Bentornato/a Simona Leoni!"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
