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

package bot.action;

import java.util.ArrayList;

import bot.RiskSystemState;
import bot.checkpoint.AbstractCheck;

/**
 * bot.action.Assessment
 *
 * This class performs the assessment of the record against all the checks and
 * determines if the record is approved or rejected.
 *
 * This assessment is stored and can be gotten from the toString() method
 * in the right format for the game engine.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Assessment {

    private Integer failedCheck;

    /**
     * Used to assess the current record and store the assessment so it can
     * be outputted to the engine later.
     * Firstly stores all the checks the record failed and then stores whether
     * the record should be authorized or rejected.
     * @param checks A list of all checks
     * @param state Current state of the bot
     */
    public void assessRecord(ArrayList<AbstractCheck> checks, RiskSystemState state) {
        this.failedCheck = this.doChecks(checks, state);
    }

    /**
     * This method loops the current record (via state) through
     * all the checks and stores the ID of the check if it does not pass the check.
     * @param checks A list of all checks
     * @param state Current state of the bot
     * @return null if no check failed, the ID of the check that failed otherwise.
     */
    private Integer doChecks(ArrayList<AbstractCheck> checks, RiskSystemState state) {
        for (AbstractCheck check : checks) {
            if (!check.approveRecord(state)) {
                return check.getId();
            }
        }

        return null;
    }

    /**
     * Transforms the assessment to the correct string format
     * @return Assessment as a string
     */
    @Override
    public String toString() {
        if (this.failedCheck == null) {
            return "authorized";
        }

        return String.format("rejected %d", this.failedCheck);
    }
}
