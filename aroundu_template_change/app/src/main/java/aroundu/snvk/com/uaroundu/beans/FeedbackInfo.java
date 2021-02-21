package aroundu.snvk.com.uaroundu.beans;

public class FeedbackInfo {
    private String id;

    public String getFeedbackBus() {
        return feedbackBus;
    }

    public void setFeedbackBus(String feedbackBus) {
        this.feedbackBus = feedbackBus;
    }

    private String feedbackBus;
    private String feedback;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
