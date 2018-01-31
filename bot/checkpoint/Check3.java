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

import java.util.HashMap;

/**
 * bot.checkpoint.ExampleCheck1
 *
 * Example check
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Check3 extends AbstractCheck {

    private HashMap<String, String> timeRecord = new HashMap<>();

    public Check3(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects repeated transaction by same card";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        System.err.println("Check2: Checking record " + record.getData("txid"));

        //# reject transaction by different sanitized emails for same card number
        //# reject repeated large transactions
        //# reject card by different same email same shopper country but more than 3 issuer_country


        // create map with card number as key, transaction time as values
        // if record does not exist. add to map and allow to pass
        // if record exists, add time to list and check the time, if last transaction was 3 min ago. reject transaction



        // If fraud, return false

        return true;
    }
}
