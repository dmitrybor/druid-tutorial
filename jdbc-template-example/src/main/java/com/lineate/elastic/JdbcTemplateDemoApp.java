package com.lineate.elastic;

import com.lineate.elastic.model.SampleDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class JdbcTemplateDemoApp implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(JdbcTemplateDemoApp.class);

    private final static String RESULT_TEMPLATE = "Time: %s | Latitude: %f | Longitude: %f | CarId: %S";

    private final static String SQL_QUERY_TEMPLATE =
            "SELECT * FROM events WHERE " +
                    "__time BETWEEN TIME_PARSE('%s') AND TIME_PARSE('%s') " +
                    "AND current_lat BETWEEN %f AND %f " +
                    "AND current_lon BETWEEN %f AND %f " +
                    "LIMIT %d";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        LOGGER.info("Starting application");
        SpringApplication.run(JdbcTemplateDemoApp.class, args);
        LOGGER.info("Application done");
    }

    @Override
    public void run(String... args) {

        ZonedDateTime fromTime = ZonedDateTime.of(2020, 9, 16, 8, 54, 29, 0, ZoneId.of("UTC"));
        ZonedDateTime toTime = ZonedDateTime.of(2020, 9, 16, 8, 54, 34, 0, ZoneId.of("UTC"));
        float fromLatitude = -9.0F;
        float toLatitude = -7.0F;
        float fromLongitude = -35.0F;
        float toLongitude = -33.0F;
        long resultLimit = 10;

        List<SampleDataPoint> results = getDataPointsByCoordinatesRange(fromTime, toTime,
                fromLatitude, toLatitude, fromLongitude, toLongitude, resultLimit);

        results.forEach(r ->
                LOGGER.info(String.format(RESULT_TEMPLATE, r.getUtcTimestamp(), r.getLatitude(),
                        r.getLongitude(), r.getCarId())));
    }

    private List<SampleDataPoint> getDataPointsByCoordinatesRange(
            ZonedDateTime fromTime, ZonedDateTime toTime,
            float fromLatitude, float toLatitude,
            float fromLongitude, float toLongitude,
            long resultsLimit
    ) {

        String sql = String.format(SQL_QUERY_TEMPLATE,
                fromTime.format(DateTimeFormatter.ISO_INSTANT),
                toTime.format(DateTimeFormatter.ISO_INSTANT),
                fromLatitude, toLatitude,
                fromLongitude, toLongitude,
                resultsLimit);

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> {
                    SampleDataPoint dataPoint = new SampleDataPoint();
                    dataPoint.setAppId(resultSet.getString("app_id"));
                    dataPoint.setAppVersion(resultSet.getString("app_version"));
                    dataPoint.setCarId(resultSet.getString("car_id"));
                    dataPoint.setLatitude(resultSet.getFloat("current_lat"));
                    dataPoint.setLongitude(resultSet.getFloat("current_lon"));
                    dataPoint.setUtcTimestamp(resultSet.getString("utc_timestamp"));
                    return dataPoint;
                });
    }
}
