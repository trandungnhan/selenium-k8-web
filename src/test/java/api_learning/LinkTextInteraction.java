package api_learning;

import driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LinkTextInteraction {

    public static void main(String[] args) {

        // Get a chrome session
        WebDriver driver = DriverFactory.getChromeDriver();

        try {
            // Navigate to the target page
            driver.get("https://the-internet.herokuapp.com/login");

            // Define selector values
            //By poweredByLinkTextSel = By.linkText("Elemental Selenium");
            By poweredByLinkTextSel = By.partialLinkText("Elemental");

            // Find elements
            WebElement poweredByLinkTextElem = driver.findElement(poweredByLinkTextSel);

            // Interaction
            poweredByLinkTextElem.click();

            // DEBUG PURPOSE ONLY
            Thread.sleep(5000);

        }catch (Exception e){
            e.printStackTrace();
        }

        // Quit the browser session
        driver.quit();
    }
}
