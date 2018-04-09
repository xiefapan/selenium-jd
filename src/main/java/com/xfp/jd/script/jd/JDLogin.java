package com.xfp.jd.script.jd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sophie on 03/20/2016
 */

public class JDLogin {
    private WebDriver dr;
    private String account;
    private String pwd;

    public JDLogin(WebDriver driver,String account, String pwd){
        this.dr = driver;
        this.dr.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        this.account = account;
        this.pwd = pwd;

    }

    public boolean login() throws Exception{
        dr.get("https://passport.jd.com/uc/login?ltype=logout");
        dr.findElement(By.linkText("账户登录")).click();
        dr.findElement(By.id("loginname")).clear();
        dr.findElement(By.id("loginname")).sendKeys(account);
        dr.findElement(By.id("nloginpwd")).clear();
        dr.findElement(By.id("nloginpwd")).sendKeys(pwd);

        boolean sucess=false;

        JDAuthCode jdAuthCode = new JDAuthCode();
        //验证码识别可能不准，尝试10次，都失败则返回
        for(int i=0;i<10;i++)
        {
            if(jdAuthCode.authCode(dr))
            {
                sucess=true;
                break;
            }
        }



        //System.out.println(dr.getPageSource());
        Thread.sleep(2000);

        return sucess;
    }




}