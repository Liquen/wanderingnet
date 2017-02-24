package org.wanderingnet.common;

import java.util.UUID;

/**
 * Created by guillermoblascojimenez on 07/03/16.
 */
public class MicroServiceResponse {

    private UUID requestUuid;
    private long requestTimestamp;
    private UUID responseUuid;
    private long responseTimestamp;

    protected MicroServiceResponse() {
    }

    private MicroServiceResponse(UUID requestUuid, long requestTimestamp, UUID responseUuid, long responseTimestamp) {
        this.requestUuid = requestUuid;
        this.requestTimestamp = requestTimestamp;
        this.responseUuid = responseUuid;
        this.responseTimestamp = responseTimestamp;
    }

    private MicroServiceResponse(MicroServiceRequest request, UUID responseUuid, long responseTimestamp) {
        this(request.getRequestUuid(), request.getRequestTimestamp(), responseUuid, responseTimestamp);
    }

    protected MicroServiceResponse(MicroServiceRequest request) {
        this(request.getRequestUuid(), request.getRequestTimestamp(), UUID.randomUUID(), System.currentTimeMillis());
    }

    public UUID getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(UUID requestUuid) {
        this.requestUuid = requestUuid;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public UUID getResponseUuid() {
        return responseUuid;
    }

    public void setResponseUuid(UUID responseUuid) {
        this.responseUuid = responseUuid;
    }

    public long getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(long responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }
}
