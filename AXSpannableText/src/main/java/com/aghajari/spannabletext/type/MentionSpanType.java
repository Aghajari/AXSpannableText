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

import java.util.regex.Pattern;

/**
 * This is called an “@mention”
 */
public class MentionSpanType extends AXSpannableStyleType {

    public final static Pattern MENTION_PATTERN =
            Pattern.compile("\\B(\\@)([\\w\\-]+)");

    public MentionSpanType() {
        setClickable(true);
        init();
    }

    @Override
    public Pattern getRegexPattern() {
        return MENTION_PATTERN;
    }

}
