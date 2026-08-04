package com.example.quotes;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Optional<Quote> getQuoteById(Long id) {
        return quoteRepository.findById(id);
    }

    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Quote updateQuote(Long id, Quote quoteDetails) {
        Quote quote = quoteRepository.findById(id).orElseThrow();
        quote.setText(quoteDetails.getText());
        return quoteRepository.save(quote);
    }

    public void deleteQuote(Long id) {
        quoteRepository.deleteById(id);
    }
}
