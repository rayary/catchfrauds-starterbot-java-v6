package bot.checkpoint;

import bot.RiskSystemState;
import bot.data.PaymentRecord;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class HighRiskCountryCheck extends AbstractCheck {

    private static final Logger log = Logger.getLogger(FrequencyCheck.class.getSimpleName());

    private static final Set<String> fraudCountries = new HashSet<>(Arrays.asList("NI", "RO", "UA"));
    private static final long MAX_AMOUNT = 5000;

    public HighRiskCountryCheck(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects high risk transaction based on country and amount";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        String txId = record.getData("txid");
        String shopperCountry = record.getData("shopper_country");
        String issuerCountry = record.getData("issuer_country");
        long amount = Long.parseLong(record.getData("amount"));

        boolean shopperCountryDifferentFromIssuerCountry = false;
        if (! shopperCountry.equals(issuerCountry)) {
            shopperCountryDifferentFromIssuerCountry = true;
        }

        boolean highRiskCountry = false;
        if (fraudCountries.contains(shopperCountry)) {
            highRiskCountry = true;
        }

        if (shopperCountryDifferentFromIssuerCountry && amount > MAX_AMOUNT) {
            log.info("Refused txId " + txId + ": shopperCountry " + shopperCountry + " doesn't match issuerCountry " + issuerCountry);
            return false;
        }

        if (highRiskCountry) {
            log.info("Refused txId " + txId + ": shopperCountry " + shopperCountry +" is a high risk country");
            return false;
        }

        return true;
    }
}