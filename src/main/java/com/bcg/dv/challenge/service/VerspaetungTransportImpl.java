package com.bcg.dv.challenge.service;

import com.bcg.dv.challenge.csv.model.Delay;
import com.bcg.dv.challenge.csv.model.Line;
import com.bcg.dv.challenge.csv.model.Stop;
import com.bcg.dv.challenge.csv.model.Time;
import com.bcg.dv.challenge.csv.reader.CsvReader;
import com.bcg.dv.challenge.exception.LineScheduleException;
import com.bcg.dv.challenge.model.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerspaetungTransportImpl implements VerspaetungTransport {

    private final CsvReader reader;

    private final Map<Point, Map<LocalTime, List<String>>> pointTimeTableMap = new HashMap<>();
    private final Map<LocalTime, Map<Integer, List<String>>> timeStopMap = new HashMap<>();
    private final Map<String, List<SortedMap<LocalTime, LocalTime>>> lineTimeMap = new HashMap<>();

    private final Map<Integer, String> lineMap = new HashMap<>();
    private final Map<String, Integer> delayMap = new HashMap<>();
    private final Map<Integer, Point> stopMap = new HashMap<>();

    private List<Line> lineList = new ArrayList<>();
    private List<Stop> stopList = new ArrayList<>();
    private List<Delay> delayList = new ArrayList<>();
    private List<Time> timeList = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        populateListsFromCsv();
        populatePointTimeTableMap();
        populateTimeStopMap();
        populateLineTimeMap();
    }


    @Override
    public List<String> findVehicleByTimeAndCoordiante(LocalTime time, int x, int y) {

        Point incomingPoint = Point.builder().x(x).y(y).build();

        if (!pointTimeTableMap.containsKey(incomingPoint)) {
            return Collections.emptyList();
        }

        Map<LocalTime, List<String>> timeLineMapForGivenPoint = pointTimeTableMap.get(incomingPoint);

        if (!timeLineMapForGivenPoint.containsKey(time)) {
            return emptyList();
        }

        return timeLineMapForGivenPoint.get(time);
    }

    @Override
    public List<String> findNextArrivingVehicle(int stopId, LocalTime currentTime) {

        LocalTime nextTime = null;
        long minTimeDifference = Long.MAX_VALUE;

        for (LocalTime time : timeStopMap.keySet()) {

            if (time.isAfter(currentTime)){
                long timeDifference = Duration.between(currentTime, time).toMinutes();
                if (timeDifference < minTimeDifference) {
                    minTimeDifference = timeDifference;
                    nextTime = time;
                }
            }
        }

        Map<Integer, List<String>> nextStopAndLineMap = timeStopMap.get(nextTime);
        if(nextStopAndLineMap != null) {
            return nextStopAndLineMap.get(stopId);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isLineCurrentlyDelayed(String lineName, LocalTime currentTime) {

        List<SortedMap<LocalTime, LocalTime>> lineTimeList = lineTimeMap.get(lineName);

        for (SortedMap<LocalTime, LocalTime> sortedTimeMap : lineTimeList) {

            LocalTime arrivingTime = sortedTimeMap.firstKey();
            LocalTime delayedTime = sortedTimeMap.get(arrivingTime);

            if (currentTime.isAfter(arrivingTime) && (currentTime.isBefore(delayedTime) || currentTime.equals(delayedTime))){
                return true;
            }
        }

        throw new LineScheduleException(String.format("For given line '%s' and time '%s' didn't find any departure time", lineName, currentTime));
    }

    private void populateListsFromCsv() {
        try {
            lineList = reader.readLineList();
            delayList = reader.readDelayList();
            stopList = reader.readStopList();
            timeList = reader.readTimeList();

        } catch (IOException e) {
            log.error("Error occured during reading from csv files");
        }

        for (Line line : lineList) {
            if (!lineMap.containsKey(line.getLineId())) {
                lineMap.put(line.getLineId(), line.getLineName());
            }
        }

        for (Delay delay : delayList) {
            if (!delayMap.containsKey(delay.getLineName())) {
                delayMap.put(delay.getLineName(), delay.getDelay());
            }
        }

        for (Stop stop : stopList) {
            if (!stopMap.containsKey(stop.getStopId())) {
                stopMap.put(stop.getStopId(), Point.builder().x(stop.getX()).y(stop.getY()).build());
            }
        }
    }

    private void populatePointTimeTableMap() {

        for (Time time : timeList) {
            String lineName = lineMap.get(time.getLineId());
            LocalTime localTime = time.getTime();
            Point point = stopMap.get(time.getStopId());

            if (!pointTimeTableMap.containsKey(point)) {
                Map<LocalTime, List<String>> timeTable = new HashMap<>();
                timeTable.put(localTime, singletonList(lineName));
                pointTimeTableMap.put(point, timeTable);
            } else {
                Map<LocalTime, List<String>> timeTable = pointTimeTableMap.get(point);
                List<String> vehicleLineList = timeTable.get(localTime);
                if (vehicleLineList == null || vehicleLineList.isEmpty()) {
                    vehicleLineList = new ArrayList<>();
                }
                vehicleLineList.add(lineName);
                timeTable.put(localTime, vehicleLineList);
                pointTimeTableMap.put(point, timeTable);
            }
        }
    }

    private void populateTimeStopMap() {
        for (Time time : timeList) {
            String lineName = lineMap.get(time.getLineId());
            Integer delay = delayMap.get(lineName);
            LocalTime timeAfterDelay = time.getTime().plusMinutes(delay);
            int stopId = time.getStopId();

            if (!timeStopMap.containsKey(timeAfterDelay)){
                Map<Integer, List<String>> stopLinesMap = new HashMap<>();
                stopLinesMap.put(stopId, singletonList(lineName));
                timeStopMap.put(timeAfterDelay, stopLinesMap);
            } else {
                Map<Integer, List<String>> stopLineMap = timeStopMap.get(timeAfterDelay);
                List<String> lineList = stopLineMap.get(stopId);
                if (lineList == null || lineList.isEmpty()) {
                    lineList = new ArrayList<>();
                }
                lineList.add(lineName);
                stopLineMap.put(stopId, lineList);
                timeStopMap.put(timeAfterDelay,stopLineMap);
            }
        }
    }

    private void populateLineTimeMap() {
        for (Time time : timeList) {
            String lineName = lineMap.get(time.getLineId());
            Integer delay = delayMap.get(lineName);
            LocalTime arrivingTime = time.getTime();
            LocalTime timeAfterDelay = arrivingTime.plusMinutes(delay);

            List<SortedMap<LocalTime, LocalTime>> lineTimeList = new ArrayList<>();

            if (!lineTimeMap.containsKey(lineName)){
                SortedMap<LocalTime, LocalTime> arrivingDelayedMap = new TreeMap<>();
                arrivingDelayedMap.put(arrivingTime, timeAfterDelay);
                lineTimeList.add(arrivingDelayedMap);
                lineTimeMap.put(lineName, lineTimeList);
            } else {
                List<SortedMap<LocalTime, LocalTime>> arrivingDelayedMapList = lineTimeMap.get(lineName);
                SortedMap<LocalTime, LocalTime> arrivingDelayedMap = new TreeMap<>();
                arrivingDelayedMap.put(arrivingTime, timeAfterDelay);
                arrivingDelayedMapList.add(arrivingDelayedMap);

            }
        }
    }

}
