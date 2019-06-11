import java.io.IOException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AnnotationConfigApplicationContext configApplicationContext;
        ApplicationRunner runner;

        String path = "com/bmn/spring/boot/BmnSpringBootApplication";

//        ClassPathResource resource = new ClassPathResource();
//        resource.getInputStream();

        Class<?> c = Class.forName(path);
        System.out.println(c.getName());
    }


}
