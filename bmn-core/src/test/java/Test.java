import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Test {


    public static void main(String[] args) {


        Type ty = new TypeToken<Class<String>>() {
        }.getType();
    }
}
