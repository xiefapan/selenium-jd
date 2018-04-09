package com.xfp.jd.script.jd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScanProduct {
    public void search(WebDriver driver,String keywords){
        driver.findElement(By.id("key")).sendKeys(keywords);
        driver.findElement(By.tagName("button")).click();

        selectItem(driver);

        followProduct(driver);
    }

    private void selectItem(WebDriver driver)
    {

        WebElement element = driver.findElement(By.xpath("//a[@href=\'" + "//item.jd.com/26872952623.html" + "\']"));
        element.click();
    }

    /**
     * 关注产品
     * @param driver
     */
    private void followProduct(WebDriver driver)
    {
        WebElement element = driver.findElement(By.xpath("//a[@clstag='shangpin|keycount|product|guanzhushangpin_2']"));
        element.click();
    }
}
