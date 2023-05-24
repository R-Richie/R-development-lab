package com.R.sae.gateway.syncdata;

import com.R.sae.gateway.dto.AppAuthData;

public class SignAuthDataSubscriber implements AuthDataSubscriber{
    @Override
    public void onSubscribe(AppAuthData appAuthData) {
        SignAuthDataCache.getInstance().cacheAuthData(appAuthData);
    }

    @Override
    public void unSubscribe(AppAuthData appAuthData) {
        SignAuthDataCache.getInstance().removeAuthData(appAuthData);
    }

    @Override
    public void refresh() {
        SignAuthDataCache.getInstance().removeAllAuthData();
    }
}
