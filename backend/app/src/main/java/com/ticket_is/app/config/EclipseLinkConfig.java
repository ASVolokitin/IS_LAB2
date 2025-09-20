package com.ticket_is.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class EclipseLinkConfig extends JpaBaseConfiguration {

    protected EclipseLinkConfig(DataSource dataSource, JpaProperties properties,
            ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
        super(dataSource, properties, jtaTransactionManager);
        //TODO Auto-generated constructor stub
    }

    
   // EclipseLink JPA Adaptor
   @Override
   protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
      EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
      adapter.setDatabasePlatform("org.eclipse.persistence.platform.database.PostgreSQLPlatform");
      return adapter;
   }


   @Override
    protected Map<String, Object> getVendorProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put(PersistenceUnitProperties.WEAVING, "false");
        map.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.INFO_LABEL); 
        map.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_ONLY);
        map.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
        
        // Оптимизации для PostgreSQL
        map.put(PersistenceUnitProperties.BATCH_WRITING, "JDBC");
        // map.put(PersistenceUnitProperties.JDBC_BATCH_WRITING_SIZE, "1000");
        
        return map;
    }


   @Override
   protected Map<String, Object> getVendorProperties(DataSource dataSource) {
    return getVendorProperties();
   }
}
