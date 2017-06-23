package org.tbm.admin;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String configPath = servletContextEvent.getServletContext().getInitParameter("tbm.admin.config.path");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
