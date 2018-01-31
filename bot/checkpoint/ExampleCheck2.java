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
 * bot.checkpoint.ExampleCheck2
 *
 * Example check 2
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class ExampleCheck2 extends AbstractCheck {

    public ExampleCheck2(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects transaction amounts higher than 100.00";
    }

    @Override
    public boolean approveRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        System.err.println("ExampleCheck2: Checking record " + record.getData("txid"));

        return record.getAmount() <= 10000;  // Doesn't check which currency it is
    }
}
