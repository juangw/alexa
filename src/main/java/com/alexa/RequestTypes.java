package com.alexa;


public enum RequestTypes {
    LAUNCH("LaunchRequest"),
    END("SessionEndedRequest"),
    INTENT("IntentRequest");

    // declaring private variable for getting values 
    private String type;

    RequestTypes(final String requestType) {
        this.type = requestType;
    }
 
    // getter method
    final public String getType() {
        return this.type;
    }
}

