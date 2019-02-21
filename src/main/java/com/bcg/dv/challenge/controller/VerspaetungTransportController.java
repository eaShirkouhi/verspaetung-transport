package com.bcg.dv.challenge.controller;

import com.bcg.dv.challenge.exception.LineScheduleException;
import com.bcg.dv.challenge.service.VerspaetungTransport;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RequiredArgsConstructor
@RestController
public class VerspaetungTransportController {

    private final VerspaetungTransport verspaetungTransport;

    @GetMapping(value = Routes.GET_VEHICLE_BY_COORDINATE_AND_TIME, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getVehicleByTimeAndCoordinate(@PathVariable int x, @PathVariable int y, @PathVariable @DateTimeFormat(iso= DateTimeFormat.ISO.TIME) LocalTime time ) {
        List<String> vehicleList = verspaetungTransport.findVehicleByTimeAndCoordiante(time, x, y);

        if (vehicleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicleList, OK);
    }


    @GetMapping(value = Routes.GET_NEXT_ARRIVING_VEHICLE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getNextArrivingVehicle(@PathVariable int stopId, @PathVariable @DateTimeFormat(iso= DateTimeFormat.ISO.TIME) LocalTime time) {

        List<String> vehicleList = verspaetungTransport.findNextArrivingVehicle(stopId, time);

        if (vehicleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicleList, OK);
    }


    @GetMapping(value = Routes.GET_IS_VEHICLE_DELAYED, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> isVehicleDelayed(@PathVariable String lineName, @PathVariable @DateTimeFormat(iso= DateTimeFormat.ISO.TIME) LocalTime time) {

        try {
            boolean delayed = verspaetungTransport.isLineCurrentlyDelayed(lineName, time);
            return new ResponseEntity<>(delayed, OK);
        } catch (LineScheduleException e){
           return ResponseEntity.notFound().build();
        }
    }
}
