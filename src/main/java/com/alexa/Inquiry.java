package com.alexa;

import static com.alexa.Ticker.getTickersFromName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Inquiry implements RequestHandler<Object, String>{
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    public String handleRequest(final Object request, final Context context) {
        System.out.println(request.toString()); 

        final Gson gson = new Gson();
        final String jsonString = gson.toJson(request);
        JSONObject data = new JSONObject();
        try {
            data = new JSONObject(jsonString);
        } catch (final JSONException e) {
            e.printStackTrace();
            return null;
        }

        final String requestType = data.getJSONObject("request").getString("type");
        switch(requestType) {
            case "IntentRequest":
                final JSONObject intentData = data.getJSONObject("request").getJSONObject("intent");
                final String intentName = intentData.getString("name");
                final String stockName = intentData.getJSONObject("slots").getJSONObject("stockName").getString("value");
                return handleIntentRequest(intentName, stockName);
            case "LaunchRequest":
                return handleLaunchRequest();
            default:
                LOGGER.info(String.format("%s not equal handled", requestType));
                return null;
        }
    }

    private String handleLaunchRequest() {
        final HashMap<String, Object> response = buildResponse("response", "Starting Alexa skill");
        final Gson gsonBuilder = new GsonBuilder().create();
        return gsonBuilder.toJson(response);
    }

    private String handleIntentRequest(final String intentName, final String stockName) {
        if (intentName.equals("getStockPrice")) {
            try {
                final JSONObject stockTickersData = getTickersFromName(stockName);
                if (stockTickersData == null) {
                    return null;
                }
                final HashMap<String, Object> repromptResponse = getRepromptFromNames(stockTickersData);
                final Gson gsonBuilder = new GsonBuilder().create();
                System.out.println(gsonBuilder.toJson(repromptResponse));
                return gsonBuilder.toJson(repromptResponse);
            } catch (final IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
        LOGGER.info("Unhandled intent");
        return null;
    }

    private HashMap<String, Object> getRepromptFromNames(final JSONObject tickersResponse) {
        final String repromptStatement = "Did you want to search for: ";
        final List<String> companies = new ArrayList<>();
        final JSONArray stockTickers = tickersResponse.getJSONObject("ResultSet").getJSONArray("Result");
        for (final Object stock : stockTickers) {
            final JSONObject stockData = (JSONObject) stock;
            companies.add(stockData.getString("name"));
        }
        final String statement = repromptStatement.concat(String.join(" or ", companies));

        final HashMap<String, Object> response = buildResponse("reprompt", statement);
        return response;
    }

    private HashMap<String, Object> buildResponse(final String type, final String text) {
        final HashMap<String, String> responseText = new HashMap<>();
        responseText.put("type", "PlainText");
        responseText.put("text", text);
        final HashMap<String, Object> outputSpeech = new HashMap<>();
        outputSpeech.put("outputSpeech", responseText);
        final HashMap<String, Object> response = new HashMap<>();
        response.put("version", "1.0");

        if (type.equals("reprompt")) {
            final HashMap<String, Object> reprompt = new HashMap<>();
            reprompt.put("reprompt", outputSpeech);
            response.put("response", reprompt);
            response.put("shouldEndSession", false);
        } else {
            response.put("response", outputSpeech);
        }
        return response;
    }

    public static void main(final String args[]) {}
}
