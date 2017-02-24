package org.wanderingnet.common;

import java.util.UUID;

/**
 * Created by guillermoblascojimenez on 07/03/16.
 */
public class MicroServiceRequest {

    private UUID requestUuid;
    private long requestTimestamp;

    private MicroServiceRequest(UUID requestUuid, long requestTimestamp) {
        this.requestUuid = requestUuid;
        this.requestTimestamp = requestTimestamp;
    }

    protected MicroServiceRequest() {
        this(UUID.randomUUID(), System.currentTimeMillis());
    }

    public UUID getRequestUuid() {
        return requestUuid;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }
}
