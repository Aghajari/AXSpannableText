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
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;

import com.aghajari.spannabletext.AXGroupRange;
import com.aghajari.spannabletext.AXSpanRange;
import com.aghajari.spannabletext.AXTransparentSpan;
import com.aghajari.spannabletext.OnSpanClickListener;
import com.aghajari.spannabletext.span.AXClickableSpan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AXSpannableStyleType extends AXSpannableType implements AXSpannableTextStyle {

    private boolean clickable = false, longClickable = false;
    OnSpanClickListener clickListener = null;
    AXSpannableTextStyle textStyle = null;

    private List<GroupSpanData> groupSpans;

    // LISTENERS
    public void setOnSpanClickListener(OnSpanClickListener onSpanClickListener) {
        this.clickListener = onSpanClickListener;
    }

    public void setTextStyleListener(AXSpannableTextStyle textStyleListener) {
        this.textStyle = textStyleListener;
    }

    // CLICK | LONG_CLICK
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isClickable() {
        return clickable;
    }

    public boolean isClickable(AXSpanRange range) {
        return isClickable();
    }

    public void setLongClickable(boolean longClickable) {
        this.longClickable = longClickable;
    }

    public boolean isLongClickable() {
        return longClickable;
    }

    public boolean isLongClickable(AXSpanRange range) {
        return isLongClickable();
    }

    public boolean isSelectionEnabled(AXSpanRange range) {
        return isClickable(range) || isLongClickable(range);
    }

    public void onSpanClick(@NonNull View view, AXSpanRange range) {
        if (clickListener != null && isClickable())
            clickListener.onSpanClick(view, range);
    }

    public void onSpanLongClick(@NonNull View view, AXSpanRange range) {
        if (clickListener != null && isLongClickable())
            clickListener.onSpanLongClick(view, range);
    }

    // STYLES

    /**
     * apply textStyle
     */
    @Override
    public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
        if (textStyle!=null) textStyle.applyTextStyle(range,textPaint,isPressed);
    }

    /**
     * load span
     */
    @Override
    public boolean apply(AXSpanRange range, SpannableString spannableString) {
        int startTransparentLength = getStartTransparentLength(range);
        int endTransparentLength = getEndTransparentLength(range);

        spannableString.setSpan(createSpan(range),
                range.getStartPoint() + startTransparentLength,
                range.getEndPoint() - endTransparentLength,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        applyGroupSpans(range,spannableString);

        if (startTransparentLength > 0) {
            spannableString.setSpan(createTransparentSpan(),
                    range.getStartPoint(),
                    range.getStartPoint() + startTransparentLength,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (endTransparentLength > 0) {
            spannableString.setSpan(createTransparentSpan(),
                    range.getEndPoint() - endTransparentLength,
                    range.getEndPoint(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return true;
    }

    /**
     * create main span
     */
    protected Object createSpan(AXSpanRange range) {
        return new AXClickableSpan(this, range) {};
    }

    protected Object createTransparentSpan(){
        return new AXTransparentSpan();
    }

    /**
     * remove characters from beginning of the span
     */
    protected int getStartTransparentLength(AXSpanRange range) {
        return 0;
    }

    /**
     * remove characters from the end of the span
     */
    protected int getEndTransparentLength(AXSpanRange range) {
        return 0;
    }

    @Override
    public String getShowingValue(AXSpanRange range) {
        String res = super.getShowingValue(range);
        try {
            res = res.substring(getStartTransparentLength(range),res.length() - getEndTransparentLength(range));
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    protected void applyGroupSpans (AXSpanRange range, SpannableString spannableString) {
        if (groupSpans == null) return;
        try {
            for (GroupSpanData data : groupSpans) {
                if (data.span != null
                        && range.getGroups().size() > data.groupIndex
                        && range.getGroups().get(data.groupIndex).getValue() != null
                        && isInArea(range, range.getGroups().get(data.groupIndex))) {
                    AXGroupRange groupRange = range.getGroups().get(data.groupIndex);
                    spannableString.setSpan(data.span,
                            groupRange.getStartPoint(),
                            groupRange.getEndPoint(),
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // GROUPS STYLES DATA

    public void addGroupSpan(int groupNumber,Object span){
        if (groupSpans == null) groupSpans = new ArrayList<>();
        groupSpans.add(new GroupSpanData(groupNumber, span));
    }

    public void removeGroupSpans(int groupNumber){
        if (groupSpans == null || groupSpans.size()==0) return;
        int index = groupNumber - 1;
        Iterator<GroupSpanData> i = groupSpans.iterator();
        while (i.hasNext()) {
            GroupSpanData data = i.next();
            if (data.groupIndex == index)
                i.remove();
        }
    }

    public void clearGroupSpans(){
        groupSpans.clear();
    }

    public void removeGroupSpanAt(int index){
        groupSpans.remove(index);
    }

    private static class GroupSpanData {
        Object span;
        int groupIndex;

        GroupSpanData (int groupNumber,Object span){
            this.span = span;
            this.groupIndex = groupNumber - 1;
        }
    }

    private boolean isInArea(AXSpanRange range,AXGroupRange groupRange) {
        if ((range.getStartPoint() <= groupRange.getStartPoint() && range.getEndPoint() >= groupRange.getStartPoint())
                || (range.getStartPoint() <= groupRange.getEndPoint() && range.getEndPoint() >= groupRange.getEndPoint())) {
            return true;
        }
        return false;
    }
}
