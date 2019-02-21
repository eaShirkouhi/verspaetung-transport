package com.bcg.dv.challenge.service;

import java.time.LocalTime;
import java.util.List;

public interface VerspaetungTransport {

    List<String> findVehicleByTimeAndCoordiante(LocalTime time, int x, int y);

    List<String> findNextArrivingVehicle(int stopId, LocalTime currentTime);

    boolean isLineCurrentlyDelayed(String lineName, LocalTime currentTime);
}
