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

import android.graphics.Typeface;
import android.text.TextPaint;

import com.aghajari.spannabletext.AXSpanRange;

import java.util.regex.Pattern;

/**
 * Markdown Monospace SpanType
 * `CODE`
 */
public class MarkdownMonospaceSpanType extends AXSpannableStyleType {

    public final static Pattern MARKDOWN_MONOSPACE_PATTERN =
            Pattern.compile("\\`(\\s*[^`\\s][^`]*)\\`");

    @Override
    public Pattern getRegexPattern() {
        return MARKDOWN_MONOSPACE_PATTERN;
    }

    @Override
    protected int getStartTransparentLength(AXSpanRange range) {
        return 1;
    }

    @Override
    protected int getEndTransparentLength(AXSpanRange range) {
        return 1;
    }

    @Override
    public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
        super.applyTextStyle(range, textPaint, isPressed);
        textPaint.setTypeface(Typeface.MONOSPACE);
    }
}
