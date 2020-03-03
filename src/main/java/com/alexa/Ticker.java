package com.alexa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Ticker {
    private static String nameToTickerUrl = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=ford&name=%s&region=1&lang=en&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
    
    public String getTickerFromName(final String name) throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(nameToTickerUrl, name)))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static void main(final String args[]) {}
}


