package com.syrnik.config;

import jakarta.persistence.EntityManagerFactory;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.Data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "branchEntityManagerFactory",
      transactionManagerRef = "branchTransactionManager", basePackages = {"com.syrnik.repository.branch"})
@ConfigurationProperties(prefix = "datasource")
public class BranchDatabaseConfiguration {

    private Map<String, DataSourceProperties> branches;

    @Bean
    public MultiTenantDataSource branchDataSource() {
        MultiTenantDataSource branchDataSource = new MultiTenantDataSource();

        Map<Object, Object> resolvedDataSources = new HashMap<>();

        for(Map.Entry<String, DataSourceProperties> entry : branches.entrySet()) {
            String dataSourceName = entry.getKey();
            DataSourceProperties properties = entry.getValue();

            DataSource dataSource = properties.initializeDataSourceBuilder().build();

            resolvedDataSources.put(dataSourceName, dataSource);
        }

        Optional<Object> dataSourceOptional = resolvedDataSources.values().stream().findFirst();
        branchDataSource.setTargetDataSources(resolvedDataSources);
        branchDataSource.setDefaultTargetDataSource(dataSourceOptional.orElseThrow(IllegalStateException::new));

        return branchDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean branchEntityManagerFactory(EntityManagerFactoryBuilder builder,
          @Qualifier("branchDataSource") DataSource dataSource) {
        return builder
              .dataSource(dataSource)
              .packages("com.syrnik.model.branch")
              .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect"))
              .persistenceUnit("branch")
              .build();
    }

    @Bean
    public PlatformTransactionManager branchTransactionManager(
          @Qualifier("branchEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.branches.liquibase")
    public LiquibaseProperties branchLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public List<SpringLiquibase> branchLiquibaseList(
          @Qualifier("branchDataSource") MultiTenantDataSource branchDataSource,
          @Qualifier("branchLiquibaseProperties") LiquibaseProperties liquibaseProperties) throws LiquibaseException {
        List<SpringLiquibase> liquibaseInstances = new ArrayList<>();

        Map<Object, DataSource> targetDataSources = branchDataSource.getResolvedDataSources();
        for(DataSource dataSource : targetDataSources.values()) {
            SpringLiquibase springLiquibase = new SpringLiquibase();
            springLiquibase.setDataSource(dataSource);
            springLiquibase.setChangeLog(liquibaseProperties.getChangeLog());
            springLiquibase.setContexts(liquibaseProperties.getContexts());
            springLiquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
            springLiquibase.afterPropertiesSet();

            liquibaseInstances.add(springLiquibase);
        }

        return liquibaseInstances;
    }
}
