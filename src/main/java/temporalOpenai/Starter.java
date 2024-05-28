package temporalOpenai;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@SpringBootApplication
public class Starter {

    //Create a list of question objects to hold our chat history
    List<Question> chat_history = new ArrayList<Question>();

    public static void main(String[] args) throws Exception {

        //Boots the web server on localhost:8080
        SpringApplication.run(Starter.class, args);
    }

    //Called when received http GET request to localhost:8080/
    @GetMapping("/")
    public String index() {
        return "index";
    }

    //Called when received http POST request to localhost:8080/answer
    @PostMapping("/answer")
    public String answer(Model model, String question) {

        //Save "question" from user input in index.html
        String user_question = question;

        //Initialize our Workflow client and Workflow options
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(user_question.replace(' ','-').toLowerCase())
                .setTaskQueue("open-ai-task-queue")
                .build();

        //Start the workflow by calling OpenaiWorkflow.chat()
        OpenaiWorkflow workflow = client.newWorkflowStub(OpenaiWorkflow.class, options);
        String response = workflow.chat(user_question);

        //Add this workflow's question and response to chat history
        Question chat = new Question(user_question, response);
        chat_history.add(chat);

        //Add updated chat history to index.html and display on localhost:8080/answer
        model.addAttribute("chat_history",chat_history);
        return "index";
    }
}