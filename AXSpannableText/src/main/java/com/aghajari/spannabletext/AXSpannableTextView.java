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

import android.content.Context;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;

public class AXSpannableTextView extends androidx.appcompat.widget.AppCompatTextView {

    AXSpannableText spannableText;

    public AXSpannableTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public AXSpannableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AXSpannableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        spannableText = new AXSpannableText();
        setMovementMethod(new AXLinkMovementMethod());
    }

    public void setSpannableText(AXSpannableText spannableText) {
        this.spannableText = spannableText;
    }

    public AXSpannableText getSpannableText() {
        return spannableText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (spannableText == null || TextUtils.isEmpty(text)) {
            super.setText(text, type);
            return;
        }
        SpannableString spannableString = spannableText.create(text);
        super.setText(spannableString, type);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= 16) {
            StaticLayout layout = null;
            Field field = null;
            try {
                Field staticField = DynamicLayout.class.getDeclaredField("sStaticLayout");
                staticField.setAccessible(true);
                layout = (StaticLayout) staticField.get(DynamicLayout.class);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (layout != null) {
                try {
                    field = StaticLayout.class.getDeclaredField("mMaximumVisibleLineCount");
                    field.setAccessible(true);
                    field.setInt(layout, getMaxLines());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (layout != null && field != null) {
                try {
                    field.setInt(layout, Integer.MAX_VALUE);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
