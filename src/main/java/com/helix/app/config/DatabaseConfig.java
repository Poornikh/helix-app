package com.helix.app.config;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

	 
	@Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(emf.unwrap(SessionFactory.class));
        return transactionManager;
    }

	
	@Bean
	public HibernateTemplate initTemplate(EntityManagerFactory emf){
		return new HibernateTemplate(emf.unwrap(SessionFactory.class));
		
	}
	

	
	@PostConstruct
	public void startDBManager() {
			
		//hsqldb
		//DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:helixdb", "--user", "sa", "--password", "" });
	}

    
}
