package test.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestNGTestOrder {

    @Test(priority = 1, dependsOnMethods = {"test01"})
    public void test02(){
        System.out.println("Test 02");
    }

    @Test(priority = 2)
    public void test01(){
        Assert.fail();
        System.out.println("Test 01");
    }
}
