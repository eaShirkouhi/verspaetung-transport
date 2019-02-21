package com.bcg.dv.challenge.csv.reader;

import com.bcg.dv.challenge.csv.model.Delay;
import com.bcg.dv.challenge.csv.model.Line;
import com.bcg.dv.challenge.csv.model.Stop;
import com.bcg.dv.challenge.csv.model.Time;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvReaderTest {

    @Autowired
    private CsvReader csvReader;

    @Test
    public void shouldReadLinesFromLinesCsv() throws Exception {

        List<Line> lineList = csvReader.readLineList();
        assertThat(lineList).hasSize(3);

        Line firstLine = lineList.get(0);
        assertThat(firstLine.getLineId()).isEqualTo(0);
        assertThat(firstLine.getLineName()).isEqualTo("M4");
    }

    @Test
    public void shouldStopsFromStopsCsv() throws Exception {

        List<Stop> stopList = csvReader.readStopList();
        assertThat(stopList).hasSize(12);

        Stop firstStop = stopList.get(0);
        assertThat(firstStop.getStopId()).isEqualTo(0);
        assertThat(firstStop.getX()).isEqualTo(1);
        assertThat(firstStop.getY()).isEqualTo(1);
    }

    @Test
    public void shouldReadDelaysFromDelaysCsv() throws Exception {

        List<Delay> delayList = csvReader.readDelayList();
        assertThat(delayList).hasSize(3);

        Delay firstDelay = delayList.get(0);
        assertThat(firstDelay.getLineName()).isEqualTo("M4");
        assertThat(firstDelay.getDelay()).isEqualTo(1);
    }

    @Test
    public void shouldReadTimesFromTimesCsv() throws Exception {

        List<Time> timeList = csvReader.readTimeList();
        assertThat(timeList).hasSize(15);

        Time firstTime = timeList.get(0);
        assertThat(firstTime.getLineId()).isEqualTo(0);
        assertThat(firstTime.getStopId()).isEqualTo(0);
        assertThat(firstTime.getTime()).isEqualTo(LocalTime.of(10, 0, 0));
    }
}