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


package com.aghajari.spannabletext.type;

import android.text.SpannableString;

import com.aghajari.spannabletext.AXSpanRange;

import java.util.regex.Pattern;

public abstract class AXSpannableType {

    public AXSpannableType(){
        init();
    }

    /**
     * @return span type regex pattern
     */
    public abstract Pattern getRegexPattern();

    /**
     * apply span style
     */
    public abstract boolean apply(AXSpanRange range, SpannableString spannableString);

    /**
     * @return true if the range is valid for this type of span
     */
    public boolean isValidRange(AXSpanRange range) {
        return true;
    }

    /**
     * initialize span
     */
    protected void init(){}

    /**
     * returns matched text value
     */
    public String getShowingValue(AXSpanRange range){
        return range.getMatchedText();
    }
}
