package matjibback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatjibBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatjibBackApplication.class, args);

		GoogleCrawler google = new GoogleCrawler();
		google.printGoogleScore();

		KakaoCrawler kakao = new KakaoCrawler();
		kakao.printKakaoScore();

		NaverCrawler naver = new NaverCrawler();
		naver.printNaverScore();
	}

}
