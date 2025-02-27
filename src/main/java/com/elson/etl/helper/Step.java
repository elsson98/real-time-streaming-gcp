package com.elson.etl.helper;

public class Step {

    public static final String READ_FROM_PUBSUB = "Read From PubSub";
    public static final String PARSE_AND_ROUTE_EVENTS = "Parse And Route Events";
    public static final String WRITE_ORDERS_TO_BQ = "Write Orders To BQ";
    public static final String WRITE_INVENTORY_TO_BQ = "Write Inventory To BQ";
    public static final String WRITE_USER_ACTIVITY_TO_BQ = "Write User Activity To BQ";
    public static final String WRITE_ERRORS_TO_BQ = "Write Errors To BQ";
    public static final String WRITE_TO_GCS = "Write To GCS";
}