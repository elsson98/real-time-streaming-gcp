CREATE TABLE `real-time-streaming-452114.events.order`
(
    event_type STRING NOT NULL OPTIONS(description="Always 'order'"),
    order_id STRING NOT NULL,
    customer_id STRING NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    status STRING NOT NULL OPTIONS(description="ENUM: pending, processing, shipped, delivered"),
    items ARRAY<
    STRUCT<
      product_id STRING,
    product_name STRING,
    quantity INT64,
    price FLOAT64
        >
  >,
    shipping_address STRUCT<
    street STRING,
    city STRING,
    country STRING
        >,
    total_amount FLOAT64 NOT NULL,
    ingestion_time TIMESTAMP NOT NULL
        OPTIONS(description="Time when the event was ingested into BigQuery")
)
    PARTITION BY DATE(timestamp)
CLUSTER BY customer_id, status;

CREATE TABLE `real-time-streaming-452114.events.inventory`
(
    event_type STRING NOT NULL OPTIONS(description="Always 'inventory'"),
    inventory_id STRING NOT NULL,
    product_id STRING NOT NULL,
    warehouse_id STRING NOT NULL,
    quantity_change INT64 NOT NULL OPTIONS(description="Range: -100 to 100"),
    reason STRING NOT NULL OPTIONS(description="ENUM: restock, sale, return, damage"),
    timestamp TIMESTAMP NOT NULL,
    ingestion_time TIMESTAMP NOT NULL
        OPTIONS(description="Time when the event was ingested into BigQuery")
)
    PARTITION BY DATE(timestamp)
CLUSTER BY product_id, warehouse_id;

CREATE TABLE `real-time-streaming-452114.events.user_activity`
(
    event_type STRING NOT NULL OPTIONS(description="Always 'user_activity'"),
    user_id STRING NOT NULL,
    activity_type STRING NOT NULL OPTIONS(description="ENUM: login, logout, view_product, add_to_cart, remove_from_cart"),
    ip_address STRING,
    user_agent STRING,
    timestamp TIMESTAMP NOT NULL,
    metadata STRUCT<
    session_id STRING,
    platform STRING
        >,
    ingestion_time TIMESTAMP NOT NULL
        OPTIONS(description="Time when the event was ingested into BigQuery")
)
    PARTITION BY DATE(timestamp)
CLUSTER BY user_id, activity_type;

CREATE TABLE `real-time-streaming-452114.events.error_records` (
    error_message STRING,
    original_json STRING,
    timestamp TIMESTAMP,
    ingestion_time TIMESTAMP
)    PARTITION BY DATE(timestamp)
CLUSTER BY error_message, original_json;

-- TRUNCATE TABLE `real-time-streaming-452114.events.error_logs`;
-- TRUNCATE TABLE `real-time-streaming-452114.events.inventory`;
-- TRUNCATE TABLE `real-time-streaming-452114.events.order`;
-- TRUNCATE TABLE `real-time-streaming-452114.events.user_activity`;