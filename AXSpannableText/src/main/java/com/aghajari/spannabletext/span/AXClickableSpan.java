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

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.aghajari.spannabletext.AXSpanRange;
import com.aghajari.spannabletext.type.AXSpannableStyleType;

public abstract class AXClickableSpan extends ClickableSpan {

    protected boolean isPressed;
    protected AXSpannableStyleType style;
    protected AXSpanRange range;

    public AXClickableSpan(AXSpannableStyleType spannableStyleType, AXSpanRange range) {
        this.style = spannableStyleType;
        this.range = range;
    }

    public boolean isSelectionEnabled() {
        return style.isSelectionEnabled(range);
    }

    public boolean isLongClickable() {
        return style.isLongClickable(range);
    }

    public AXSpanRange getSpanRange() {
        return range;
    }

    boolean longPressed = false;

    @Override
    public void onClick(@NonNull View view) {
        if (!longPressed) style.onSpanClick(view, range);
        longPressed = false;
    }

    public void onLongClick(@NonNull View view) {
        style.onSpanLongClick(view, range);
        longPressed = true;
    }

    public void setPressed(boolean isSelected) {
        if (isSelected) longPressed = false;
        isPressed = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
        textPaint.bgColor = Color.TRANSPARENT;
        style.applyTextStyle(range, textPaint, isPressed);
    }
}