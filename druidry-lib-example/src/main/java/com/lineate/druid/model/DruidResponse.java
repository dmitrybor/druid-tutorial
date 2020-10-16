package com.lineate.druid.model;

import java.util.List;

public class DruidResponse {
    private String segmentId;
    private List<String> columns;
    private List<SampleDataPoint> events;

    public DruidResponse() {
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<SampleDataPoint> getEvents() {
        return events;
    }

    public void setEvents(List<SampleDataPoint> events) {
        this.events = events;
    }
}
