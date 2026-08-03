
package com.example.orderservice.controller;

import com.example.orderservice.dto.RevenueReportDto;
import com.example.orderservice.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    public void getRevenueReport_shouldReturnRevenueReport() throws Exception {
        LocalDate date = LocalDate.of(2023, 1, 1);
        RevenueReportDto.RevenueByDate revenueByDate = new RevenueReportDto.RevenueByDate(date, new BigDecimal("100.00"));
        RevenueReportDto revenueReportDto = new RevenueReportDto(Collections.singletonList(revenueByDate), new BigDecimal("100.00"));

        when(reportService.getRevenueReport()).thenReturn(revenueReportDto);

        mockMvc.perform(get("/api/reports/revenue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(100.00))
                .andExpect(jsonPath("$.revenueByDate[0].date").value("2023-01-01"))
                .andExpect(jsonPath("$.revenueByDate[0].revenue").value(100.00));
    }
}
