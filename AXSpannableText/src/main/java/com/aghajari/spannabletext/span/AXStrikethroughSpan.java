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


package com.aghajari.spannabletext.span;

import android.text.TextPaint;
import android.text.style.StrikethroughSpan;

import com.aghajari.spannabletext.AXSpanRange;
import com.aghajari.spannabletext.type.AXSpannableStyleType;

public class AXStrikethroughSpan extends StrikethroughSpan {

    protected AXSpannableStyleType style;
    protected AXSpanRange range;

    public AXStrikethroughSpan(AXSpannableStyleType spannableStyleType, AXSpanRange range) {
        super();
        this.style = spannableStyleType;
        this.range = range;
    }

    public AXSpanRange getSpanRange() {
        return range;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        style.applyTextStyle(range, ds, false);
    }
}
