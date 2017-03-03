package onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class OnlinejudgeMsJudgehostApplication extends SpringBootServletInitializer{
	 @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OnlinejudgeMsJudgehostApplication.class);
    }
	public static void main(String[] args) {
		SpringApplication.run(OnlinejudgeMsJudgehostApplication.class, args);
	}
}
