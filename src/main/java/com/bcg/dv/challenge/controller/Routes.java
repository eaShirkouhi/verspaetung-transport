package com.bcg.dv.challenge.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Routes {

    static final String GET_IS_VEHICLE_DELAYED = "/vehicles/{lineName}/{time}/delayed";
    static final String GET_NEXT_ARRIVING_VEHICLE = "/vehicles/{stopId}/{time}" ;
    static final String GET_VEHICLE_BY_COORDINATE_AND_TIME = "/vehicles/{x}/{y}/{time}";
}
