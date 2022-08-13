package cn.jiayeli.dataLeaf;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;


@SpringBootApplication(scanBasePackages = {"cn.jiayeli.dataLeaf"}, exclude = {GroovyTemplateAutoConfiguration.class})
//@MapperScan("com.spj.campous_pub_opin_monit.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

