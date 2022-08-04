package test.global.footer;

import driver.DriverFactory;
import models.components.global.footer.*;
import models.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import url.Urls;

public class FooterTest {

    @Test(priority = 1, dependsOnMethods = {"testFooterRegisterPage"})
    public void testFooterCategoryPage() {

    }

    @Test(priority = 2)
    public void testFooterRegisterPage() {
        String actualResult = "Teo";
        String expectedResult = "Ti";
//        Verifier.verifyEquals(actualResult,expectedResult);

        // Hard assertion
        Assert.assertEquals(actualResult, expectedResult, "[ERR] Welcome message is incorrect!");

        System.out.println("Hello");
        Assert.assertTrue(actualResult.equals(expectedResult), "....");
        Assert.assertFalse(actualResult.equals(expectedResult), "...");
        Assert.fail();
        Assert.fail("...");
    }

    @Test(priority = 3)
    public void testFooterLoginPage() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(1, 2);
        softAssert.assertEquals(true, true);
        softAssert.assertEquals(2, 3);

        System.out.println("Hello");
        softAssert.assertAll();

    }

    // @Test(priority = 4)
    public void testFooterHomePage() {
        WebDriver driver = DriverFactory.getChromeDriver();
        driver.get(Urls.demoBaseUrl);
        try {
            HomePage homePage = new HomePage(driver);
            InformationColumnComponent informationColumnComp =
                    homePage.footerComp().informationColumnComp();

            CustomerServiceColumnComponent customerServiceColumnComponent =
                    homePage.footerComp().customerServiceColumnComp();

            AccountColumnComponent accountColumnComponent =
                    homePage.footerComp().accountColumnComp();

            FollowUsColumnComponent followUsColumnComponent =
                    homePage.footerComp().followUsColumnComp();

            testFooterColumn(informationColumnComp);
            testFooterColumn(customerServiceColumnComponent);
            testFooterColumn(accountColumnComponent);
            testFooterColumn(followUsColumnComponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
    }

    private static void testFooterColumn(FooterColumnComponent footerColumnComponent) {
        System.out.println(footerColumnComponent.headerElem().getText());
        footerColumnComponent.linksElem().forEach(link -> {
            System.out.println(link.getText());
            System.out.println(link.getAttribute("href"));
        });
    }
}
