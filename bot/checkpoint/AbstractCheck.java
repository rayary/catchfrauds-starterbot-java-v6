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

/**
 * bot.checkpoint.AbstractCheck
 *
 * All checks created must extend this class and implement at least the
 * abstract methods.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public abstract class AbstractCheck {

    private int id;

    public AbstractCheck(int id) {
        this.id = id;
    }

    /**
     * Returns a description of this check, that will be displayed in the
     * game visualizer. The description should be short, but clear.
     * @return Description of this check point
     */
    public abstract String getDescription();

    /**
     * Returns whether the current record (state.getCurrentRecord()) passes
     * or fails this check. Should be implemented to do what is described
     * in the getDescription() method.
     * @param state Current bot state
     * @return True if the check is passed, false otherwise
     */
    public abstract boolean approveRecord(RiskSystemState state);

    /**
     * Gets the records id, used to output to the game engine.
     * @return The record id
     */
    public int getId() {
        return this.id;
    }
}
