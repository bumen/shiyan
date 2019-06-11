package com.bmn.bootstrap.order;

import com.bmn.bootstrap.annotation.Order;
import com.bmn.bootstrap.util.AnnotationUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * 排序。数越h优先级越高。实现的接口将被先执行
 *
 * @author 562655151@qq.com
 * @date 2019/5/17
 */
public class AnnotationAwareOrderComparator extends OrderComparator {

    public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();

    @Override
    protected int getOrder(Object obj) {
        if (obj instanceof Ordered) {
            return ((Ordered) obj).getOrder();
        }
        if (obj != null) {
            Class<?> clazz = (obj instanceof Class ? (Class) obj : obj.getClass());
            Order order = AnnotationUtils.findAnnotation(clazz, Order.class);
            if (order != null) {
                return order.value();
            }
        }
        return Ordered.LOWEST_PRECEDENCE;
    }

    public static void sort(List<?> list) {
        if (list.size() > 1) {
            Collections.sort(list, INSTANCE);
        }
    }

    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }
    }
}
