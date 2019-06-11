package com.bmn.bootstrap;

import com.bmn.bootstrap.config.ApplicationConfig;
import com.bmn.bootstrap.context.ApplicationContext;
import com.bmn.bootstrap.context.BmnApplicationContext;
import com.bmn.bootstrap.context.BmnSofaRpcApplicationContext;
import com.bmn.bootstrap.exception.BeanInstantiationException;
import com.bmn.bootstrap.listener.ApplicationListener;
import com.bmn.bootstrap.listener.ApplicationRunListener;
import com.bmn.bootstrap.listener.EventPublishingRunListener;
import com.bmn.bootstrap.order.AnnotationAwareOrderComparator;
import com.bmn.bootstrap.runner.ApplicationRunner;
import com.bmn.bootstrap.support.ClassPathComponentLoader;
import com.bmn.bootstrap.util.ClassUtils;
import com.bmn.bootstrap.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心服务器启动类
 * <pre>
 * 启过程分为多个阶段：
 * 1. starting： 开始启动事件
 * 2. contextLoaded ： 加载完上下文事件
 * 3. refresh ： 服务器刷新事件
 * 4. 可以加一个started。目录未实现
 * 5. failed：处理失败
 * 说明：每一个阶段都通过触发相应事件通知。来实现不同业务功能
 *   + 如：配置初始化
 *   + 事件可以在服务器运行过程中。只要获取到ApplicationContext就可以随时触发。
 *   所以注意监听器中，在接收到重复事件的处理
 * </pre>
 *
 *
 * runner执行 + runner是服务器启阶段完成后调用。只执行一次，一般用于服务模块启动
 *
 * @author 562655151@qq.com
 * @date 2019/5/17
 */
public class BmnApplication {

    private static final Logger logger = LoggerFactory.getLogger(BmnApplication.class);

    /**
     * 负责接收ComponentScan，获取需要扫描的包
     */
    private Class<?> primaryClass;

    /**
     * 服务器需要初始化的上下文类
     */
    private Class<? extends BmnApplicationContext> applicationContextClass;

    /**
     * 监听器。监听启动过程。服务器运行事件
     */
    private List<ApplicationListener<?>> listeners;

    /**
     * 核心类加载
     */
    ClassPathComponentLoader loader;
    /**
     * 服务启动时执行一次
     */
    private List<ApplicationRunner> runners;

    /**
     * 创建时。需要加载ComponentScan指定的包
     */
    public BmnApplication(Class<?> primaryClass) {
        this.primaryClass = primaryClass;

        loader = new ClassPathComponentLoader();
        loader.load(primaryClass);

        // 获取监听器
        setListeners((Collection) getTypeInstances(ApplicationListener.class));
        // 获取runner
        setRunners(getTypeInstances(ApplicationRunner.class));
    }

    /**
     * 启动过程
     */
    private ApplicationContext run(String type, String[] args) {
        BmnApplicationContext context = null;

        // 启动开始事件
        ApplicationRunListeners listeners = getRunListeners(args);
        listeners.starting();

        logger.info("launcher starting...");

        try {
            //加载配置
            ApplicationConfig.getInstance().load();
            //创建服务器主类对象
            context = createApplicationContext(type);

            prepareContext(context);
            //上下文内部初始化完成后触发。
            listeners.contextLoaded(context);
            //刷新
            context.refresh();

            listeners.started(context);

            listeners.running(context);

            //此时服务器所有需要的资源都准备好。可以启动外部模块

            //执行runner
            callRunners(context, args);

            logger.info("SERVER STARTED");
        } catch (Throwable ex) {
            // 启动时错误处理
            handleRunFailure(context, ex, listeners);
            throw new IllegalStateException(ex);
        }

        return context;
    }

    /**
     * 调用runner
     */
    private void callRunners(BmnApplicationContext context, String[] args) {
        List<ApplicationRunner> runners = this.runners;
        AnnotationAwareOrderComparator.sort(runners);
        for (ApplicationRunner runner : runners) {
            callRunner(runner, context, args);
        }
    }

    private void callRunner(ApplicationRunner runner, BmnApplicationContext context,
        String[] args) {
        try {
            (runner).run(context, args);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to execute ApplicationRunner", ex);
        }
    }

