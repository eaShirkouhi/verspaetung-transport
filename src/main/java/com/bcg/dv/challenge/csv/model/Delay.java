package com.bcg.dv.challenge.csv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Delay {
    private String lineName;
    private int delay;
}
