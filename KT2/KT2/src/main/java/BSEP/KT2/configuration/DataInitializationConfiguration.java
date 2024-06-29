package BSEP.KT2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataInitializationConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            String filePath = "src/main/resources/static/insert.sql";

            FileSystemResource fileSystemResource = new FileSystemResource(filePath);

            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(fileSystemResource);

            DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        };
    }
}