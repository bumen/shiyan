package com.bmn.socket.netty.util;

import com.bmn.socket.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by Administrator on 2017/2/24.
 */
public abstract class ResourceLeakDetectorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLeakDetectorFactory.class);
    private static volatile ResourceLeakDetectorFactory factoryInstance = new DefaultResourceLeakDetectorFactory();

    public static ResourceLeakDetectorFactory instance() {
        return factoryInstance;
    }

    public static void setResourceLeakDetectorFactory(ResourceLeakDetectorFactory factoryInstance) {
        factoryInstance = factoryInstance;
    }

    public final <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource) {
        return newResourceLeakDetector(resource, ResourceLeakDetector.DEFAULT_SAMPLING_INTERVAL, Long.MAX_VALUE);
    }

    public abstract <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval, long maxActive);


    private static final class DefaultResourceLeakDetectorFactory extends ResourceLeakDetectorFactory {
        private final Constructor<?> customClassConstructor;

        DefaultResourceLeakDetectorFactory() {
            String customLeakDetector;
            try {
                customLeakDetector = AccessController.doPrivileged(new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return SystemPropertyUtil.get("io.netty.customResourceLeakDetector");
                    }
                });
            } catch (Throwable cause) {
                logger.error("Could not access System property: io.netty.customResourceLeakDetector", cause);
                customLeakDetector = null;
            }
            customClassConstructor = customLeakDetector == null ? null : customClassConstructor(customLeakDetector);
        }

        private static Constructor<?> customClassConstructor(String customLeakDetector) {
            try {
                final Class<?> detectorClass = Class.forName(customLeakDetector, true, io.netty.util.internal.PlatformDependent.getSystemClassLoader());
                if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
                    return detectorClass.getConstructor(Class.class, int.class, long.class);
                } else {
                    logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
                }
            } catch (Throwable throwable) {
                logger.error("Could not load custom resource leak detector class provided: {}", customLeakDetector, throwable);
            }
            return null;
        }

        @Override
        public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval, long maxActive) {
            if (customClassConstructor != null) {
                try {
                    ResourceLeakDetector<T> leakDetector = (ResourceLeakDetector<T>) customClassConstructor.newInstance(
                            resource, samplingInterval, maxActive
                    );
                    logger.debug("Loaded custom ResourceLeakDetector: {}", customClassConstructor.getDeclaringClass().getName());
                    return leakDetector;
                } catch (Throwable cause) {
                    logger.error("Could not load custom resource leak detector provided: {} with the given resource: {}",
                            customClassConstructor.getDeclaringClass().getName(), resource, cause);
                }
            }

            ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<>(resource, samplingInterval, maxActive);
            logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
            return resourceLeakDetector;
        }
    }

}
