package com.lineate.druid;

import com.lineate.druid.model.SampleDataPoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class JdbcDemoApp {
    private final static String druidUrl = "jdbc:avatica:remote:url=http://localhost:8082/druid/v2/sql/avatica/";
    private final static Properties druidConnectionProps = new Properties();
    private final static String SQL_QUERY_TEMPLATE =
            "SELECT * FROM events WHERE " +
                    "__time BETWEEN TIME_PARSE('%s') AND TIME_PARSE('%s') " +
                    "AND current_lat BETWEEN %f AND %f " +
                    "AND current_lon BETWEEN %f AND %f " +
                    "LIMIT %d";

    private final static String RESULT_TEMPLATE = "Time: %s | Latitude: %f | Longitude: %f | CarId: %S";

    public static void main(String[] args) {

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
                System.out.println(String.format(RESULT_TEMPLATE, r.getUtcTimestamp(), r.getLatitude(),
                        r.getLongitude(), r.getCarId())));
    }

    private static List<SampleDataPoint> getDataPointsByCoordinatesRange(
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

        try (Connection connection = DriverManager.getConnection(druidUrl, druidConnectionProps)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    return parseDataPoints(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<SampleDataPoint> parseDataPoints(ResultSet resultSet) throws SQLException {
        List<SampleDataPoint> dataPoints = new ArrayList<>();
        while (resultSet.next()) {
            SampleDataPoint dataPoint = new SampleDataPoint();
            dataPoint.setAppId(resultSet.getString("app_id"));
            dataPoint.setAppVersion(resultSet.getString("app_version"));
            dataPoint.setCarId(resultSet.getString("car_id"));
            dataPoint.setLatitude(resultSet.getFloat("current_lat"));
            dataPoint.setLongitude(resultSet.getFloat("current_lon"));
            dataPoint.setUtcTimestamp(resultSet.getString("utc_timestamp"));
            dataPoints.add(dataPoint);
        }
        return dataPoints;
    }
}
