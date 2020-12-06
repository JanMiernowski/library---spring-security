package pl.jmiernowski.external.exchangeRate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CurrencyRateResponseForTableAandB {

    private String table;
    private String no;
    private LocalDate effectiveDate;
    private List<Rate> rates;

    @Getter
    @Setter
    public static class Rate{
        private String currency;
        private String code;
        private Double mid;
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
