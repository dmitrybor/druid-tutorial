package com.lineate.druid;

import com.lineate.druid.model.DruidResponse;
import com.lineate.druid.model.SampleDataPoint;
import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;
import in.zapr.druid.druidry.client.exception.DruidryException;
import in.zapr.druid.druidry.dataSource.TableDataSource;
import in.zapr.druid.druidry.filter.AndFilter;
import in.zapr.druid.druidry.filter.BoundFilter;
import in.zapr.druid.druidry.query.config.Interval;
import in.zapr.druid.druidry.query.config.SortingOrder;
import in.zapr.druid.druidry.query.scan.DruidScanQuery;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DruidryDemoApp {
    private final static String druidHost = "localhost";
    private final static String druidEndPoint = "druid/v2/";
    private final static String druidDataSource = "events";

    private final static String RESULT_TEMPLATE = "Time: %s | Latitude: %f | Longitude: %f | CarId: %S";

    public static void main(String[] args) {

        DateTime fromTime = new DateTime(2019, 12, 31, 0, 0, 0, DateTimeZone.UTC);
        DateTime toTime = new DateTime(2020, 12, 31, 0, 0, 0, DateTimeZone.UTC);
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
            DateTime fromTime, DateTime toTime,
            float fromLatitude, float toLatitude,
            float fromLongitude, float toLongitude,
            long resultsLimit
    ) {

        try (DruidClient client = createDruidClient()) {

            BoundFilter latitudeFilter = BoundFilter.builder()
                    .dimension("current_lat")
                    .lower(String.valueOf(fromLatitude))
                    .lowerStrict(false)
                    .upper(String.valueOf(toLatitude))
                    .upperStrict(false)
                    .ordering(SortingOrder.NUMERIC)
                    .build();

            BoundFilter longitudeFilter = BoundFilter.builder()
                    .dimension("current_lon")
                    .lower(String.valueOf(fromLongitude))
                    .lowerStrict(false)
                    .upper(String.valueOf(toLongitude))
                    .upperStrict(false)
                    .ordering(SortingOrder.NUMERIC)
                    .build();

            AndFilter filter = new AndFilter(Arrays.asList(latitudeFilter, longitudeFilter));

            Interval interval = new Interval(fromTime, toTime);

            DruidScanQuery query = DruidScanQuery.builder()
                    .dataSource(new TableDataSource(druidDataSource))
                    .columns(Arrays.asList("utc_timestamp", "current_lat", "current_lon",
                            "car_id", "app_id", "app_version"))
                    .filter(filter)
                    .intervals(Collections.singletonList(interval))
                    .limit(resultsLimit)
                    .build();

            client.connect();

            return client.query(query, DruidResponse.class)
                    .stream()
                    .flatMap(response -> response.getEvents().stream())
                    .collect(Collectors.toList());

        } catch (DruidryException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static DruidClient createDruidClient() {
        DruidConfiguration config = DruidConfiguration
                .builder()
                .host(druidHost)
                .endpoint(druidEndPoint)
                .build();
        return new DruidJerseyClient(config);
    }
}
