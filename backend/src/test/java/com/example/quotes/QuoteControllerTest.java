package com.example.quotes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuoteRepository quoteRepository;

    @BeforeEach
    public void setUp() {
        quoteRepository.deleteAll();
    }

    @Test
    public void testCreateQuote() throws Exception {
        Quote quote = new Quote("Test quote");

        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test quote"));
    }

    @Test
    public void testGetAllQuotes() throws Exception {
        quoteRepository.save(new Quote("Quote 1"));
        quoteRepository.save(new Quote("Quote 2"));

        mockMvc.perform(get("/api/quotes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetQuoteById() throws Exception {
        Quote quote = quoteRepository.save(new Quote("Test quote"));

        mockMvc.perform(get("/api/quotes/{id}", quote.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test quote"));
    }

    @Test
    public void testUpdateQuote() throws Exception {
        Quote quote = quoteRepository.save(new Quote("Original quote"));
        Quote updatedQuote = new Quote("Updated quote");

        mockMvc.perform(put("/api/quotes/{id}", quote.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedQuote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated quote"));
    }

    @Test
    public void testDeleteQuote() throws Exception {
        Quote quote = quoteRepository.save(new Quote("To be deleted"));

        mockMvc.perform(delete("/api/quotes/{id}", quote.getId()))
                .andExpect(status().isNoContent());
    }
}
