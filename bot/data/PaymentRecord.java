/*
 * Copyright 2018 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package bot.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * PaymentRecord
 *
 * This class stores all information about a record. Methods can
 * be implemented here that perform calculations on the record data
 *
 * Currently only stores a map of all the string values of the record
 * and additionally parses and stores the amount.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class PaymentRecord {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String filterRemoveDomain = "@.*$";        // pattern to be matched. (any character after @)
    private String filterLocalPart = "[^a-zA-Z0-9]";
    private String replacement = "";  // replacement pattern
    private Pattern patternRemoveDomain, patternLocalPart;

    private HashMap<String, String> stringValueMap;
    private double amount;
    private LocalDateTime creationDate;
    private String localPart;

    public PaymentRecord(String[] recordFormat, String record) throws InstantiationError {
        this.stringValueMap  = new HashMap<>();
        String[] values = record.split(",");

        patternRemoveDomain = Pattern.compile(filterRemoveDomain, Pattern.CASE_INSENSITIVE);
        patternLocalPart = Pattern.compile(filterLocalPart, Pattern.CASE_INSENSITIVE);

        if (recordFormat.length != values.length) {
            throw new InstantiationError("Record does not match given format");
        }

        for (int i = 0; i < recordFormat.length; i++) {
            this.stringValueMap.put(recordFormat[i], values[i]);
            this.parseValues(recordFormat[i], values[i]);
        }
    }

    private void parseValues(String key, String value) {
        try {
            switch (key) {
                case "amount":
                    this.amount = Double.parseDouble(value);
                    break;
                case "creation_date":
                    this.creationDate = LocalDateTime.parse(value, formatter);
                    break;
                case "shopper_email":
                    // Probably exists a cleaner filter than matching twice...
                    String sanitized_addr  = patternRemoveDomain.matcher(value).replaceAll(replacement);
                    sanitized_addr = patternLocalPart.matcher(sanitized_addr).replaceAll(replacement);
                    this.localPart = sanitized_addr;
                    break;
                // case "xxx": You can add more value parsing here
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse record value '%s' for key '%s'", value, key));
        }
    }

    public String getData(String key) {
        return this.stringValueMap.get(key);
    }

    public double getAmount() {
        return this.amount;
    }

    // TODO: Parse the record values (such as date, etc.), which are now just strings
    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    // TODO: Implement useful methods to perform calculations on the record data
    public String getEmailLocalPart() {
        return this.localPart;
    }
}
