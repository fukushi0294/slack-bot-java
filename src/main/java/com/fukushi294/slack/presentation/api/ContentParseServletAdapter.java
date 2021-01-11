package com.fukushi294.slack.presentation.api;

import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.request.Request;
import com.slack.api.bolt.request.RequestHeaders;
import com.slack.api.bolt.servlet.SlackAppServletAdapter;
import com.slack.api.bolt.util.QueryStringParser;
import com.slack.api.bolt.util.SlackRequestParser;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.slack.api.bolt.servlet.ServletAdapterOps.toHeaderMap;

@Slf4j
public class ContentParseServletAdapter extends SlackAppServletAdapter {
    private final SlackRequestParser requestParser;

    public ContentParseServletAdapter(AppConfig appConfig) {
        super(appConfig);
        this.requestParser = new SlackRequestParser(appConfig);
    }

    @Override
    protected String doReadRequestBodyAsString(HttpServletRequest req) throws IOException {
        String requestBody;
        String payload = req.getParameter("payload");
        if (!StringUtils.isEmpty(payload)) {
            requestBody = "payload="
                    + URLEncoder.encode(payload, StandardCharsets.UTF_8.toString());
        } else {
            requestBody = super.doReadRequestBodyAsString(req);
        }
        return requestBody;
    }

    @Override
    public Request<?> buildSlackRequest(HttpServletRequest req) throws IOException {
        String requestBody = doReadRequestBodyAsString(req);
        RequestHeaders headers = new RequestHeaders(toHeaderMap(req));
        SlackRequestParser.HttpRequest rawRequest = SlackRequestParser.HttpRequest.builder()
                .requestUri(req.getRequestURI())
                .queryString(QueryStringParser.toMap(req.getQueryString()))
                .headers(headers)
                .requestBody(requestBody)
                .remoteAddress(req.getRemoteAddr())
                .build();
        return requestParser.parse(rawRequest);
    }
}
