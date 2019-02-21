package com.bcg.dv.challenge.csv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Builder
@Getter
public class Time {

    private final int lineId;
    private final int stopId;
    private final LocalTime time;
}
