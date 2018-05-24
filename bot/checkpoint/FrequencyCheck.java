package bot.checkpoint;

import bot.RiskSystemState;
import bot.data.PaymentRecord;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FrequencyCheck extends AbstractCheck {

    private static final Logger log = Logger.getLogger(FrequencyCheck.class.getSimpleName());

    private Map<String, LocalDateTime> emailLastUse = new HashMap<>();
    private Map<String, LocalDateTime> cardLastUse = new HashMap<>();

    private static final int MIN_MINUTES_BETWEEN_TRANSACTIONS = 5;

    public FrequencyCheck(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects transactions based on email / card use frequency";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        String txId = record.getData("txid");
        //String email = record.getData("shopper_email");
        String email = record.getEmailLocalPart();
        String card = record.getData("card_number_hash");
        LocalDateTime creationDate = record.getCreationDate();

        LocalDateTime emailPreviousCreationDate = emailLastUse.put(email, creationDate);
        LocalDateTime cardPreviousCreationDate = cardLastUse.put(card, creationDate);

        if (emailPreviousCreationDate != null) {
            long minutesBetween = Duration.between(emailPreviousCreationDate, creationDate).toMinutes();
            if (minutesBetween <= MIN_MINUTES_BETWEEN_TRANSACTIONS) {
                log.info("Refused txId " + txId + ": email " + email + " was used " + minutesBetween + " minutes ago");
                return false;
            }
        }

        if (cardPreviousCreationDate != null) {
            long minutesBetween = Duration.between(cardPreviousCreationDate, creationDate).toMinutes();
            if (minutesBetween <= MIN_MINUTES_BETWEEN_TRANSACTIONS) {
                log.info("Refused txId " + txId + ": card " + card + " was used " + minutesBetween + " minutes ago");
                return false;
            }
        }

        return true;
    }
}