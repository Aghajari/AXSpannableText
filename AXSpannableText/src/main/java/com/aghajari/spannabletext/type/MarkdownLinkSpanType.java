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

import java.util.regex.Pattern;

/**
 * Markdown Link Span
 * [title](src)
 */
public class MarkdownLinkSpanType extends AXSpannableStyleType {

    public final static Pattern MARKDOWN_LINK_PATTERN =
            Pattern.compile("(\\[])|\\[(.*?)\\]\\((.*?)\\)");

    public MarkdownLinkSpanType() {
        setClickable(true);
        init();
    }

    @Override
    public Pattern getRegexPattern() {
        return MARKDOWN_LINK_PATTERN;
    }

    @Override
    protected int getStartTransparentLength(AXSpanRange range) {
        return 1;
    }

    @Override
    protected int getEndTransparentLength(AXSpanRange range) {
        if (isValidRange(range)) {
            return range.getGroups().get(2).getValue().length() + 3;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isValidRange(AXSpanRange range) {
        if (range.getGroups() == null || range.getGroups().size() != 3) return false;
        return super.isValidRange(range);
    }

    public String getLinkValue (AXSpanRange range){
        return range.getGroups().get(2).getValue();
    }
}
