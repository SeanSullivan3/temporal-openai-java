package temporalOpenai;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
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
        return activities.getResponse(q);
    }
}