package com.bmn.rt.java8;

import com.bmn.rt.annotation.Scheduled;
import com.bmn.rt.annotation.Schedules;
import java.lang.reflect.Method;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/17
 */
public class RepeatableAnnotationApi {


    @Scheduled("0 0/15 * * * ?")
    @Scheduled("0 0 12 * ?")
    public void task1() {}

//    @Schedules({@Scheduled("0 0/15 * * * ?"),
//        @Scheduled("0 0 12 * ?")})
//    public void task2() {}

    public static void main(String[] args) throws Exception{
        javaStyle();
        System.out.println("-----------");
//        springStyle();
    }


    private static void javaStyle() throws Exception{
        Method[] methods = RepeatableAnnotationApi.class.getMethods();
        for (Method method : methods) {
            Schedules schedules = method.getAnnotation(Schedules.class);
            if (schedules == null) {
                continue;
            }
            Scheduled[] values = schedules.value();
            if (values == null) {
                continue;
            }
            for (Scheduled scheduled : values) {
                System.out.println(scheduled.value());
            }
        }
    }


}
