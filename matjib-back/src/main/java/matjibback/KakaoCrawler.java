package matjibback;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class KakaoCrawler {
    public static void printKakaoScore() {
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
            driver.get("https://map.kakao.com");
            // 대기 설정
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            // xpath로 element를 찾는다. 이 xpath는 구글지도(www.google.co.kr/maps)의 검색어 입력창이다.
            WebElement element = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/fieldset/div[1]/input"));
            element.sendKeys("카페");

            // xpath로 '검색' 버튼 찾기
            WebElement searchBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/fieldset/div[1]/button"));

            // 클릭한다. 사실 element.click()로도 클릭이 가능한데 가끔 호환성 에러가 발생하는 경우가 있다.
            js.executeScript("arguments[0].click();", searchBtn);

            // 2초 기다린다.
            //Thread.sleep(2000);

            // 이름 xpath의 텍스트를 가져온다. getText()
            String name = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div[1]/div[7]/div[4]/ul/li[1]/div[3]/strong/a[2]")).getText();

            if (name != null) {
                // 주소 xpath의 텍스트를 가져온다. getText()
                String address = driver.findElement(By.xpath("//*[@id=\"info.search.place.list\"]/li[1]/div[5]/div[2]/p[1]")).getText();

                // 평점 xpath의 텍스트를 가져온다. getText()
                String rateNum = driver.findElement(By.xpath("//*[@id=\"info.search.place.list\"]/li[1]/div[4]/span[1]/em")).getText();

                // 프린트
                System.out.println("이름 : " + name);
                System.out.println("주소 : " + address);
                System.out.println("카카오 평점 : " + rateNum);
            } else {        //이름 받아오지 못하면
                System.out.println("음식점이 없습니다.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }
}