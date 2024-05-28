package temporalOpenai;

//Object to be passed into OpenaiActivities.getResponse()
//Used to store chat history as well
public class Question {

    private String question;
    private String response;

    public Question() {}
    public Question(String q) {
        question = q;
    }
    public Question(String q, String a) {
        question = q;
        response = a;
    }

    public String getQuestion() {return question;}
    public void setQuestion(String q) { question = q;}
    public String getResponse() { return response; }
    public void setResponse(String a) { response = a; }

}