    private void prepareContext(BmnApplicationContext context) {
        context.registerShutdownHook();
    }

    private ApplicationRunListeners getRunListeners(String[] args) {
        ApplicationRunListener listener = new EventPublishingRunListener(this, args);

        //一个facade接口
        ApplicationRunListeners listeners = new ApplicationRunListeners(Arrays.asList(listener));
        return listeners;
    }

    private <T> Collection<? extends T> getTypeInstances(Class<T> type) {
        return getTypeInstances(type, new Class<?>[]{});
    }

    private <T> Collection<? extends T> getTypeInstances(Class<T> type,
        Class<?>[] parameterTypes, Object... args) {
        Set<Class<? extends T>> classes = loader.getSubTypesOf(type);

        List<? extends T> instances = createSpringFactoriesInstances(type, parameterTypes, args,
            classes);
        AnnotationAwareOrderComparator.sort(instances);
        return instances;
    }

    private <T> List<? extends T> createSpringFactoriesInstances(Class<T> type,
        Class<?>[] parameterTypes, Object[] args,
        Set<Class<? extends T>> classes) {
        List<T> instances = new ArrayList<>(classes.size());
        for (Class<?> clazz : classes) {
            try {
                if (!type.isAssignableFrom(clazz)) {
                    continue;
                }
                Constructor<?> constructor = clazz
                    .getDeclaredConstructor(parameterTypes);
                T instance = (T) ClassUtils.instantiateClass(constructor, args);
                instances.add(instance);
            } catch (Throwable ex) {
                throw new IllegalArgumentException(
                    "Cannot instantiate " + type + " : " + clazz.getName(), ex);
            }
        }
        return instances;
    }


    public void setListeners(Collection<? extends ApplicationListener<?>> listeners) {
        this.listeners = new ArrayList<>();
        this.listeners.addAll(listeners);
    }

    public void setRunners(Collection<? extends ApplicationRunner> runners) {
        this.runners = new ArrayList<>();
        this.runners.addAll(runners);
    }

    /**
     * 添加监听器
     */
    public void addListeners(ApplicationListener<?>... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    /**
     * 自定义上下文
     */
    public void setApplicationContextClass(
        Class<? extends BmnApplicationContext> applicationContextClass) {
        this.applicationContextClass = applicationContextClass;
    }

    public Set<ApplicationListener<?>> getListeners() {
        return asUnmodifiableOrderedSet(this.listeners);
    }

    protected BmnApplicationContext createApplicationContext(String type) {
        Class<?> contextClass = this.applicationContextClass;
        if (contextClass == null) {
            if (type != null && "rpc".equals(type)) {
                contextClass = BmnSofaRpcApplicationContext.class;
            } else {
                contextClass = BmnApplicationContext.class;
            }
        }
        try {
            return (BmnApplicationContext) ClassUtils
                .instantiateClass(contextClass.getDeclaredConstructor());
        } catch (NoSuchMethodException e) {
            throw new BeanInstantiationException(
                "create application contenxt: " + contextClass + " error");
        }
    }

    /**
     * 排序。需要实现{@link com.bmn.bootstrap.order.Ordered}或使用注解{@link com.bmn.bootstrap.order.Ordered}
     */
    private static <E> Set<E> asUnmodifiableOrderedSet(Collection<E> elements) {
        List<E> list = new ArrayList<>(elements);
        list.sort(AnnotationAwareOrderComparator.INSTANCE);
        return new LinkedHashSet<>(list);
    }

    /**
     * 启动失败处理
     */
    private void handleRunFailure(ApplicationContext context,
        Throwable exception,
        ApplicationRunListeners listeners) {
        try {
            try {
                if (listeners != null) {
                    listeners.failed(context, exception);
                }
            } finally {
                if (context != null) {
                    context.close();
                }
            }
        } catch (Exception ex) {
            logger.warn("Unable to close ApplicationContext", ex);
        }

        ReflectionUtils.rethrowRuntimeException(exception);
    }

    public static ApplicationContext run(Class<?> primaryClass, String[] args) {
        return run(primaryClass, null, args);
    }

    public static ApplicationContext run(Class<?> primaryClass, String type, String[] args) {
        return new BmnApplication(primaryClass).run(type, args);
    }
}
