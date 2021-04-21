package com.bitacademy.mysite.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ContextLoadListener
 *
 */
@WebListener
public class ContextLoadListener implements ServletContextListener {

    public ContextLoadListener() {
        System.out.println("context loaded ");
    }

    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

    public void contextInitialized(ServletContextEvent sce)  {
    	ServletContext context = sce.getServletContext();
    	String contextConfigLocation = context.getInitParameter("contextConfigLocation");
    	System.out.println("Application starts " + contextConfigLocation);
    }
	
}
