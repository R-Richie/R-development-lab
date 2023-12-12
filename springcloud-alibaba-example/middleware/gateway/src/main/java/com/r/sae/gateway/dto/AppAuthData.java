package com.r.sae.gateway.dto;

import lombok.Data;

@Data
public class AppAuthData {
    private String appKey;

    private String appSecret;

    private Boolean enabled;

    private Boolean open;

}
