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

import com.aghajari.spannabletext.AXSpanRange;
import com.aghajari.spannabletext.span.AXStyleSpan;

import java.util.regex.Pattern;

/**
 * Markdown style SpanType
 * *Italic*
 * **Bold**
 * ***Bold & Italic***
 */
public class MarkdownStyleSpanType extends AXSpannableStyleType {

    private boolean supportsBold = true;
    private boolean supportsItalic = true;
    private boolean supportsBoldItalic = true;

    public final int STYLE_NORMAL = Typeface.NORMAL;
    public final int STYLE_ITALIC = Typeface.ITALIC;
    public final int STYLE_BOLD = Typeface.BOLD;
    public final int STYLE_BOLD_ITALIC = Typeface.BOLD_ITALIC;

    public final static Pattern MARKDOWN_STYLE_PATTERN =
            Pattern.compile("\\*(\\s*[^*\\s][^*]*)\\*|\\*{2}(\\s*[^*\\s][^*]*)\\*{2}|\\*{3}(\\s*[^*\\s][^*]*)\\*{3}");

    public MarkdownStyleSpanType(){
        setClickable(false);
        setLongClickable(false);
        init();
    }

    @Override
    public Pattern getRegexPattern() {
        return MARKDOWN_STYLE_PATTERN;
    }

    @Override
    protected Object createSpan(AXSpanRange range) {
        return new AXStyleSpan(getStyleType(range),this,range);
    }

    @Override
    protected int getStartTransparentLength(AXSpanRange range) {
        return getTransparentLength(range);
    }

    @Override
    protected int getEndTransparentLength(AXSpanRange range) {
        return getTransparentLength(range);
    }

    @Override
    public boolean isValidRange(AXSpanRange range) {
        if (range.getGroups() == null || range.getGroups().size() == 0) return false;
        switch (getStyleType(range)) {
            case STYLE_ITALIC:
                return isItalicEnabled();
            case STYLE_BOLD:
                return isBoldEnabled();
            case STYLE_BOLD_ITALIC:
                return isBoldItalicEnabled();
        }
        return false;
    }

    private int getTransparentLength(AXSpanRange range){
        switch (getStyleType(range)){
            case STYLE_ITALIC:
                return 1;
            case STYLE_BOLD:
                return 2;
            case STYLE_BOLD_ITALIC:
                return 3;
            default:
                return 0;
        }
    }

    public int getStyleType(AXSpanRange range) {
        if (range.getType() instanceof MarkdownStyleSpanType) {
            if (range.getGroups().get(0).getValue() != null) {
                return STYLE_ITALIC;
            } else if (range.getGroups().get(1).getValue() != null) {
                return STYLE_BOLD;
            } else if (range.getGroups().get(2).getValue() != null) {
                return STYLE_BOLD_ITALIC;
            }
        }
        return STYLE_NORMAL;
    }

    public boolean isBoldEnabled() {
        return supportsBold;
    }

    public boolean isItalicEnabled() {
        return supportsItalic;
    }

    public boolean isBoldItalicEnabled() {
        return supportsBoldItalic;
    }

    public void setBoldEnabled(boolean supportsBold) {
        this.supportsBold = supportsBold;
    }

    public void setBoldItalicEnabled(boolean supportsBoldItalic) {
        this.supportsBoldItalic = supportsBoldItalic;
    }

    public void setItalicEnabled(boolean supportsItalic) {
        this.supportsItalic = supportsItalic;
    }
}
