package edu.shev.myApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// Имя класса абсолютно всегда и без исключений с большой буквы
public class myAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(myAppApplication.class, args);
	}

}
