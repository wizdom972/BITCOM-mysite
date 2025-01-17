package mysite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import mysite.config.app.DBConfig;
import mysite.config.app.MyBatisConfig;
import mysite.config.web.SecurityConfig;

@Controller
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import({DBConfig.class, MyBatisConfig.class, SecurityConfig.class})
@ComponentScan(basePackages = { "mysite.service", "mysite.repository", "mysite.aspect" })
public class AppConfig {

}
