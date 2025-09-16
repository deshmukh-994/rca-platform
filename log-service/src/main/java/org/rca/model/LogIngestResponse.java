package org.rca.model;

public class LogIngestResponse {
    private boolean accepted;
    private int count;
    private String topic;

    public LogIngestResponse(){}
    public LogIngestResponse(boolean accepted, int count, String topic){
        this.accepted = accepted; this.count = count; this.topic = topic;
    }
    public boolean isAccepted(){ return accepted; }
    public void setAccepted(boolean v){ accepted = v; }
    public int getCount(){ return count; }
    public void setCount(int v){ count = v; }
    public String getTopic(){ return topic; }
    public void setTopic(String v){ topic = v; }
}