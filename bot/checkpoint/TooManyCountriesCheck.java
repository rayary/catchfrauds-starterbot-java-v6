package bot.checkpoint;

import bot.RiskSystemState;
import bot.data.PaymentRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

public class TooManyCountriesCheck extends AbstractCheck {

    private static final Logger log = Logger.getLogger(TooManyCountriesCheck.class.getSimpleName());

    private static final int MAX_COUNTRIES_PER_SHOPPER = 2;

    private Map<String, Set<String>> emailToCountries = new HashMap<>();
    private Map<String, Set<String>> cardToCountries = new HashMap<>();

    public TooManyCountriesCheck(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects shoppers with too many countries";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        String txId = record.getData("txid");
        //String email = record.getData("shopper_email");
        String email = record.getEmailLocalPart();
        String card = record.getData("card_number_hash");
        String shopperCountry = record.getData("shopper_country");
        String issuerCountry = record.getData("issuer_country");

        // TODO: use cleaned email here

        Set<String> countriesForEmail = emailToCountries.computeIfAbsent(email, k -> new HashSet<>());
        Set<String> countriesForCard = cardToCountries.computeIfAbsent(card, k -> new HashSet<>());

        countriesForEmail.add(shopperCountry);
        countriesForEmail.add(issuerCountry);

        countriesForCard.add(shopperCountry);
        countriesForCard.add(issuerCountry);

        if (countriesForEmail.size() > MAX_COUNTRIES_PER_SHOPPER) {
            log.info("Refused txId " + txId + ": " + countriesForEmail);
            return false;
        }

        if (countriesForCard.size() > MAX_COUNTRIES_PER_SHOPPER) {
            log.info("Refused txId " + txId + ": " + countriesForCard);
            return false;
        }

        return true;
    }
}