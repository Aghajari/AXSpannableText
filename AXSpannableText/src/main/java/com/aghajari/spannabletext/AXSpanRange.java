/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.spannabletext;

import com.aghajari.spannabletext.type.AXSpannableType;

import java.util.List;

public class AXSpanRange implements Comparable<AXSpanRange>{

    private final AXSpannableType type;
    private final String matchedText;
    private final int startPoint,endPoint;
    List<AXGroupRange> groups;

    public AXSpanRange(int startPoint, int endPoint, String matchedText, AXSpannableType type) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.matchedText = matchedText;
        this.type = type;
    }

    /**
     * @return span type
     */
    public AXSpannableType getType() {
        return type;
    }

    public String getMatchedText() {
        return matchedText;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    /**
     * @return regex group values
     */
    public List<AXGroupRange> getGroups() {
        return groups;
    }

    @Override
    public int compareTo(AXSpanRange compare) {
        return this.startPoint-compare.startPoint;
    }

    @Override
    public String toString() {
        return "AXSpanRange{" +
                "type=" + type +
                ", matchedText='" + matchedText + '\'' +
                '}';
    }
}