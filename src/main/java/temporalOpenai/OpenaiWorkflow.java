package temporalOpenai;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OpenaiWorkflow {

    @WorkflowMethod
    public String chat(String question);

}
