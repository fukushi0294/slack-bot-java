package com.fukushi294.slack.presentation.api;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServletAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(value = {"/slack/actions", "/slack/events"})
public class SlackAppController extends HttpServlet {
    private final App app;
    private final SlackAppServletAdapter adapter;

    public SlackAppController(App app) {
        this.app = app;
        this.adapter = new ContentParseServletAdapter(app.config());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var slackReq = adapter.buildSlackRequest(req);
        if (slackReq != null) {
            try {
                var slackResp = app.run(slackReq);
                adapter.writeResponse(resp, slackResp);
            } catch (Exception e) {
                log.error("Failed to handle a request - {}", e.getMessage(), e);
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"Something is wrong\"}");
            }
        }
    }
}
