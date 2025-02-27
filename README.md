# real-time-streaming-gcp

This repository demonstrates a real-time streaming pipeline on GCP. Follow the steps below to build, deploy, and test the pipeline.

## Prerequisites

- **Java** and **Maven** installed.
- A valid **GCP account** with permissions to create infrastructure and resources.
- GCP SDK configured locally.
- Familiarity with **Google Cloud Dataflow** and **Pub/Sub**.

## Build the Project

Run the following Maven command to clean and build the project:

```bash
mvn clean install
GCP Infrastructure Setup
	1.	Create Infrastructure:
                Use the DDL file provided in the project to create the required tables in your GCP infrastructure.
             Create one more bucket as needed for your project.
	2.	Create Topics & Schema:
                Topics and the defined schema are available in the project files.
                Refer to the file: pubsub_schema.json for the schema details.
	3.	Authentication and Configuration:
        	Authenticate with your GCP account.
        	Update variables in your project with your specific project ID and other resource IDs.
        	For local runs, update the configuration directly in config.json.
        	For GCP runs, adjust the hardcoded values accordingly.

Running the Pipeline

Local Execution

To run the pipeline locally, execute the EventPipelineLocal.java class.

GCP Execution

You can run the pipeline on GCP by executing EventPipelineGcp.java or using Maven with the following command:
mvn clean compile exec:java \
-Dexec.mainClass=com.elson.etl.pipeline.EventPipelineGcp \
-Dexec.args=" \
--runner=DataflowRunner \
--project=real-time-streaming-452114 \
--region=europe-west1 \
--stagingLocation=gs://inventory-ingestion-etl/staging \
--tempLocation=gs://inventory-ingestion-etl/temp \
--numWorkers=2 \
--workerMachineType=n2-standard-2"
After deployment, you can view the Dataflow graph in the GCP console.
Start publishing messages on the created topics to see the related tables populated immediately.

Some test messages (both valid and invalid) can be found in the file: pubsub_test_messages.txt.