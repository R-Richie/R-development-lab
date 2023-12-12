package com.r.sae.gateway.syncdata.zookeeper;

import com.r.sae.gateway.constant.DefaultPathConstants;
import com.r.sae.gateway.dto.AppAuthData;
import com.r.sae.gateway.syncdata.AuthDataSubscriber;
import com.r.sae.gateway.syncdata.RouteDataSubscriber;
import com.r.sae.gateway.syncdata.SyncDataService;
import com.r.sae.gateway.utils.JacksonUtils;
import com.google.common.base.Strings;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ZookeeperSyncDataService implements SyncDataService {

    private final ZookeeperClient zkClient;


    private final RouteDataSubscriber routeDataSubscribe;

    private final List<AuthDataSubscriber> authDataSubscribers;

    public ZookeeperSyncDataService(ZookeeperClient zkClient,
                                    RouteDataSubscriber routeDataSubscribe,
                                    List<AuthDataSubscriber> authDataSubscribers) {
        this.zkClient = zkClient;
        this.routeDataSubscribe = routeDataSubscribe;
        this.authDataSubscribers = authDataSubscribers;
        watcherData();
        watchAppAuth();
    }
    private void watcherData() {
        zkClient.addCache(DefaultPathConstants. ROUTE_PARENT, new RouteCacheListener());
    }

    private void watchAppAuth() {
        zkClient.addCache(DefaultPathConstants.APP_AUTH_PARENT, new AuthCacheListener());
    }

    @Override
    public void close(){
        if (Objects.nonNull(zkClient)) {
            zkClient.close();
        }
    }
    private void cacheRouteData(final RouteDefinition routeData) {
        Optional.ofNullable(routeData)
                .ifPresent(data -> Optional.ofNullable(routeDataSubscribe)
                        .ifPresent(e -> e.onSubscribe(data)));
    }

    private void unCacheRouteData(final String dataPath) {
        RouteDefinition routeData = new RouteDefinition();
        final String selectorId = dataPath.substring(dataPath.lastIndexOf("/") + 1);
        final String str = dataPath.substring(DefaultPathConstants.ROUTE_PARENT.length());
//        final String pluginName = str.substring(1, str.length() - selectorId.length() - 1);
        routeData.setId(selectorId);


        Optional.ofNullable(routeDataSubscribe)
                .ifPresent(e -> e.unSubscribe(routeData));
    }
    private void cacheAuthData(final AppAuthData appAuthData) {
        Optional.ofNullable(appAuthData)
                .ifPresent(data -> authDataSubscribers.forEach(e -> e.onSubscribe(data)));
    }

    private void unCacheAuthData(final String dataPath) {
        final String key = dataPath.substring(DefaultPathConstants.APP_AUTH_PARENT.length() + 1);
        AppAuthData appAuthData = new AppAuthData();
        appAuthData.setAppKey(key);
        authDataSubscribers.forEach(e -> e.unSubscribe(appAuthData));
    }
    abstract static class AbstractDataSyncListener implements TreeCacheListener {
        @Override
        public final void childEvent(final CuratorFramework client, final TreeCacheEvent event) {
            ChildData childData = event.getData();
            if (null == childData) {
                return;
            }
            String path = childData.getPath();
            if (Strings.isNullOrEmpty(path)) {
                return;
            }
            event(event.getType(), path, childData);
        }

        /**
         * data sync event.
         *
         * @param type tree cache event type.
         * @param path tree cache event path.
         * @param data tree cache event data.
         */
        protected abstract void event(TreeCacheEvent.Type type, String path, ChildData data);
    }
    class AuthCacheListener extends AbstractDataSyncListener {

        @Override
        public void event(final TreeCacheEvent.Type type, final String path, final ChildData data) {
            // if not uri register path, return.
            if (!path.contains(DefaultPathConstants.APP_AUTH_PARENT)) {
                return;
            }

            if (type.equals(TreeCacheEvent.Type.NODE_REMOVED)) {
                unCacheAuthData(path);
                return;
            }

            // create or update
            Optional.ofNullable(data)
                    .ifPresent(e ->
                            cacheAuthData(JacksonUtils.getInstance()
                                    .fromJson(
                                            new String(data.getData(), StandardCharsets.UTF_8), AppAuthData.class)));
        }
    }

    class RouteCacheListener extends AbstractDataSyncListener {

        @Override
        public void event(final TreeCacheEvent.Type type, final String path, final ChildData data) {

            // if not uri register path, return.
            if (!path.contains(DefaultPathConstants.ROUTE_PARENT)) {
                return;
            }
            if (!zkClient.getChildren(path).isEmpty()) {
                return;
            }
            if (type.equals(TreeCacheEvent.Type.NODE_REMOVED)) {
                unCacheRouteData(path);
                return;
            }

            // create or update
            Optional.ofNullable(data)
                    .ifPresent(e -> cacheRouteData(JacksonUtils.getInstance().fromJson(new String(data.getData(), StandardCharsets.UTF_8), RouteDefinition.class)));
        }
    }
}
