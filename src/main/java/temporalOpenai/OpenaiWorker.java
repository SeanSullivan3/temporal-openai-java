package temporalOpenai;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class OpenaiWorker {

    public static void main(String[] args) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker("open-ai-task-queue");

        worker.registerWorkflowImplementationTypes(OpenaiWorkflowImpl.class);
        worker.registerActivitiesImplementations(new OpenaiActivitiesImpl());

        factory.start();

    }
}