package com.restlet.frontend.web;

import java.util.Date;
import java.util.logging.Level;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.routing.Filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestLogFilter extends Filter {
    private final RequestLogFilter.RequestLogFilterType requestLogFilterType;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public RequestLogFilter() {
        this(RequestLogFilter.RequestLogFilterType.INBOUND);
    }

    public RequestLogFilter(RequestLogFilter.RequestLogFilterType requestLogFilterType) {
        this.requestLogFilterType = requestLogFilterType;
    }

    protected int doHandle(Request request, Response response) {
        Date date = new Date();
        if (!request.isLoggable()) {
            return super.doHandle(request, response);
        } else {
            this.logBefore(request, response);
            boolean var11 = false;

            int var4;
            try {
                var11 = true;
                var4 = super.doHandle(request, response);
                var11 = false;
            } finally {
                if (var11) {
                    long durationInMs1 = new Date().getTime() - date.getTime();
                    this.logAfter(request, response, durationInMs1);
                }
            }

            long durationInMs = new Date().getTime() - date.getTime();
            this.logAfter(request, response, durationInMs);
            return var4;
        }
    }

    private void logBefore(Request request, Response response) {
        Engine.getLogger(getClass()).log(Level.INFO, this.getResponseLogMessageStart(request, response));
        Engine.getLogger(getClass()).log(Level.INFO, this.getHeadersLog(request));
    }

    private void logAfter(Request request, Response response, long durationInMs) {
        Engine.getLogger(getClass()).log(Level.INFO, this.getResponseLogMessageEnd(request, response, durationInMs));
    }

    public String getHeadersLog(Request request) {
        Object headers = request.getHeaders();

        try {
            return "headers:<@" + MAPPER.writeValueAsString(headers) + "@>" + headers;
        } catch (JsonProcessingException var4) {
            Engine.getLogger(getClass()).log(Level.SEVERE, "Jackson serialization error", var4);
            return "headers:<@[\"error\"]@>";
        }
    }

    public String getResponseLogMessageStart(Request request, Response response) {
        return this.getResponseLogMessage(request, response, RequestLogFilter.RequestLogFilterState.START, -1L);
    }

    public String getResponseLogMessageEnd(Request request, Response response, long durationInMs) {
        return this.getResponseLogMessage(request, response, RequestLogFilter.RequestLogFilterState.END, durationInMs);
    }

    /**
     * Format a log entry using the default IIS log format.
     */
    private String getResponseLogMessage(Request request, Response response, RequestLogFilterState when,
            long durationInMs) {
        StringBuilder sb = new StringBuilder();

        // Append the client IP address
        String clientAddress = request.getClientInfo().getUpstreamAddress();
        sb.append((clientAddress == null) ? "-" : clientAddress);

        // Append the user name (via IDENT protocol)
        sb.append(' ');
        if ((request.getChallengeResponse() != null)
                && (request.getChallengeResponse().getIdentifier() != null)) {
            sb.append(request.getChallengeResponse().getIdentifier());
        } else {
            sb.append('-');
        }

        // Append the method name
        sb.append(' ');
        String methodName = (request.getMethod() == null) ? "-" : request
                .getMethod().getName();
        sb.append((methodName == null) ? "-" : methodName);

        // Append the resource path
        sb.append(' ');
        String resourcePath = null;
        switch (requestLogFilterType) {
        case OUTBOUND:
            // For outgoing request, log the absolute URL without the query and fragment.
            resourcePath = (request.getResourceRef() == null) ? "-"
                    : request.getResourceRef().toString(false, false);
            break;
        default:
            resourcePath = (request.getResourceRef() == null) ? "-"
                    : request.getResourceRef().getPath();
            break;
        }
        sb.append((resourcePath == null) ? "-" : resourcePath);

        // Append the resource query
        sb.append(' ');
        String resourceQuery = (request.getResourceRef() == null) ? "-"
                : request.getResourceRef().getQuery();
        sb.append((resourceQuery == null) ? "-" : resourceQuery);

        switch (when) {
        case START:
            // Append the status code
            sb.append(" ?");
            // Append the response entity size
            sb.append(" ");
            sb.append(getResponseEntitySize(request, response));
            // Append the request entity size
            sb.append(" ?");
            // Append the duration
            sb.append(" ?");
            break;
        case END:
            // Append the status code
            sb.append(' ');
            sb.append(Integer.toString(response.getStatus().getCode()));
            // Append the response entity size
            sb.append(' ');
            sb.append(getResponseEntitySize(request, response));
            // Append the request entity size
            sb.append(' ');
            sb.append(getRequestEntitySize(request));
            // Append the duration
            sb.append(' ');
            sb.append(durationInMs);
            break;
        default:
            throw new RuntimeException("Unexpected value: " + when);
        }

        // Append the host reference
        sb.append(' ');
        sb.append((request.getHostRef() == null) ? "-" : request.getHostRef()
                .toString());

        // Append the agent name
        sb.append(' ');
        String agentName = request.getClientInfo().getAgent();
        sb.append((agentName == null) ? "-" : "<@" + agentName + "@>");

        // Append the referrer
        sb.append(' ');
        Reference referrer = request.getReferrerRef();
        sb.append((referrer == null) ? "-" : "<@" + referrer + "@>");
        return sb.toString();
    }

    private String getRequestEntitySize(Request request) {
        try {
            return request.getEntity() == null ? "0" : (request.getEntity().getSize() == -1L ? "-" : Long
                    .toString(request.getEntity().getSize()));
        } catch (Throwable var3) {
            Engine.getLogger(getClass())
                    .log(Level.SEVERE, "Cannot retrieve size of request\'s entity", var3);
            return "error";
        }
    }

    private String getResponseEntitySize(Request request, Response response) {
        return response.isEntityAvailable() && !Status.REDIRECTION_NOT_MODIFIED.equals(response.getStatus())
                && !Status.SUCCESS_NO_CONTENT.equals(response.getStatus()) && !Method.HEAD.equals(request.getMethod()) ? (response
                .getEntity().getSize() == -1L ? "-" : Long.toString(response.getEntity().getSize()))
                : "0";
    }

    private static enum RequestLogFilterState {
        START,
        END;

        private RequestLogFilterState() {
        }
    }

    public static enum RequestLogFilterType {
        INBOUND,
        OUTBOUND;

        private RequestLogFilterType() {
        }
    }
}
