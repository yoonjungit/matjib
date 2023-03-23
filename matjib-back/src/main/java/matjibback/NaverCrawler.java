package matjibback;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class NaverCrawler {
    static String searchRes = null;

    public static void printNaverScore() {
        //1. 크롬 드라이버 설정
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        String WEB_DRIVER_PATH = "/Users/jihun/Documents/chromedriver/chromedriver";
        // (현 버전-111이 버그가 있어서 다음과 같이 입력해야함)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        //2. 음식점 검색
        searchRes = "카페";      //검색할 음식점
        String searchURL = "https://m.map.naver.com/search2/search.naver?query=" + searchRes;       //네이버지도 모바일 검색
        driver.get(searchURL);      //검색url 불러오기
        //검색했을 때 첫번째 보이는 음식점(가게) 이름 가져오기
        WebElement firstRes = driver.findElement(By.cssSelector("#ct > div.search_listview._content._ctList > ul > li > div.item_info > a.a_item.a_item_distance._linkSiteview > div > strong"));
        String firstResName = firstRes.getText();

        if (firstResName != null) {       //이름 제대로 받아오면
            //음식점 코드 가져오기
            WebElement res = driver.findElement(By.cssSelector("#ct > div.search_listview._content._ctList > ul > li"));
            String resCode = res.getAttribute("data-sid");        //11672261
            //음식점 상세페이지로 이동
            String url = "https://m.place.naver.com/restaurant/" + resCode;
            driver.get(url);
            //음식점 평점 가져오기
            WebElement review = driver.findElement(By.xpath("//*[@id=\"app-root\"]/div/div/div/div[2]/div[1]/div[2]/span[1]/em"));
            //음식점 주소 가져오기
            WebElement add = driver.findElement(By.xpath("//*[@id=\"app-root\"]/div/div/div/div[6]/div/div[2]/div/div/div[1]/div/a/span[1]"));
            String address = add.getText();
            //프린트
            System.out.println("이름 : " + firstResName);
            System.out.println("주소 : " + address);
            System.out.println("네이버 평점 : " + review.getText());
            //4-2. 일치하지 않으면
        } else {        //이름 받아오지 못하면
            System.out.println("음식점이 없습니다.");
        }
    }
}
