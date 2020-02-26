package managers;

import enums.DriverType;
import enums.EnvironmentType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.concurrent.TimeUnit;

public class WebDriverManager {
    private WebDriver driver;
    private static DriverType driverType;
    private static EnvironmentType environmentType;
    private static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";
    private static final String FIREFOX_DRIVER_PROPERTY = "webdriver.gecko.driver";

    public WebDriverManager() {
        driverType = FileReaderManager.getInstance().getConfigReader().getBrowser();
        environmentType = FileReaderManager.getInstance().getConfigReader().getEnvironment();
    }

    public WebDriver getDriver() {
        if(driver == null) driver = createDriver();
        return driver;
    }

    private WebDriver createDriver() {
        switch (environmentType) {
            case LOCAL : driver = createLocalDriver();
                break;
            case REMOTE : driver = createRemoteDriver();
                break;
        }
        return driver;
    }

    private WebDriver createRemoteDriver() {
        throw new RuntimeException("RemoteWebDriver is not yet implemented");
    }

    private WebDriver createLocalDriver() {
        switch (driverType) {
            case FIREFOX :
                System.setProperty(FIREFOX_DRIVER_PROPERTY,FileReaderManager.getInstance().getConfigReader().getFirefoxDriverPath());
                driver = new FirefoxDriver();
                break;
            case CHROME :
                System.setProperty(CHROME_DRIVER_PROPERTY, FileReaderManager.getInstance().getConfigReader().getChromeDriverPath());
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("useAutomationExtension", false);
                driver = new ChromeDriver(options);
                break;
            case INTERNETEXPLORER : driver = new InternetExplorerDriver();
                break;
        }

        if(FileReaderManager.getInstance().getConfigReader().getBrowserWindowSize()) driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().getConfigReader().getImplicitlyWait(), TimeUnit.SECONDS);
        return driver;
    }

    public void closeDriver() {
        driver.close();
        driver.quit();
    }

    public void quitDriver() {
        driver.quit();
    }

    /*public void waitForPageToLoad()
    {
        WebDriverWait wait = new WebDriverWait(driver,30);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)driver;

        ExpectedCondition<Boolean> jsload = webDriver -> ((JavascriptExecutor)driver)
                .executeScript("return document.readyState").toString().equals("complete");

        //Get JS Ready
        boolean jsReady = javascriptExecutor.executeScript("return document.readyState").toString().equals("complete");
        LogUtil logutil = new LogUtil();
        if(!jsReady)
            wait.until(jsload);
        else
            logutil.write("Page is loaded");
    }

    public static void WaitForElementVisible(final WebElement elementFindBy){
        WebDriverWait wait= new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(elementFindBy));
    }

    public void WaitForElementTextVisible(final WebElement elementFindBy, String text){
        WebDriverWait wait= new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.textToBePresentInElement(elementFindBy, text));
    }

    public void WaitUntilTextDisplayed(final By element, String text){
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(textDisplayed(element, text));
    }

    private ExpectedCondition<Boolean> textDisplayed (final By elementFindBy, final String text){
        return webDriver -> webDriver.findElement(elementFindBy).getText().contains(text);
    }

    public void WaitElementEnabled(final By elementFindBy){
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(webDriver -> webDriver.findElement(elementFindBy).isEnabled());
    }*/
}
