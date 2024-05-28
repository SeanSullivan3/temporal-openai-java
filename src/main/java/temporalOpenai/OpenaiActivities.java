package temporalOpenai;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface OpenaiActivities {

    @ActivityMethod
    String getResponse(Question q);
}