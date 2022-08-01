package test.global.footer;

import driver.DriverFactory;
import models.components.global.footer.*;
import models.pages.HomePage;
import org.openqa.selenium.WebDriver;
import url.Urls;

public class FooterTest {

    public static void main(String[] args) {

        WebDriver driver = DriverFactory.getChromeDriver();

        try{
            testFooterHomePage(driver);
            testFooterCategorypage(driver);
            testFooterRegisterpage(driver);
            testFooterLoginpage(driver);
        }catch (Exception e){
            e.printStackTrace();
        }

        driver.quit();
    }

    private static void testFooterHomePage(WebDriver driver){
        driver.get(Urls.demoBaseUrl);
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
    }

    private static void testFooterCategorypage(WebDriver driver){

    }
    private static void testFooterRegisterpage(WebDriver driver){

    }
    private static void testFooterLoginpage(WebDriver driver){

    }

    private static void testFooterColumn(FooterColumnComponent footerColumnComponent){
        System.out.println(footerColumnComponent.headerElem().getText());
        footerColumnComponent.linksElem().forEach(link -> {
        System.out.println(link.getText());
        System.out.println(link.getAttribute("href"));
        });
    }
}
