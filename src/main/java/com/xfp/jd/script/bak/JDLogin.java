package com.xfp.jd.script.bak;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sophie on 03/20/2016
 */

public class JDLogin {
    private WebDriver dr;
    private String url;
    private String account;
    private String pwd;
    private String myJDXPATH;
    private String myBeanXPATH;
    private String datesXPATH;
    private String transacDetailsXPATH;
    private String transacNameXPATH;
    private String pageNextXPATH;
    private String total;
    private List dateDetail;
    private List transacDetail;
    private List transacName;

    public List retrieveJDBeanDatesDetails(){
        return dateDetail;
    }

    public List retrieveJDBeanTransacDetail(){
        return transacDetail;
    }

    public List retrieveJDBeanTransacName(){
        return transacName;
    }

    public JDLogin(String url, String account, String pwd,String myJDXPATH, String myBeanXPATH,
                   String datesXPATH, String transacDetailsXPATH, String transacNameXPATH, String pageNextXPATH){
        this.url = url;
        this.dr = new FirefoxDriver();
        this.dr.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        this.account = account;
        this.pwd = pwd;
        this.myJDXPATH = myJDXPATH;
        this.myBeanXPATH = myBeanXPATH;
        this.datesXPATH = datesXPATH;
        this.transacDetailsXPATH = transacDetailsXPATH;
        this.transacNameXPATH = transacNameXPATH;
        this.pageNextXPATH = pageNextXPATH;
    }

    public WebDriver login() throws Exception{
        dr.get(url);
        dr.findElement(By.id("loginname")).clear();
        dr.findElement(By.id("loginname")).sendKeys(account);
        dr.findElement(By.id("nloginpwd")).clear();
        dr.findElement(By.id("nloginpwd")).sendKeys(pwd);
        dr.findElement(By.id("loginsubmit")).click();
        //System.out.println(dr.getPageSource());
        Thread.sleep(2000);
        return dr;
    }

    public WebDriver JDBeanPage(){
        WebElement dropDown = dr.findElement(By.xpath(myJDXPATH));
        Actions act = new Actions(dr);
        act.click(dropDown).perform();
        dr.get(dr.findElement(By.xpath(myBeanXPATH)).getAttribute("href"));
        return dr;
    }

    public List getDatesDetail(WebDriver dr){
        List<WebElement> e = dr.findElements(By.xpath(datesXPATH));
        List result = new ArrayList<String>();
        for (int i = 0; i < e.size(); i ++){
            result.add(e.get(i).getText());
        }

        return result;
    }

    public List getTransacDetail(WebDriver dr){
        List<WebElement> e = dr.findElements(By.xpath(transacDetailsXPATH));
        List result = new ArrayList<String>();
        for (int i = 0; i < e.size(); i ++){
            result.add(e.get(i).getText());
        }

        return result;
    }

    public List getTransacName(WebDriver dr){
        List<WebElement> e = dr.findElements(By.xpath(transacNameXPATH));
        List result = new ArrayList<String>();
        for (int i = 0; i < e.size(); i ++){
            result.add(e.get(i).getText());
        }

        return result;
    }

    public void getJDBeanDetails(){

        total = dr.findElement(By.cssSelector("strong.ftx01.num")).getText();

        //dates that have transactions
        dateDetail = getDatesDetail(dr);
        //gain or lost JDBean
        transacDetail = getTransacDetail(dr);
        //name of transac
        transacName = getTransacName(dr);

        Actions act = new Actions(dr);

        //get pages size
        List<WebElement> pageHref = dr.findElements(By.xpath("//div[@class='pagin fr']/a[not(@class) and @href]"));
        if(pageHref.size() != 0) {//该用户京豆交易明细至多只有一页的情况
            act.moveToElement(pageHref.get(0));
            act.click().perform();
        }
        //System.out.print(dr.getPageSource());

        for (int i = 1; i < pageHref.size(); i ++) {
            WebElement pageIcon = dr.findElement(By.xpath(pageNextXPATH));
            act.moveToElement(pageIcon);
            act.click().perform();

            //date
            dateDetail.addAll(getDatesDetail(dr));

            //gain or lost
            transacDetail.addAll(getTransacDetail(dr));

            //name of transac
            transacName.addAll(getTransacName(dr));

        }

        maskItemNumber(transacName);

        System.out.println("共有京豆：" + total + "，明细如下：\n==============================================================================");

        for(int i = 0; i < transacName.size(); i ++){
            System.out.println(dateDetail.get(i)+"\t\t\t\t\t\t"+transacDetail.get(i)+"\t\t\t\t\t\t"+transacName.get(i));
        }
    }

    public void maskItemNumber(List<String> transacName){
        StringBuffer s ;
        for (int i = 0; i < transacName.size(); i ++){
            if(transacName.get(i).toString().contains("商品")){
                s = new StringBuffer(transacName.get(i).toString());
                for(int j = 0; j < s.length(); j ++){
                    if (Character.isDigit(s.charAt(j)))
                        s.setCharAt(j,'*');
                }
                transacName.set(i,s.toString());
            }
        }
    }


    public void tearDown() throws Exception{
        dr.quit();
    }

    public static void main(String[] args) throws Exception {
        String url = "https://passport.jd.com/uc/login?ltype=logout";

        Scanner s = new Scanner(System.in);
        System.out.println("请输入帐号:");
        String account = s.nextLine();

        System.out.println("请输入密码:");
        String pwd = s.nextLine();

        String myJDXPATH = "//li[@id='ttbar-myjd']/div[1]/i[@class='ci-right']/s";
        String myBeanXPATH = "//li[@id='ttbar-myjd']/div[2]/div[@class='otherlist']/div[@class='fore2']/div[2]/a";
        String datesXPATH = "//span[@class='ftx03']";
        String transacDetailsXPATH = "//table[@class='tb-void']/tbody/tr/td[2]/span";
        String transacNameXPATH = "//table[@class='tb-void']/tbody/tr/td[3]";
        String pageNextXPATH = "//div[@class='pagin fr']/a[@class='current']/following-sibling::a[1]";
        JDLogin jd = new JDLogin(url,  account,  pwd, myJDXPATH,  myBeanXPATH, datesXPATH,  transacDetailsXPATH,  transacNameXPATH,  pageNextXPATH);
        jd.login();
        jd.JDBeanPage();
        jd.getJDBeanDetails();
        jd.tearDown();
    }
}