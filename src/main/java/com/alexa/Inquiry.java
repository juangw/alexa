package com.alexa;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class Inquiry implements RequestHandler<String, String>{
    public String handleRequest(String data, Context context) {
        return String.format("This is the data %", data);
    }

    public static void main(String args[]) {
        System.out.println(args);
    }
}
