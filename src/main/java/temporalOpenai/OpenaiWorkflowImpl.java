package temporalOpenai;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowFailedException;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.failure.TemporalFailure;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class OpenaiWorkflowImpl implements OpenaiWorkflow {

    RetryOptions retryOptions = RetryOptions.newBuilder()
            .setMaximumAttempts(5)
            .build();

    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(15)).setRetryOptions(retryOptions)
            .build();

    private final OpenaiActivities activities = Workflow.newActivityStub(OpenaiActivities.class, options);

    @Override
    public String chat(String question) {

        Question q = new Question(question);
        String response;

        //Try activity getResponse() and catch TemporalFailure in case of activity error
        try {
            response = activities.getResponse(q);
        }
        catch (TemporalFailure e) {
            response = "ERROR: Check localhost:8233 for details...";
        }
        return response;
    }
}