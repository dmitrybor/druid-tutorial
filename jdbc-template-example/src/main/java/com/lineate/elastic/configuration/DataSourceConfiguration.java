package com.lineate.elastic.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean
    public DataSource getDruidDataSource() {
        DataSourceBuilder datasourceBuilder = DataSourceBuilder.create();
        datasourceBuilder.driverClassName("org.apache.calcite.avatica.remote.Driver");
        datasourceBuilder.url("jdbc:avatica:remote:url=http://localhost:8082/druid/v2/sql/avatica/");
        return datasourceBuilder.build();
    }
}
