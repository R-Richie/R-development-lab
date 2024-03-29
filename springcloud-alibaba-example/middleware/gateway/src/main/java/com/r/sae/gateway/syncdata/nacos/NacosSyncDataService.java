package com.r.sae.gateway.syncdata.nacos;

import com.r.sae.gateway.constant.NacosPathConstants;
import com.r.sae.gateway.syncdata.AuthDataSubscriber;
import com.r.sae.gateway.syncdata.RouteDataSubscriber;
import com.r.sae.gateway.syncdata.SyncDataService;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.List;

public class NacosSyncDataService extends NacosCacheHandler implements SyncDataService {

    public NacosSyncDataService(ConfigService configService, RouteDataSubscriber routeDataSubscribe,
                                List<AuthDataSubscriber> authDataSubscribers) {
        super(configService, routeDataSubscribe, authDataSubscribers);
        start();
    }
    public void start(){

    }

    @Override
    public void close() throws Exception {
        LISTENERS.forEach((dataId, list) ->{
            list.forEach(listener -> getConfigService().removeListener(dataId, NacosPathConstants.GROUP, listener));
            list.clear();
        });
    }
}
