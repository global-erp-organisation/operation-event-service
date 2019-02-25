package com.ia.operation.queries.category;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryGetQuery {
    private String categoryId;
}
