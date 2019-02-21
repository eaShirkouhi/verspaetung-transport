package com.bcg.dv.challenge.csv.reader;

import com.bcg.dv.challenge.csv.model.Delay;
import com.bcg.dv.challenge.csv.model.Line;
import com.bcg.dv.challenge.csv.model.Stop;
import com.bcg.dv.challenge.csv.model.Time;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class CsvReader {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public List<Line> readLineList() throws IOException {

        File lineFile = new ClassPathResource("csv/lines.csv").getFile();

        List<Line> lineList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(lineFile));

            String csvLine = null;
            Scanner scanner = null;
            int index = 0;
            boolean firstLine = true;

            while ((csvLine = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                Line.LineBuilder lineBuilder = Line.builder();
                scanner = new Scanner(csvLine);
                scanner.useDelimiter(",");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0) {
                        lineBuilder.lineId(Integer.parseInt(data));
                    }
                    if (index == 1) {
                        lineBuilder.lineName(data);
                    }

                    index++;
                }


                lineList.add(lineBuilder.build());

                index = 0;
            }

        } catch (FileNotFoundException e1) {
           log.error(String.format("File '%s' not found", lineFile));
        }

        return lineList;

    }

    public List<Stop> readStopList() throws IOException {

        File stopFile = new ClassPathResource("csv/stops.csv").getFile();

        List<Stop> stopList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopFile));

            String csvLine = null;
            Scanner scanner = null;
            int index = 0;
            boolean firstLine = true;

            while ((csvLine = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                Stop.StopBuilder stopBuilder = Stop.builder();
                scanner = new Scanner(csvLine);
                scanner.useDelimiter(",");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0) {
                        stopBuilder.stopId(Integer.parseInt(data));
                    }
                    if (index == 1) {
                        stopBuilder.x(Integer.parseInt(data));
                    }
                    if (index == 2) {
                        stopBuilder.y(Integer.parseInt(data));
                    }

                    index++;
                }


                stopList.add(stopBuilder.build());

                index = 0;
            }

        } catch (FileNotFoundException e1) {
            log.error(String.format("File '%s' not found", stopFile));        }

        return stopList;

    }

    public List<Delay> readDelayList() throws IOException {

        File delayFile = new ClassPathResource("csv/delays.csv").getFile();

        List<Delay> delayList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(delayFile));

            String csvLine = null;
            Scanner scanner = null;
            int index = 0;
            boolean firstLine = true;

            while ((csvLine = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                Delay.DelayBuilder delayBuilder = Delay.builder();
                scanner = new Scanner(csvLine);
                scanner.useDelimiter(",");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0) {
                        delayBuilder.lineName(data);
                    }
                    if (index == 1) {
                        delayBuilder.delay(Integer.parseInt(data));
                    }

                    index++;
                }


                delayList.add(delayBuilder.build());

                index = 0;
            }

        } catch (FileNotFoundException e1) {
            log.error(String.format("File '%s' not found", delayFile));        }

        return delayList;

    }

    public List<Time> readTimeList() throws IOException {

        File timesFile = new ClassPathResource("csv/times.csv").getFile();
        List<Time> timeList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(timesFile));

            String csvLine = null;
            Scanner scanner = null;
            int index = 0;
            boolean firstLine = true;

            while ((csvLine = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                Time.TimeBuilder timeBuilder = Time.builder();
                scanner = new Scanner(csvLine);
                scanner.useDelimiter(",");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0) {
                        timeBuilder.lineId(Integer.parseInt(data));
                    }
                    if (index == 1) {
                        timeBuilder.stopId(Integer.parseInt(data));
                    }
                    if (index == 2) {
                        timeBuilder.time(LocalTime.parse(data, TIME_FORMATTER));
                    }

                    index++;
                }

                timeList.add(timeBuilder.build());

                index = 0;
            }

        } catch (FileNotFoundException e1) {
            log.error(String.format("File '%s' not found", timesFile));        }

        return timeList;

    }
}






















