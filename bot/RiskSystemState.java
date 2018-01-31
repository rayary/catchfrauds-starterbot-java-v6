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

import java.util.ArrayList;

import bot.action.Assessment;
import bot.data.PaymentRecord;

/**
 * bot.RiskSystemState
 * 
 * Stores all information about the current bot state, such as the bot's timebank,
 * the bot's name, records gotten so far and assessments made so far (and more).
 *
 * This class can be editted to store even more data.
 * 
 * @author Jim van Eeden - jim@riddles.io
 */

public class RiskSystemState {

    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int MAX_CHECKPOINTS;

    private String[] recordFormat;
    private ArrayList<PaymentRecord> records;
    private ArrayList<Assessment> assessments;
    private int timebank;
    private String myName;
    
    public RiskSystemState() {
        this.records = new ArrayList<>();
        this.assessments = new ArrayList<>();
    }

    /**
     * Set the time left in the bot's time bank, i.e. time left before
     * the engine will determine the bot has timed out.
     * @param value Time left in ms
     */
    public void setTimebank(int value) {
        this.timebank = value;
    }

    /**
     * Add an assessment to the list of assessments made so far.
     * @param assessment Assessment
     */
    public void storeAssessment(Assessment assessment) {
        this.assessments.add(assessment);
    }

    /**
     * Get the current timebank.
     * @return Current timebank
     */
    public int getTimebank() {
        return this.timebank;
    }

    /**
     * Get the bot's name as determined by the game engine.
     * @return Bot's given name
     */
    public String getMyName() {
        return this.myName;
    }

    /**
     * Get the maximum amount of check points the bot can have.
     * @return Maximum amount of check points
     */
    public int getMaxCheckPoints() {
        return this.MAX_CHECKPOINTS;
    }

    /**
     * Get the record that was last given by the game engine.
     * @return Current record
     */
    public PaymentRecord getCurrentRecord() {
        return this.records.get(this.records.size() - 1);
    }

    /**
     * Get all the records the bot has received so far.
     * @return A list of all records
     */
    public ArrayList<PaymentRecord> getRecords() {
        return this.records;
    }

    /**
     * Get all the assessments made so far.
     * @return A list of all assessments
     */
    public ArrayList<Assessment> getAssessments() {
        return this.assessments;
    }

    /**
     * Sets the maximum timebank as given by the settings.
     * @param maxTimebank Maximum timebank
     */
    public void setMaxTimebank(int maxTimebank) {
        MAX_TIMEBANK = maxTimebank;
    }

    /**
     * Sets the time per move as given by the settings.
     * @param timePerMove Time per move
     */
    public void setTimePerMove(int timePerMove) {
        TIME_PER_MOVE = timePerMove;
    }

    /**
     * Sets the maximum amount of checkpoints available as given
     * by the settings.
     * @param maxCheckpoints The maximum amount of checkpoints
     */
    public void setMaxCheckpoints(int maxCheckpoints) {
        MAX_CHECKPOINTS = maxCheckpoints;
    }

    /**
     * Sets the name given to this bot as far as the engine
     * is concerned (player0 for this game).
     * @param myName My name
     */
    public void setMyName(String myName) {
        this.myName = myName;
    }

    /**
     * Sets the record format, i.e. the headers of the columns
     * in the records table.
     * @param recordFormat The record format.
     */
    public void setRecordFormat(String[] recordFormat) {
        this.recordFormat = recordFormat;
    }

    public void addToRecords(String input) {
        try {
            this.records.add(new PaymentRecord(this.recordFormat, input));
        } catch (InstantiationError e) {
            System.err.println(String.format("Cannot parse record '%s'", input));
        }
    }
}