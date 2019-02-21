package com.bcg.dv.challenge.csv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Stop {

    private int stopId;
    private int x;
    private int y;
}
