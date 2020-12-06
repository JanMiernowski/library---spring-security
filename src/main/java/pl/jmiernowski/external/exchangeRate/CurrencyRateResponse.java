package pl.jmiernowski.external.exchangeRate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CurrencyRateResponse {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Getter
    @Setter
    public static class Rate{
        private String no;
        private LocalDate effectiveDate;
        private Double bid;
        private Double ask;
    }

/*
{"table":"A",
    "currency":"dolar amerykański",
    "code":"USD",
    "rates":
                [{
                "no":"064/A/NBP/2016",
                "effectiveDate":"2016-04-04",
                "mid":3.7254}
                ]}

 */
}
