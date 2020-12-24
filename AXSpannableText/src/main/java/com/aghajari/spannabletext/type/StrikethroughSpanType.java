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

import com.aghajari.spannabletext.AXSpanRange;
import com.aghajari.spannabletext.span.AXStrikethroughSpan;

import java.util.regex.Pattern;

/**
 * Any word wrapped with two tildes (like ~~this~~) will appear crossed out.
 */
public class StrikethroughSpanType extends AXSpannableStyleType {

    public final static Pattern STRIKE_THROUGH_PATTERN =
            Pattern.compile("\\~{2}(\\s*[^~\\s][^~]*)\\~{2}");

    public StrikethroughSpanType() {
        setClickable(false);
        setLongClickable(false);
        init();
    }

    @Override
    public Pattern getRegexPattern() {
        return STRIKE_THROUGH_PATTERN;
    }

    @Override
    protected Object createSpan(AXSpanRange range) {
        return new AXStrikethroughSpan(this,range);
    }

    @Override
    protected int getStartTransparentLength(AXSpanRange range) {
        return 2;
    }

    @Override
    protected int getEndTransparentLength(AXSpanRange range) {
        return 2;
    }
}
