package com.alexa;

import java.io.IOException;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

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
        boolean isIntentResponse = confirmIntentResponse(data);
        if (!isIntentResponse) {
            return null;
        }
        final JSONObject intentData = data.getJSONObject("request").getJSONObject("intent");
        final String intentName = intentData.getString("name");
        final String stockName = intentData.getJSONObject("slots").getJSONObject("stockName").getString("value");
        if (intentName.equals("getStockPrice")) {
            Ticker ticker = new Ticker();
            try {
                JSONObject stockTicker = ticker.getTickerFromName(stockName);
                System.out.println(stockTicker);
            } catch (final IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
            System.out.println(intentName);
            System.out.println(stockName);
        }
        return "1";
    }

    public boolean confirmIntentResponse(JSONObject data) {
        final String intentRequest = RequestTypes.INTENT.getType();
        final String requestType = data.getJSONObject("request").getString("type");
        if (!requestType.equals(intentRequest)) {
            LOGGER.info(String.format("%s not equal to %s", requestType, intentRequest));
            return false;
        }
        return true;
    }

    public static void main(final String args[]) {}
}
