package com.syrnik.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "centralEntityManagerFactory",
        transactionManagerRef = "centralTransactionManager",
        basePackages = {"com.syrnik.repository.central"})
public class CentralDatabaseConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("datasource.central-db")
    public DataSource centralDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean centralEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("centralDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("com.syrnik.model.central")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect"))
                .persistenceUnit("central").build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager centralTransactionManager(
            @Qualifier("centralEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
