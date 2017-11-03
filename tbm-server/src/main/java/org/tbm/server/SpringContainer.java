package org.tbm.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/10/25.
 */
public class SpringContainer {
    private static ClassPathXmlApplicationContext applicationContext;
    private AtomicBoolean start = new AtomicBoolean(false);

    public static Object getBean(Class<?> clazz) {
        if (null == applicationContext) {
            throw new IllegalStateException("Spring Container Uninitialized");
        }

        return applicationContext.getBean(clazz);
    }

    public void start(String path) {
        if (start.compareAndSet(false, true)) {
            applicationContext = new ClassPathXmlApplicationContext();
            applicationContext.setConfigLocations(path.split("[,\\s]+"));
            applicationContext.refresh();
            applicationContext.start();
        }
    }

    public void stop() {
        if (start.compareAndSet(true, false)) {
            applicationContext.close();
        }
    }

}
