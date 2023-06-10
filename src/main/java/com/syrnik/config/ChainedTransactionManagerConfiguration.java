package com.syrnik.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChainedTransactionManagerConfiguration {

    @Primary
    @Bean
    PlatformTransactionManager chainedTransactionManager(
          @Qualifier("centralTransactionManager") PlatformTransactionManager centralTransactionManager,
          @Qualifier("branchTransactionManager") PlatformTransactionManager branchTransactionManager) {
        return new ChainedTransactionManager(centralTransactionManager, branchTransactionManager);
    }
}
