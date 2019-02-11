package com.ia.operation.events.deleted;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyDeletedEvent {
    private String companyId;
}
