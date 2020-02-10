package com.alexa;


public enum RequestTypes {
    LAUNCH("LaunchRequest"),
    END("SessionEndedRequest"),
    INTENT("IntentRequest");

    // declaring private variable for getting values 
    private String type;

    RequestTypes(String requestType) {
        this.type = requestType;
    }
 
    // getter method 
    public String getType() {
        return this.type;
    }
}

