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

package bot.checkpoint;

import bot.RiskSystemState;
import bot.data.PaymentRecord;

/**
 * bot.checkpoint.ExampleCheck1
 *
 * Example check
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Check1 extends AbstractCheck {

    public Check1(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects transaction in different country and amount > 2000";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        System.err.println("Check1: Checking record " + record.getData("txid"));

        boolean diffCountry = !record.getData("issuer_country").equals(record.getData("shopper_country"));
        boolean fraudThreshold = record.getAmount() > 2000;

        /* amount was chosen to be 2000 to block 75% of different country transaction fraud. false positive is around 50% of legitimate
        different country transactions (6% of all transactions)

        legit_diff_country['amount'].describe()
        count       5.000000
        mean     1413.200000
        std      1143.962281
        min       255.000000
        25%       376.000000
        50%      1442.000000
        75%      2012.000000
        max      2981.000000
        Name: amount, dtype: float64

        fraud_diff_country['amount'].describe()
        count       12.000000
        mean      8273.583333
        std       9606.352533
        min        635.000000
        25%       1377.000000
        50%       3801.500000
        75%      13937.250000
        max      29290.000000
        Name: amount, dtype: float64
        */

        // If fraud, return false

        return !(diffCountry && fraudThreshold);
    }
}
