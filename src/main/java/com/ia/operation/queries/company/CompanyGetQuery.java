package com.ia.operation.queries.company;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyGetQuery {
    private String companyId;
}
