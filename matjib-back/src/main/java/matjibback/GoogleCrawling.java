package matjibback;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class GoogleCrawling {
    public static void printGoogleScore(String... args) {
        WebDriver driver = null;
        try {
            // drvier 설정
            System.setProperty("webdriver.chrome.driver", "/Users/jihun/Documents/chromedriver/chromedriver");
            // Chrome 드라이버 인스턴스 설정

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);

            // 스크립트를 사용하기 위한 캐스팅
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // URL로 접속
            driver.get("https://www.google.co.kr/maps");
            // 대기 설정
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            // xpath로 element를 찾는다. 이 xpath는 구글지도(www.google.co.kr/maps)의 검색어 입력창이다.
            WebElement element = driver.findElement(By.xpath("/html/body/div[3]/div[9]/div[3]/div[1]/div[1]/div[1]/div[2]/form/div[2]/div[3]/div/input[1]"));
            element.sendKeys("리버레인");

            // xpath로 '검색' 버튼 찾기
            WebElement searchBtn = driver.findElement(By.xpath("/html/body/div[3]/div[9]/div[3]/div[1]/div[1]/div[1]/div[2]/div[1]/button"));

            // 클릭한다. 사실 element.click()로도 클릭이 가능한데 가끔 호환성 에러가 발생하는 경우가 있다.
            js.executeScript("arguments[0].click();", searchBtn);

            // 2초 기다린다.
            Thread.sleep(2000);

            // xpath로 '검색 결과 최상위 항목' 버튼 찾기
            WebElement firstResult = driver.findElement(By.xpath("/html/body/div[3]/div[9]/div[9]/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]/div[1]/div[3]/div/a"));

            // 클릭한다. 사실 element.click()로도 클릭이 가능한데 가끔 호환성 에러가 발생하는 경우가 있다.
            js.executeScript("arguments[0].click();", firstResult);

            // 2초 기다린다.
            Thread.sleep(2000);

            // 평점 xpath의 텍스트를 가져온다. getText()
            String rateNum = driver.findElement(By.xpath("/html/body/div[3]/div[9]/div[9]/div/div/div[1]/div[3]/div/div[1]/div/div/div[2]/div[2]/div/div[1]/div[2]/div/div[1]/div[2]/span[1]/span[1]")).getText();
            System.out.println("구글 평점 : " + rateNum);

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }
}