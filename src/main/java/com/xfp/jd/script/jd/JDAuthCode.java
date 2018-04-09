package com.xfp.jd.script.jd;

import com.xfp.jd.script.utils.VCodeCheckUtils;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class JDAuthCode {

    public boolean authCode(WebDriver driver) throws IOException, InterruptedException, AWTException {

        WebElement webElement = driver.findElement(By.id("o-authcode"));
        if ((webElement == null)|| (!webElement.isDisplayed())) {
            submit(driver);
            if(isLogin(driver))
            {
                return true;
            }
            return authCode(driver);
        }

//        File fng = createElementImage(driver);
//        if (fng == null) {
//            submit(driver);
//            return false;
//        }

//        try {
//            ImageIO.write(grayImage, "png", new File("E:\\code\\test\\script\\img", "vc1.png"));
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        String path1 = "E:\\code\\test\\script\\img\\image.jpg";
//
//        File imageFile1 = new File(path1);
//        String result = null;
//        ITesseract instance = new Tesseract();
//        instance.setDatapath("E:\\code\\test\\script\\tessdata");
//        try {
//            result = instance.doOCR(fng);
//        } catch (TesseractException e1) {
//            e1.printStackTrace();
//        }
//        result = result.replaceAll("[^a-z^A-Z^0-9]", "");
       // String authCode = VCodeCheckUtils.authCode(fng.getCanonicalPath());
        Scanner s = new Scanner(System.in);
        System.out.println("请输入验证码:");
        String authCode = s.nextLine();
        System.out.println("验证码： " + authCode);
        if (StringUtils.isEmpty(authCode)) {
            inputAutoCode(driver, "aaaa");
            submit(driver);
            return false;
        }

        inputAutoCode(driver, authCode);

        return isLogin(driver);
    }

    private boolean isLogin(WebDriver driver)
    {
        return StringUtils.startsWith(driver.getCurrentUrl(),"https://www.jd.com");
    }

    private static void inputAutoCode(WebDriver driver, String authCode) {
        WebElement authcodeElement = driver.findElement(By.id("authcode"));
        authcodeElement.clear();
        authcodeElement.sendKeys(authCode);

        submit(driver);
    }

    private static void submit(WebDriver driver) {
        driver.findElement(By.id("loginsubmit")).click();
    }


    public File createElementImage(WebDriver driver) throws IOException, InterruptedException, AWTException {
        WebElement ele = driver.findElement(By.id("JD_Verification1"));
        if (ele == null) {
            return null;
        }
        //验证码不展示，不需要输入
        if (!ele.isDisplayed()) {
            return null;
        }

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Point p = ele.getLocation();
        int width = ele.getSize().getWidth();
        int higth = ele.getSize().getHeight();
        BufferedImage img = ImageIO.read(scrFile);
        BufferedImage dest = img.getSubimage(1480, 562, width, higth + 20);
        ImageIO.write(dest, "jpg", scrFile);
        Thread.sleep(1000);
        File fng = new File("E:\\code\\test\\script\\img\\image.jpg");
        if (fng.exists()) {
            fng.delete();
        }
        FileUtils.copyFile(scrFile, fng);

        return fng;
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        BufferedImage fullImg = ImageIO.read(screenshot);
//
//        // Get the location of element on the page
//        Point point = ele.getLocation();
//
//        // Get width and height of the element
//        int eleWidth = ele.getSize().getWidth();
//        int eleHeight = ele.getSize().getHeight();
//
//        // Crop the entire page screenshot to get only element screenshot
//        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(),
//                eleWidth, eleHeight);
//        ImageIO.write(eleScreenshot, "png", screenshot);
//
//        return eleScreenshot;

//        Dimension size = webElement.getSize();
//
//        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
//        Point location = webElement.getLocation();
//        BufferedImage croppedImage = originalImage.getSubimage(location.getX(), location.getY(), size.getWidth(), size.getHeight());//linux注释下面一行代码，取消此行和上一行注释，可正确截图
//        //BufferedImage croppedImage = originalImage.getSubimage(x, y, size.getWidth() + width, size.getHeight() + heigth);//windows下，浏览器最大化后浏览器截图，获取的x、y坐标不可直接使用，推测和屏幕分辨率有关。
//        return croppedImage;
    }


}
