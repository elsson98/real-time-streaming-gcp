package com.elson.etl.pipeline;

import com.elson.etl.helper.ConfigLoader;
import com.elson.etl.helper.Step;
import com.elson.etl.models.ErrorRecord;
import com.elson.etl.models.Inventory;
import com.elson.etl.models.Order;
import com.elson.etl.models.UserActivity;
import com.elson.etl.schema.ErrorTableSchema;
import com.elson.etl.schema.InventoryTableSchema;
import com.elson.etl.schema.OrderTableSchema;
import com.elson.etl.schema.UserActivityTableSchema;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

import java.io.IOException;

public class EventPipelineLocal {

    static final TupleTag<Order> ORDER_TAG = new TupleTag<Order>() {
    };
    static final TupleTag<Inventory> INVENTORY_TAG = new TupleTag<Inventory>() {
    };
    static final TupleTag<UserActivity> USER_ACTIVITY_TAG = new TupleTag<UserActivity>() {
    };
    static final TupleTag<ErrorRecord> ERROR_TAG = new TupleTag<ErrorRecord>() {
    };

    public static void main(String[] args) throws IOException {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();

        options.setRunner(DirectRunner.class);
        Pipeline pipeline = Pipeline.create(options);


        ConfigLoader.loadConfig("config.json");

        PCollection<String> messages = pipeline.apply(
                Step.READ_FROM_PUBSUB,
                PubsubIO.readStrings()
                        .fromSubscription(ConfigLoader.getConfig("subscriptionPath"))
        );

        PCollectionTuple parsedEvents = messages.apply(
                Step.PARSE_AND_ROUTE_EVENTS,
                ParDo.of(new EventParser())
                        .withOutputTags(ORDER_TAG, TupleTagList.of(INVENTORY_TAG)
                                .and(USER_ACTIVITY_TAG)
                                .and(ERROR_TAG))
        );

        writeToBigQuery(parsedEvents);
        messages.apply(Step.WRITE_TO_GCS, ParDo.of(new GcsWriter()));
        pipeline.run().waitUntilFinish();
    }

    private static void writeToBigQuery(PCollectionTuple parsedEvents) {
        parsedEvents.get(ORDER_TAG)
                .apply(Step.WRITE_ORDERS_TO_BQ, BigQueryIO.<Order>write()
                        .to(ConfigLoader.getConfig("orderTable"))
                        .withFormatFunction(Order::toTableRow)
                        .withSchema(OrderTableSchema.getSchema())
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

        parsedEvents.get(INVENTORY_TAG)
                .apply(Step.WRITE_INVENTORY_TO_BQ, BigQueryIO.<Inventory>write()
                        .to(ConfigLoader.getConfig("inventoryTable"))
                        .withFormatFunction(Inventory::toTableRow)
                        .withSchema(InventoryTableSchema.getSchema())
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

        parsedEvents.get(USER_ACTIVITY_TAG)
                .apply(Step.WRITE_USER_ACTIVITY_TO_BQ, BigQueryIO.<UserActivity>write()
                        .to(ConfigLoader.getConfig("userActivityTable"))
                        .withFormatFunction(UserActivity::toTableRow)
                        .withSchema(UserActivityTableSchema.getSchema())
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

        parsedEvents.get(ERROR_TAG)
                .apply(Step.WRITE_ERRORS_TO_BQ, BigQueryIO.<ErrorRecord>write()
                        .to(ConfigLoader.getConfig("errorTable"))
                        .withFormatFunction(ErrorRecord::toTableRow)
                        .withSchema(ErrorTableSchema.getSchema())
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));
    }
}