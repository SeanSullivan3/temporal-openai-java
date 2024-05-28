package temporalOpenai;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import io.temporal.activity.Activity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenaiActivitiesImpl implements OpenaiActivities {

    //Define user's openai api key from environment
    private final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private final String URL = "https://api.openai.com/v1/chat/completions";


    @Override
    public String getResponse(Question q) {

        //Create JSON formatted string for the http request body (see static helper function below)
        String requestBody = toJSON(q.getQuestion());

        //Build http request with the url, headers, and json data
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer " + OPENAI_API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //Try http POST request with question to api.openai.com
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException | InterruptedException e) {
            throw Activity.wrap(e);
        }

        //Try to store received json data in JsonNode object
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.body());
        }
        catch (JsonProcessingException e) {
            throw Activity.wrap(e);
        }

        //If we received error from api.openai.com return the error message
        if (!jsonNode.get("error").isNull()) {
            return "ERROR WITH OPENAI SERVER:\n" + jsonNode.get("error").get("message").asText();
        }

        //Return the content of the first choice message from openai
        return jsonNode.get("choices").get(0).get("message").get("content").asText();
    }

    public static String toJSON(String question) {

        String json =   "{ \"model\" : \"gpt-3.5-turbo\", " +
                        "\"messages\" : [" +
                        "{ \"role\" : \"system\", \"content\" : \"You are a chatbot.\" }, " +
                        "{ \"role\" : \"user\", \"content\" : \"" + question + "\" }] }";
        return json;
    }
}