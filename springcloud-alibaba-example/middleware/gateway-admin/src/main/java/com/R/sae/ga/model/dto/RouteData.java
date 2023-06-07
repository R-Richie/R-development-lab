package com.R.sae.ga.model.dto;

import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class RouteData {

    private String id;

    private List<PredicateDefinition> predicates;

    private List<FilterDefinition> filters;

    private URI uri;

    private Map<String, Object> metadata;

    private int order = 0;
}
