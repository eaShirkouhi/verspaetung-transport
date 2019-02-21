package com.bcg.dv.challenge.service;

import com.bcg.dv.challenge.ChallengeApplication;
import com.bcg.dv.challenge.exception.LineScheduleException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(ChallengeApplication.class)
public class VerspaetungTransportImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private VerspaetungTransport verspaetungTransport;

    @Test
    public void shouldFindVehicleForGivenTimeAndCoordinate() throws Exception {

        List<String> lines = verspaetungTransport.findVehicleByTimeAndCoordiante(LocalTime.of(10, 8, 0), 2, 9);
        assertThat(lines).hasSize(2);
        assertThat(lines).containsExactlyInAnyOrder("200", "S75");
    }

    @Test
    public void shouldFindNexArrivingVehicle() throws Exception {

        List<String> lineList = verspaetungTransport.findNextArrivingVehicle(3, LocalTime.of(10, 7, 0));
        assertThat(lineList).hasSize(1);
        assertThat(lineList.get(0)).isEqualTo("M4");
    }

    @Test
    public void shouldReturnDelayedTrueIfGivenLineIsCurrentlyDelayed() throws Exception {
        boolean delayed = verspaetungTransport.isLineCurrentlyDelayed("M4", LocalTime.of(10, 8, 0));
        assertThat(delayed).isTrue();
    }

    @Test
    public void shouldReturnDelayedFalseIfGivenLineIsCurrentlyNotDelayed() throws Exception {
        boolean delayed = verspaetungTransport.isLineCurrentlyDelayed("M4", LocalTime.of(10, 9, 0));
        assertThat(delayed).isFalse();
    }

    @Test
    public void shouldThrowExceptionIfNoTimeFoundForGivenLineAndCurrentTime() throws Exception {

        thrown.expect(LineScheduleException.class);
        verspaetungTransport.isLineCurrentlyDelayed("M4", LocalTime.of(11, 0, 0));
    }
}