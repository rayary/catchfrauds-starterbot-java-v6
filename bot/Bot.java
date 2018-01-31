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

package bot;

import bot.action.Assessment;
import bot.checkpoint.AbstractCheck;
import bot.checkpoint.Check2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * bot.BotStarter
 *
 * Main class. Checkpoints should be initialized here.
 * Also the assessment is created and returned here.
 * 
 * @author Jim van Eeden - jim@riddles.io
 */

public class Bot {

    private ArrayList<AbstractCheck> checks;

    public Bot() {
        this.checks = new ArrayList<>();

        // TODO: Change these example checks for useful ones and add more checks
        //this.checks.add(new Check1(0)); // ID needs to start with 0
        this.checks.add(new Check2(0));

    }

    /**
     * Creates a new Assessment that is then used to assess the current
     * record and then can be returned to the game engine.
     * @param state The current bot state
     * @param timeout Time in ms the bot has to respond before a time out
     * @return Assessment of the current record
     */
    public Assessment getAssessment(RiskSystemState state, int timeout) {
        state.setTimebank(timeout);

        Assessment assessment = new Assessment();
        assessment.assessRecord(this.checks, state);

        state.storeAssessment(assessment);
        
        return assessment;
    }

    /**
     * Gets the description of all the checks and transforms them
     * to the correct output for the game engine.
     * @return A string that the game engine can use to set the checkpoint descriptions
     */
    public String checkPointsToString() {
        return this.checks.stream()
                .sorted(Comparator.comparingInt(AbstractCheck::getId))
                .map(AbstractCheck::getDescription)
                .collect(Collectors.joining(";"));
    }

    /**
     * Main method
     * @param args Ignored
     */
    public static void main(String[] args) {
        Parser parser = new Parser(new Bot());
        parser.run();
    }
 }
