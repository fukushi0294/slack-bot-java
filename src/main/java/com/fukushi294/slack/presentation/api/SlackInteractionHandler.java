package com.fukushi294.slack.presentation.api;

import com.fukushi294.slack.application.TimeRecordService;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.ViewSubmissionContext;
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.ViewState;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SlackInteractionHandler {
    private final App app;
    private final TimeRecordService timeRecordService;
    private final String homeViewString;
    private final String modalViewString;

    public SlackInteractionHandler(App app,
                                   TimeRecordService timeRecordService) throws IOException {
        this.app = app;
        this.timeRecordService = timeRecordService;
        this.homeViewString = readJsonViewAsString("view/home.json");
        this.modalViewString = readJsonViewAsString("view/modal.json");
    }

    private String readJsonViewAsString(String resourcePath) throws IOException {
        var resource = new ClassPathResource(resourcePath);
        return new String(resource.getInputStream().readAllBytes()).replaceAll("\\r\\n|\\n", "");
    }

    @PostConstruct
    public void setCommandAction() {
        app.viewSubmission("meeting-arrangement", this::viewSubmissionAction);
        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
            ctx.client().viewsPublish(r -> r
                    .userId(payload.getEvent().getUser())
                    .viewAsString(homeViewString)
            );
            return ctx.ack();
        });

        app.blockAction("add_note", (req, ctx) -> {
            ViewsOpenResponse viewsOpenRes = ctx.client().viewsOpen(r -> r
                    .triggerId(ctx.getTriggerId())
                    .viewAsString(modalViewString));
            if (viewsOpenRes.isOk()) return ctx.ack();
            else return Response.builder().statusCode(500).body(viewsOpenRes.getError()).build();
        });
    }

    private Response viewSubmissionAction(ViewSubmissionRequest req, ViewSubmissionContext ctx) {
        String privateMetadata = req.getPayload().getView().getPrivateMetadata();
        Map<String, Map<String, ViewState.Value>> stateValues = req.getPayload().getView().getState().getValues();
        String agenda = stateValues.get("agenda-block").get("agenda-action").getValue();
        Map<String, String> errors = new HashMap<>();
        if (agenda.length() <= 10) {
            errors.put("agenda-block", "Agenda needs to be longer than 10 characters.");
        }
        if (!errors.isEmpty()) {
            return ctx.ack(r -> r.responseAction("errors").errors(errors));
        } else {
            return ctx.ack();
        }
    }
}
