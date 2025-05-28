import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GitHubTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new EdgeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Тест главной страницы (заголовок)
    @Test
    public void testHomePageTitle() {
        driver.get("https://github.com/");
        wait.until(ExpectedConditions.titleContains("GitHub"));
        assertTrue(driver.getTitle().contains("GitHub"));
    }

    // Проверка меню навигации
    @Test
    public void testNavigationMenu() {
        driver.get("https://github.com/");

        List<WebElement> menuItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("header nav ul li")
        ));

        assertTrue(menuItems.size() >= 5, "Количество пунктов меню меньше ожидаемого");

        String[] expectedItems = {"Product", "Solutions", "Open Source", "Pricing"};
        for (String item : expectedItems) {
            assertTrue(driver.getPageSource().contains(item),
                    "Пункт меню '" + item + "' не найден");
        }
    }

    // Тест поиска репозиториев
    @Test
    public void testRepositorySearch() {
        driver.get("https://github.com/");

        // Кликаем по кнопке поиска чтобы открыть поле ввода
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.header-search-button")
        ));
        searchButton.click();

        // Находим поле ввода
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input#query-builder-test[data-target='query-builder.input']")
        ));

        String searchQuery = "Selenium";
        searchInput.sendKeys(searchQuery);
        searchInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.urlContains("search"));
        assertTrue(driver.getCurrentUrl().contains("q=Selenium"));


    }

    // Проверка авторизации (отображение формы)
    @Test
    public void testLoginFormDisplay() {
        driver.get("https://github.com/login");

        WebElement loginField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("login_field")
        ));
        assertTrue(loginField.isDisplayed(), "Поле логина не отображается");

        WebElement passwordField = driver.findElement(By.id("password"));
        assertTrue(passwordField.isDisplayed(), "Поле пароля не отображается");

        WebElement signInButton = driver.findElement(By.name("commit"));
        assertTrue(signInButton.isDisplayed(), "Кнопка входа не отображается");
    }

    // Тест перехода на страницу документации
    @Test
    public void testDocumentationPage() {
        driver.get("https://github.com/");

        WebElement docsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//footer//a[contains(text(), 'Docs')]")
        ));
        docsLink.click();

        wait.until(ExpectedConditions.urlContains("docs.github.com"));
        assertTrue(driver.getCurrentUrl().contains("docs.github.com"));
    }

    // Проверка футера (ссылки)
    @Test
    public void testFooterLinks() {
        driver.get("https://github.com/");

        List<WebElement> footerColumns = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("footer ul")
        ));

        assertTrue(footerColumns.size() >= 4, "Количество колонок в футере меньше ожидаемого");

        String[] expectedLinks = {"About", "Blog", "Careers", "Contact"};
        for (String link : expectedLinks) {
            assertTrue(driver.getPageSource().contains(link),
                    "Ссылка '" + link + "' не найдена в футере");
        }
    }

    // Тест страницы "About"
    @Test
    public void testAboutPage() {
        driver.get("https://github.com/about");


        WebElement mainHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Let') and contains(text(), 'build')]")
        ));
        assertTrue(mainHeading.getText().length() > 20, "Главный заголовок слишком короткий");
    }

    // Проверка кнопок на странице Team
    @Test
    public void testTeamPage() {
        driver.get("https://github.com/team");


        WebElement startTrialButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/join?plan=business&ref_cta=Get%2520started%2520with%2520Team&ref_loc=team-page-hero&ref_page=%2Fteam') and contains(text(), 'Get started with Team')]")
        ));
        assertTrue(startTrialButton.isDisplayed(),
                "Кнопка 'Get started with Team' не отображается");
    }

    // Тест страницы "Pricing"
    @Test
    public void testPricingPage() {
        driver.get("https://github.com/pricing");

        wait.until(ExpectedConditions.titleContains("Pricing"));

        WebElement getStartedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/organizations/enterprise_plan') and contains(text(), 'Start free for 30 days')]")
        ));
        assertTrue(getStartedButton.isDisplayed(), "Кнопка 'Start free for 30 days' не отображается");
    }

    // Проверка отображения популярных репозиториев
    @Test
    public void testTrendingRepositories() {
        driver.get("https://github.com/trending");

        wait.until(ExpectedConditions.titleContains("Trending"));

        List<WebElement> repos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".Box article")
        ));
        assertTrue(repos.size() >= 10, "Количество популярных репозиториев меньше 10");

        for (WebElement repo : repos) {
            WebElement title = repo.findElement(By.cssSelector("h2"));
            assertTrue(title.getText().length() > 0, "Название репозитория отсутствует");
        }
    }
}