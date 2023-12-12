package com.r.sae.gateway.syncdata;

import com.r.sae.gateway.dto.AppAuthData;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public class SignAuthDataCache {
    private static final SignAuthDataCache INSTANCE = new SignAuthDataCache();

    private static final ConcurrentMap<String, AppAuthData> AUTH_MAP = Maps.newConcurrentMap();

    private SignAuthDataCache(){

    }

    public static SignAuthDataCache getInstance(){return INSTANCE;}

    public void cacheAuthData(final AppAuthData data){AUTH_MAP.put(data.getAppKey(), data);}

    public void removeAuthData(final AppAuthData data){AUTH_MAP.remove(data.getAppKey());}

    public AppAuthData obtainAuthData(final String appKey){return AUTH_MAP.get(appKey);}

    public void removeAllAuthData(){
        AUTH_MAP.clear();
    }
}
