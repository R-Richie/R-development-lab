package com.r.sae.ga.listener;

import com.r.sae.ga.model.dto.AppAuthData;
import com.r.sae.ga.model.dto.RouteData;
import com.r.sae.ga.enums.DataEventTypeEnum;

import java.util.List;

public interface DataChangedListener {
    /**
     * invoke this method when AppAuth was received.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    default void onAppAuthChanged(List<AppAuthData> changed, DataEventTypeEnum eventType) {
    }



    /**
     * invoke this method when Selector was received.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    default void onRouteChanged(List<RouteData> changed, DataEventTypeEnum eventType) {
    }



}
