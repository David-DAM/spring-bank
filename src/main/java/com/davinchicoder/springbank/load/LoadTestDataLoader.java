package com.davinchicoder.springbank.load;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Profile("load")
@Component
@RequiredArgsConstructor
public class LoadTestDataLoader implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("load/data.sql"));
        populator.execute(dataSource);
    }
}
