package com.alexa;

import org.json.*;
 
import java.util.logging.Logger;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ticker {
    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private static String nameToTickerUrl = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=%s&region=1&lang=en&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
    
    public static JSONObject getTickerFromName(final String name) throws IOException, InterruptedException {
        final URL url = new URL(String.format(nameToTickerUrl, name));
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        final int status = con.getResponseCode();
        if (status == 200) {
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            final StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // return result in json obj
            final String res = response.toString();
            final String data = res.substring(res.indexOf("(") + 1, res.indexOf(")") + 1);
            final JSONObject obj = new JSONObject(data);
            return obj;
        }
        LOGGER.info("Failed to call yahoo finance to find stock ticker");
        return null;
    }

    public static void main(final String args[]) {
        final String stockName = args[0];
        try {
            System.out.println(getTickerFromName(stockName));
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

