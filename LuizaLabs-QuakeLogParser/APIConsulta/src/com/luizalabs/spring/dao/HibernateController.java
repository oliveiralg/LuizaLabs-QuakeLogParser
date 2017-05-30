package com.luizalabs.spring.dao;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/*
 * Classe de controle do SessionFactory e ServiceRegistry hibernate
 * 
 */
public class HibernateController {

  private static SessionFactory sessionFactory = null;
  private static ServiceRegistry serviceRegistry = null;

  public static SessionFactory configureSessionFactory() throws HibernateException {
    Configuration configuration = new Configuration();
    configuration.configure();

    Properties properties = configuration.getProperties();

    if (serviceRegistry == null)
      serviceRegistry =
          new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
    if (sessionFactory == null)
      sessionFactory = configuration.buildSessionFactory(serviceRegistry);

    return sessionFactory;
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public static void setSessionFactory(SessionFactory sessionFactory) {
    HibernateController.sessionFactory = sessionFactory;
  }

  public static ServiceRegistry getServiceRegistry() {
    return serviceRegistry;
  }

  public static void setServiceRegistry(ServiceRegistry serviceRegistry) {
    HibernateController.serviceRegistry = serviceRegistry;
  }

}
