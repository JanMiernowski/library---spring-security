package pl.jmiernowski.external.exchangeRate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CurrencyRateResponseForTableC {

    private String table;
    private String no;
    private LocalDate tradingDate;
    private LocalDate effectiveDate;
    private List<Rate> rates;

    @Getter
    @Setter
    public static class Rate{
        private String currency;
        private String code;
        private Double bid;
        private Double ask;
    }

/*
<Table>B</Table>
<No>013/B/NBP/2016</No>
<EffectiveDate>2016-03-30</EffectiveDate>
<Rates>
<Rate>
<Currency>afgani (Afganistan)</Currency>
<Code>AFN</Code>
<Mid>0.054585</Mid>

 */
}
