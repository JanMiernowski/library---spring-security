package pl.jmiernowski.external.exchangeRate;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.jmiernowski.config.NbpProperties;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    private final RestTemplate restTemplate;
    private final NbpProperties nbpProperties;
    private final ObjectMapper objectMapper;

    public CurrencyRateResponse findCurrencyRate(String currency, LocalDate forDate) throws JsonProcessingException {
        String address =  String.format("%s/exchangerates/rates/c/%s/%s/?format=json",
                nbpProperties.getUrl(), currency, forDate);

        ResponseEntity<String> rateResponse =
                restTemplate.exchange(address, HttpMethod.GET, HttpEntity.EMPTY, String.class);


        if(rateResponse.getStatusCode().is2xxSuccessful()){
            return objectMapper.readValue(rateResponse.getBody(), CurrencyRateResponse.class);
        }

        if(rateResponse.getStatusCode().is4xxClientError()){
            return findCurrencyRate(currency, forDate.minusDays(1));
        }

        throw new IllegalStateException("It was impossible to fetch currency rate " + rateResponse.getStatusCodeValue());
    }

    public List<CurrencyRateResponseForTableAandB> findCurrencyRateForTableAandB(LocalDate forDate) throws JsonProcessingException {
        String address =  String.format("%s/exchangerates/tables/b/%s",
                nbpProperties.getUrl(), forDate);

        ResponseEntity<String> rateResponse =
                restTemplate.exchange(address, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        rateResponse.getBody();

        if(rateResponse.getStatusCode().is2xxSuccessful()){

            return objectMapper.readValue(rateResponse.getBody(), new TypeReference<List<CurrencyRateResponseForTableAandB>>(){});
        }


        if(rateResponse.getStatusCode().is4xxClientError()){
            return findCurrencyRateForTableAandB(forDate);
        }

        throw new IllegalStateException("It was impossible to fetch currency rate " + rateResponse.getStatusCodeValue());
    }

    public List<CurrencyRateResponseForTableC> findCurrencyRateForTableC(LocalDate forDate) throws JsonProcessingException {
        String address =  String.format("%s/exchangerates/tables/c/%s",
                nbpProperties.getUrl(), forDate);

        ResponseEntity<String> rateResponse =
                restTemplate.exchange(address, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        rateResponse.getBody();

        if(rateResponse.getStatusCode().is2xxSuccessful()){

            return objectMapper.readValue(rateResponse.getBody(), new TypeReference<List<CurrencyRateResponseForTableC>>(){});
        }


        if(rateResponse.getStatusCode().is4xxClientError()){
            return findCurrencyRateForTableC(forDate);
        }

        throw new IllegalStateException("It was impossible to fetch currency rate " + rateResponse.getStatusCodeValue());
    }

}




///exchangerates/rates/a/usd/2020-12-04/?format=json
