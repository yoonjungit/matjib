package matjibback;

import org.checkerframework.checker.units.qual.K;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatjibBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatjibBackApplication.class, args);

		GoogleCrawling google = new GoogleCrawling();
		google.printGoogleScore();

		KakaoCrawling kakao = new KakaoCrawling();
		kakao.printKakaoScore();
	}

}
