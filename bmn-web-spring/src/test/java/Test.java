import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */
public class Test {
    public static void main(String[] args) throws IntrospectionException {
        List<String> keys = new ArrayList<>(2);
        keys.add("0");
        keys.add("2");
        String a = "key.name";
        a+="[" +
                StringUtils.collectionToDelimitedString(keys, "]" + "[") +
                "]";
        System.out.println(a);

        int d = PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(a);
        System.out.println(d);


        BeanInfo delegate = Introspector.getBeanInfo(Test.class);
        MethodDescriptor[] methodDescriptors = delegate.getMethodDescriptors();
        for (MethodDescriptor md : methodDescriptors) {
            boolean t = Test.isCandidateWriteMethod(md.getMethod());
            System.out.println("name : "+ md.getName() + " is candidate: " + t);
        }


        String v = "";
        String s = "1231中国";
        byte[] stra = s.getBytes();
        for(byte b : stra) {
            if(b >= 48 && b <= 57) {
                char ax = (char)(b & 0xff);
                System.out.println(ax);
            }
        }

    }


    public void setName(String name){
    }

    public static void setNames(String name) {

    }

    public String setNamer(String name) {
        return "";
    }

    public static void setName2(int inted, String name) {

    }

    public  String setName3(int inted, String name) {
        return "";
    }



    public static boolean isCandidateWriteMethod(Method method) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        int nParams = parameterTypes.length;
        return (methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) &&
                (!void.class.isAssignableFrom(method.getReturnType()) || Modifier.isStatic(method.getModifiers())) &&
                (nParams == 1 || (nParams == 2 && parameterTypes[0].equals(int.class))));
    }
}
