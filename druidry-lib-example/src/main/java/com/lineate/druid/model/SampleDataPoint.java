package com.lineate.druid.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class SampleDataPoint {

    @JsonAlias("utc_timestamp")
    private String utcTimestamp;

    @JsonAlias("current_lat")
    private float latitude;

    @JsonAlias("current_lon")
    private float longitude;

    @JsonAlias("car_id")
    private String carId;

    @JsonAlias("app_id")
    private String appId;

    @JsonAlias("app_version")
    private String appVersion;


    public SampleDataPoint() {
    }

    public String getUtcTimestamp() {
        return utcTimestamp;
    }

    public void setUtcTimestamp(String utcTimestamp) {
        this.utcTimestamp = utcTimestamp;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
