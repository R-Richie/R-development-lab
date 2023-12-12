package com.r.sae.ga.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppAuthData {
    private String appKey;

    private String appSecret;

    private Boolean enabled;

    private Boolean open;
}
