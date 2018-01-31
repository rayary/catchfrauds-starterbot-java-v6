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
import java.util.Arrays;
import java.util.Scanner;

import bot.action.Assessment;

/**
 * bot.BotParser
 *
 * This class will keep reading output from the game engine.
 * Will either update the bot state or, when an action is requested, will return
 * the result the bot has calculated.
 *
 * @author Jim van Eeden - jim@riddles.io
 */

public class Parser {

    private final Scanner scan;
    private final Bot bot;

    private RiskSystemState currentState;

    public Parser(Bot bot) {
        this.scan = new Scanner(System.in);
        this.bot = bot;
        this.currentState = new RiskSystemState();
    }

    /**
     * Run the parser
     */
    public void run() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            this.parseLine(line);
        }
    }

    /**
     * Parse line gotten from the game engine
     * @param line Current line
     */
    private void parseLine(String line) {

        if (line.length() <= 0) return;

        String[] parts = line.split(" ");

        switch (parts[0]) {
            case "settings": // game settings
                parseSettings(parts[1], parts[2]);
                break;
            case "update": // update about the game
                // record data might be separated by spaces, so combine again
                ArrayList<String> input = new ArrayList<>(Arrays.asList(parts));
                String record = String.join(" ", input.subList(3, input.size()));

                parseUpdates(parts[2], record);
                break;
            case "action": // action requested
                String output = getActionOutput(parts[1], Integer.parseInt(parts[2]));

                System.out.println(output); // send the line to the engine
                break;
            default:
                System.err.println("Unknown input");
        }
    }

    /**
     * Parses all the game settings given by the game engine
     * @param key Type of data given
     * @param value Value
     */
    private void parseSettings(String key, String value) {
        try {
            switch (key) {
                case "timebank":
                    int time = Integer.parseInt(value);
                    this.currentState.setMaxTimebank(time);
                    this.currentState.setTimebank(time);
                    break;
                case "time_per_move":
                    this.currentState.setTimePerMove(Integer.parseInt(value));
                    break;
                case "player_names":
                    // ignore, only 1 player this game
                    break;
                case "your_bot":
                    this.currentState.setMyName(value);
                    break;
                case "max_checkpoints":
                    this.currentState.setMaxCheckpoints(Integer.parseInt(value));
                    break;
                case "record_format":
                    this.currentState.setRecordFormat(value.split(","));
                    break;
                default:
                    System.err.println(
                            String.format("Cannot parse settings input with key '%s'", key));
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse settings value '%s' for key '%s'", value, key));
        }
    }

    /**
     * Parse a record that was sent by the game engine
     * @param value String representation of the record
     */
    private void parseUpdates(String key, String value) {
        switch (key) {
            case "next_record":
                this.currentState.addToRecords(value);
                break;
            default:
                System.err.println("Unknown update input");
        }
    }

    /**
     * Gets the right output from the BotStarter to return to the engine
     * @param key Type of action to perform
     * @param timeout Time in milliseconds to respond
     * @return String representation of the output
     */
    private String getActionOutput(String key, int timeout) {
        switch (key) {
            case "checkpoints":
                return this.bot.checkPointsToString();
            case "record":
                Assessment assessment = this.bot.getAssessment(this.currentState, timeout);
                return assessment.toString();
            default:
                System.err.println("Unknown action input");
                return null;
        }
    }
}