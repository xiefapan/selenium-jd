package com.xfp.jd.script;

import com.xfp.jd.script.jd.JDLogin;
import com.xfp.jd.script.jd.ScanProduct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Entry {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "E:\\code\\test\\jd\\chromedriver_win32\\chromedriver.exe");//chromedriver服务地址
        WebDriver driver = new ChromeDriver(); //新建一个WebDriver 的对象，但是new 的是FirefoxDriver的驱动
        driver.manage().window().maximize();
        Entry entry = new Entry();
        entry.start(driver);
    }

    private void start(WebDriver driver) throws Exception
    {
        boolean login = login(driver);
        if(login)
        {
           scanProioduct(driver);
        }

        //driver.quit();
    }

    private boolean login(WebDriver driver) throws Exception {
        String account="jd_22ef92ba3d124";
        String pwd="musong56";

        JDLogin jdLogin = new JDLogin(driver, account, pwd);
        return jdLogin.login();
    }

    private void scanProioduct(WebDriver driver) throws Exception {
        new ScanProduct().search(driver,"沁丝缘 板鞋 男");
    }
}
