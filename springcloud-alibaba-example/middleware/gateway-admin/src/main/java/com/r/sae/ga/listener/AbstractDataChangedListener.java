package com.r.sae.ga.listener;

import com.r.sae.ga.model.dto.AppAuthData;
import com.r.sae.ga.model.dto.ConfigData;
import com.r.sae.ga.model.dto.RouteData;
import com.r.sae.ga.enums.ConfigGroupEnum;
import com.r.sae.ga.enums.DataEventTypeEnum;
import com.r.sae.ga.service.AppAuthService;
import com.r.sae.ga.service.RouteService;
import com.r.sae.ga.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.r.sae.ga.enums.ConfigGroupEnum.ROUTE;

@Slf4j
public abstract class AbstractDataChangedListener implements DataChangedListener, InitializingBean {
    /**
     * The constant CACHE.
     */
    protected static final ConcurrentMap<String, ConfigDataCache> CACHE = new ConcurrentHashMap<>();

    @Resource
    private AppAuthService appAuthService;
    @Resource
    private RouteService routeService;

    public ConfigData<?> fetchConfig(final ConfigGroupEnum groupKey) {
        ConfigDataCache config = CACHE.get(groupKey.name());
        switch (groupKey) {
            case APP_AUTH:
                return buildConfigData(config, AppAuthData.class);
            case ROUTE:
                return buildConfigData(config, RouteData.class);

            default:
                throw new IllegalStateException("Unexpected groupKey: " + groupKey);
        }
    }
    private <T> ConfigData<T> buildConfigData(final ConfigDataCache config, final Class<T> dataType) {
        return new ConfigData<>(config.getMd5(), config.getLastModifyTime(), GsonUtils.getInstance().fromList(config.getJson(), dataType));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.refreshLocalCache();
        this.afterInitialize();
    }

    protected abstract void afterInitialize();

    @Override
    public void onAppAuthChanged(List<AppAuthData> changed, DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updateAppAuthCache();
        this.afterAppAuthChanged(changed, eventType);
    }
    protected void updateAppAuthCache() {
        //todo listAll Method
        this.updateCache(ConfigGroupEnum.APP_AUTH, appAuthService.listAll());
//                appAuthService.listAll());
    }
    /**
     * if md5 is not the same as the original, then update lcoal cache.
     * @param group ConfigGroupEnum
     * @param <T> the type of class
     * @param data the new config data
     */
    protected <T> void updateCache(final ConfigGroupEnum group, final List<T> data) {
        String json = GsonUtils.getInstance().toJson(data);
        ConfigDataCache newVal = new ConfigDataCache(group.name(), json, DigestUtils.md5Hex(json), System.currentTimeMillis());
        ConfigDataCache oldVal = CACHE.put(newVal.getGroup(), newVal);
        log.info("update config cache[{}], old: {}, updated: {}", group, oldVal, newVal);
    }
    /**
     * After app auth changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterAppAuthChanged(final List<AppAuthData> changed, final DataEventTypeEnum eventType) {
    }

    @Override
    public void onRouteChanged(List<RouteData> changed, DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updateRouteCache();
        this.afterRouteChanged(changed, eventType);
    }

    protected void updateRouteCache() {
        //todo listAll Method
        this.updateCache(ROUTE, routeService.listAll());
    }

    /**
     * After app auth changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterRouteChanged(final List<RouteData> changed, final DataEventTypeEnum eventType) {
    }

    /**
     * refresh local cache.
     */
    protected void refreshLocalCache() {
        this.updateAppAuthCache();
        this.updateRouteCache();

    }
}
