package com.davinchicoder.springbank.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class DatabaseHooks {

    private final DataSource dataSource;

    @Before("@transfer-success")
    public void setupTransferSuccess() {
        executeSql("/it/transfer/success/data.sql");
    }

    @Before("@transfer-duplicated")
    public void setupTransferDuplicated() {
        executeSql("/it/transfer/duplicated/data.sql");
    }

    @Before("@transfer-account-not-found")
    public void setupTransferAccountNotFound() {
        executeSql("/it/transfer/account-not-found/data.sql");
    }

    @After
    public void cleanup() {
        executeSql("/it/clean.sql");
    }

    private void executeSql(String path) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(path));
        populator.execute(dataSource);
    }
}
