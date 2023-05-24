package com.R.sae.gateway.syncdata;

import com.R.sae.gateway.dto.AppAuthData;

public interface AuthDataSubscriber {
    void onSubscribe(AppAuthData appAuthData);

    void unSubscribe(AppAuthData appAuthData);

    default void refresh(){}
}
