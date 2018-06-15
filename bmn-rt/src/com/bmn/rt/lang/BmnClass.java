
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.rt.lang;

import com.bmn.bean.BranchBean;
import com.bmn.bean.LeafBean;
import com.bmn.bean.Person;
import com.bmn.bean.Student;
import com.bmn.bean.TreeBean;
import com.bmn.bean.TreeInterface;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.sound.midi.Soundbank;

/**
 * 
 *
 * @date 2018-04-08
 * @author 562655151@qq.com
 */
public class BmnClass {

    /**
     * <code> implements io.Serializable, GenericDeclaration, Type, AnnotatedElement</code>
     * <p>
     * 
     * <pre>
     * GenericDeclaration: 
     * 1. 声明了可以定义泛型的位置 , 通过继承GenericDeclaration来确定位置
     *   - Class
     *   - Constructor
     *   - Method
     * </pre>
     * 
     * <pre>
     * Type : 
     * 1. 声明了定义泛型的类型， 通过继承Type来确定有哪些类型
     *   - Class 类类型
     *   - GenericArrayType  泛型数组 T[]
     *   - ParameterizedType 泛型参数 <T>
     *   - TypeVariable      泛型变量 T
     *   - WildcardType      通配符类型 *
     * </pre>
     * 
     * <pre>
     * AnnotatedElement:
     * 1. 声明了可以定义注解的位置， 通过继承AnnotatedElement来确定位置
     *   - Class
     *   - Constructor
     *   - Method
     *   - Field
     *   - Package
     *   - Parameter
     *   - TypeVariable
     * </pre>
     * 
     */
    private Class<?> clazz;


    public static void main(String[] args) {
        Method[] methods = BranchBean.class.getMethods();

        for(Method m : methods) {
            System.out.println( m.getName() + " " + m.getDeclaringClass());
        }

        System.out.println(BranchBean.class.isAssignableFrom(TreeBean.class));
        System.out.println(TreeBean.class.isAssignableFrom(BranchBean.class));

        System.out.println(Integer.class.isAssignableFrom(int.class));

        System.out.println(int.class.isAssignableFrom(Integer.class));

        System.out.println(int.class);
        System.out.println(Integer.class);

        System.out.println(TreeBean.class.isInterface());


        methods = LeafBean.class.getMethods();
        outMethod(methods);

        System.out.println("===========");

        methods = LeafBean.class.getDeclaredMethods();
        outMethod(methods);


        Type type = Student.class.getGenericSuperclass();
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            System.out.println(parameterizedType.getRawType());
            System.out.println(parameterizedType.getTypeName());
            System.out.println(parameterizedType.getOwnerType());
            Type[] argsType = parameterizedType.getActualTypeArguments();
            for (Type t : argsType) {
                if(t instanceof  Class<?>) {
                    Class<?> clazz = (Class<?>)t;
                System.out.println(clazz);
                }
            }
        }

        outSuperClass(Student.class);

        outSuperClass(TreeInterface.class);

        outSuperInterface(Student.class);
        outSuperInterface(TreeInterface.class);

        TypeVariable<Class<Person>>[] vars = Person.class.getTypeParameters();
        for (TypeVariable<Class<Person>> tt : vars) {
            System.out.println(tt);

            Type[] types = tt.getBounds();
            for(Type t : types) {
                System.out.println("=== " + t);
            }
        }

        Type[] types = Student.class.getGenericInterfaces();
        for (Type t : types) {
            System.out.println(t.getClass());
        }


        Class<?> classes = String[][].class.getComponentType();
        System.out.println(classes);


    }

    private static void outSuperClass(Class<?> clazz) {
        System.out.println("--------super class");
        Class<?> clazzT = clazz;
        while(clazzT != null) {
            System.out.println(clazzT);
            clazzT = clazzT.getSuperclass();
        }
        System.out.println("--------super class------");
    }

    private static void outSuperInterface(Class<?> clazz) {
        System.out.println("--------super interface");
        for(Class<?> it : clazz.getInterfaces()) {
            System.out.println(it);
        }
        System.out.println("--------super interface------");
    }

    private static void outMethod(Method[] methods) {
        for(Method m : methods) {
            System.out.println( m.getName() + " " + m.getDeclaringClass());
        }
    }
}
