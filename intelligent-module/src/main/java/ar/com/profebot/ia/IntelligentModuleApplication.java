package ar.com.profebot.ia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class IntelligentModuleApplication {

	private static String[] args;
	private static ConfigurableApplicationContext context;

	public static void main(String[] anArgs) {
		args = anArgs;
		context = SpringApplication.run(IntelligentModuleApplication.class, args);
	}
}
