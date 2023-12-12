package com.r.sae.gateway.syncdata;

import com.r.sae.gateway.dto.AppAuthData;

public interface AuthDataSubscriber {
    void onSubscribe(AppAuthData appAuthData);

    void unSubscribe(AppAuthData appAuthData);

    default void refresh(){}
}
