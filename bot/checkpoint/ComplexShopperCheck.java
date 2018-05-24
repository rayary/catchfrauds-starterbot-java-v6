package bot.checkpoint;

import bot.RiskSystemState;
import bot.data.PaymentRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

public class ComplexShopperCheck extends AbstractCheck {

    private static final Logger log = Logger.getLogger(ComplexShopperCheck.class.getSimpleName());

    private static final int MAX_CARDS_PER_SHOPPER = 2;
    private static final int MAX_EMAILS_PER_CARD = 1;

    private Map<String, Set<String>> emailToCards = new HashMap<>();
    private Map<String, Set<String>> cardToEmails = new HashMap<>();

    public ComplexShopperCheck(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects transactions that are linked to multiple shoppers / cards";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        String txId = record.getData("txid");
        //String email = record.getData("shopper_email");
        String email = record.getEmailLocalPart();
        String card = record.getData("card_number_hash");
        // TODO: use cleaned email address here
        // TODO: reduce email per card to 1 per address


        Set<String> cards = emailToCards.computeIfAbsent(email, k -> new HashSet<>());
        cards.add(card);

        Set<String> emails = cardToEmails.computeIfAbsent(card, k -> new HashSet<>());
        emails.add(email);

        if (cards.size() > MAX_CARDS_PER_SHOPPER) {
            log.info("Refused txId " + txId + ": email " + email + " has " + cards.size() + " linked cards");
            return false;
        }

        if (emails.size() > MAX_EMAILS_PER_CARD) {
            log.info("Refused txId " + txId + ": card " + card + " has " + emails.size() + " linked emails");
            return false;
        }

        return true;
    }
}
