package com.bcg.dv.challenge.controller;

import com.bcg.dv.challenge.exception.LineScheduleException;
import com.bcg.dv.challenge.service.VerspaetungTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerspaetungTransportController.class)
@RunWith(SpringRunner.class)
public class VerspaetungTransportControllerTest {

    @MockBean
    private VerspaetungTransport verspaetungTransport;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldFindVehicleForGivenTimeAndCoordinateAndReturnOK() throws Exception {

        when(verspaetungTransport.findVehicleByTimeAndCoordiante(LocalTime.of(10, 8, 0), 2, 9)).thenReturn(Arrays.asList("200", "S75"));

        List<String> expectedList = Arrays.asList("200", "S75");

        mockMvc.perform(get(Routes.GET_VEHICLE_BY_COORDINATE_AND_TIME, 2, 9, LocalTime.of(10, 8, 0)).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentType(), is(APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(result -> assertThat(om.writeValueAsString(expectedList), is(result.getResponse().getContentAsString())));
    }

    @Test
    public void shouldNotFindVehicleForGivenTimeAndCoordinateAndReturn404() throws Exception {

        when(verspaetungTransport.findVehicleByTimeAndCoordiante(LocalTime.of(10, 8, 0), 2, 9)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(Routes.GET_VEHICLE_BY_COORDINATE_AND_TIME, 2, 9, LocalTime.of(10, 8, 0)).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindNextArrivingVehicleForGivenStopIdReturnOK() throws Exception {

        when(verspaetungTransport.findNextArrivingVehicle(3, LocalTime.of(10, 8, 0))).thenReturn(Collections.singletonList("M4"));

        List<String> expectedList = Collections.singletonList("M4");

        mockMvc.perform(get(Routes.GET_NEXT_ARRIVING_VEHICLE, 3, LocalTime.of(10, 8, 0)).accept(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentType(), is(APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(result -> assertThat(om.writeValueAsString(expectedList), is(result.getResponse().getContentAsString())));
    }

    @Test
    public void shouldNotFindNextArrivingVehicleForGivenStopIdAndReturn404() throws Exception {

        when(verspaetungTransport.findNextArrivingVehicle(3, LocalTime.of(10, 8, 0))).thenReturn(Collections.emptyList());

        mockMvc.perform(get(Routes.GET_NEXT_ARRIVING_VEHICLE, 3, LocalTime.of(10, 8, 0)).accept(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldIndicateGivenLineIsDelayedAndReturnOK() throws Exception {

        when(verspaetungTransport.isLineCurrentlyDelayed("M4", LocalTime.of(10, 8, 0))).thenReturn(true);

        mockMvc.perform(get(Routes.GET_IS_VEHICLE_DELAYED, "M4", LocalTime.of(10, 8, 0)).accept(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentType(), is(APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(result -> assertThat(om.writeValueAsString(true), is(result.getResponse().getContentAsString())));
    }

    @Test
    public void shouldNotReturnTrueOrFalseIfNotTimeScheduleFoundForGivenLineAndReturn404() throws Exception {

        when(verspaetungTransport.isLineCurrentlyDelayed("M4", LocalTime.of(22, 0, 0))).thenThrow(new LineScheduleException("Didn't found any time schedule for a given line and time"));

        mockMvc.perform(get(Routes.GET_IS_VEHICLE_DELAYED, "M4", LocalTime.of(22, 0, 0)).accept(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
    }
}