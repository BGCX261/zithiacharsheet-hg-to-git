/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mcherm.zithiacharsheet.client.model;



public interface StatCost {
    /**
     * Returns the cost to raise the stat that many points, or
     * throws an exception if its in the range where raising
     * it just doesn't make sense.
     * 
     * @param pointsRaised the number of points the stat has
     *   been raised from its base value
     * @return the cost to raise it
     */
    public int getCost(int pointsRaised);


    static class NormalStatCost implements StatCost {
        public int getCost(int pointsRaised) {
            int pseudoStat = 10 + pointsRaised;
            if (pseudoStat >= 10) {
                int div = pseudoStat / 5;
                int mod = pseudoStat % 5;
                int result = (((div * (div - 1)) / 2) * 5) + (div * mod) - 5;
                return result;
            } else if (pseudoStat >= 1) {
                switch(pseudoStat) {
                case 1: return -34;
                case 2: return -28;
                case 3: return -23;
                case 4: return -18;
                case 5: return -14;
                case 6: return -10;
                case 7: return -7;
                case 8: return -4;
                case 9: return -2;
                default: throw new RuntimeException("Code should not reach here.");
                }
            } else {
                throw new RuntimeException("Stat out of range where cost can be computed.");
            }
        }
    }
    
    static class DexStatCost extends NormalStatCost {
        @Override
        public int getCost(int pointsRaised) {
            return super.getCost(pointsRaised) * 2;
        }
    }
    
    static class HpStunStatCost extends NormalStatCost {
        @Override
        public int getCost(int pointsRaised) {
            if (pointsRaised >= 0) {
                return pointsRaised * 2;
            } else {
                return super.getCost(pointsRaised);
            }
        }
    }
    
    static class MoveStatCost extends NormalStatCost {
        @Override
        public int getCost(int pointsRaised) {
            return (int) Math.floor(super.getCost(pointsRaised) / 2.0);
        }
    }
    
    static class SpdStatCost implements StatCost {
        public int getCost(int pointsRaised) {
            int pseudoStat = 6 + pointsRaised;
            switch (pseudoStat) {
            case 9: return -21;
            case 8: return -15;
            case 7: return -8;
            case 6: return 0;
            case 5: return 8;
            case 4: return 20;
            case 3: return 36;
            case 2: return 60;
            case 1: return 108;
            case 0: return 250;
            default: throw new RuntimeException(
                    "Stat out of range where cost can be computed (" + pointsRaised + ").");
            }
        }
    }
    
}
