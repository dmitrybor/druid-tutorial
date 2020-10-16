package com.lineate.druid.model;

public class SampleDataPoint {

    private String utcTimestamp;
    private float latitude;
    private float longitude;
    private String carId;
    private String appId;
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
