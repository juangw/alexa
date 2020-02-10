package com.alexa;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.logging.Logger;


public class Inquiry implements RequestHandler<Object, String>{
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    public String handleRequest(final Object request, final Context context) {
        System.out.println(request.toString()); 

        Gson gson = new Gson();
        String jsonString = gson.toJson(request);
        try {
            JSONObject data = new JSONObject(jsonString);
            final String intentRequest = RequestTypes.INTENT.getType();
            final String requestType = data.getJSONObject("request").getString("type");
            if (!requestType.equals(intentRequest)) {
                LOGGER.info(String.format("Could not find request type in request: %s", data.toString()));
                return null;
            }
            System.out.println(data.getJSONObject("session").getJSONObject("application").getString("applicationId"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return "1";
    }

    public static void main(final String args[]) {
        System.out.println(args);
    }
}
