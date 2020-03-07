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
    private static String repromptStatement = "Did you want to search for: ";

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
        final boolean isIntentResponse = confirmIntentResponse(data);
        if (!isIntentResponse) {
            return null;
        }
        final JSONObject intentData = data.getJSONObject("request").getJSONObject("intent");
        final String intentName = intentData.getString("name");
        final String stockName = intentData.getJSONObject("slots").getJSONObject("stockName").getString("value");
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
        return "1";
    }

    private boolean confirmIntentResponse(final JSONObject data) {
        final String intentRequest = RequestTypes.INTENT.getType();
        final String requestType = data.getJSONObject("request").getString("type");
        if (!requestType.equals(intentRequest)) {
            LOGGER.info(String.format("%s not equal to %s", requestType, intentRequest));
            return false;
        }
        return true;
    }

    private HashMap<String, Object> getRepromptFromNames(final JSONObject tickersResponse) {
        final List<String> companies = new ArrayList<>();
        final JSONArray stockTickers = tickersResponse.getJSONObject("ResultSet").getJSONArray("Result");
        for (final Object stock : stockTickers) {
            final JSONObject stockData = (JSONObject) stock;
            companies.add(stockData.getString("name"));
        }
        String statement = repromptStatement.concat(String.join(" or ", companies));

        HashMap<String, String> outputSpeech = new HashMap<>();
        outputSpeech.put("type", "PlainText");
        outputSpeech.put("text", statement);
        HashMap<String, Object> reprompt = new HashMap<>();
        reprompt.put("version", "1.0");
        reprompt.put("response", outputSpeech);
        return reprompt;
    }

    public static void main(final String args[]) {}
}
