package matjibback.naverLogin;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "matjibback.naverLogin")
public class NaverLoginConfig {

}
