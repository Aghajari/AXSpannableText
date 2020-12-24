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

import android.text.SpannableString;

import androidx.annotation.NonNull;

import com.aghajari.spannabletext.span.AXClickableSpan;
import com.aghajari.spannabletext.type.AXSpannableStyleType;
import com.aghajari.spannabletext.type.AXSpannableType;
import com.aghajari.spannabletext.type.MarkdownMonospaceSpanType;
import com.aghajari.spannabletext.type.MarkdownStyleSpanType;
import com.aghajari.spannabletext.type.StrikethroughSpanType;
import com.aghajari.spannabletext.type.UnderlineSpanType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Amir Hossein Aghajari
 * @version 1.0.0
 */
public class AXSpannableText {

    private boolean useOnlyOneSpanInRange = true;

    protected List<AXSpannableType> supportedTypes;
    protected List<Class<? extends AXSpannableType>> supportedMultiSpanTypes;

    public AXSpannableText(){
        supportedTypes = new ArrayList<>();
        supportedMultiSpanTypes = new ArrayList<>();
        supportedMultiSpanTypes.add(MarkdownStyleSpanType.class);
        supportedMultiSpanTypes.add(StrikethroughSpanType.class);
        supportedMultiSpanTypes.add(UnderlineSpanType.class);
    }

    /**
     * Add new multi span type support to AXSpannableText
     */
    public void addMultiSpanTypeSupport (Class<? extends AXSpannableType> classOfType){
        if (supportedMultiSpanTypes.contains(classOfType)) return;
        supportedMultiSpanTypes.add(classOfType);
    }

    /**
     * remove multi span type support to AXSpannableText
     */
    public void removeMultiSpanTypeSupport (Class<? extends AXSpannableType> classOfType){
        supportedMultiSpanTypes.remove(classOfType);
    }

    /**
     * clear all supported multi span types
     */
    public void clearSupportedMultiSpanTypes (){
        supportedMultiSpanTypes.clear();
    }

    /**
     * @return all AXSpannableText supported multi span types
     */
    public List<Class<? extends AXSpannableType>> getSupportedMultiSpanTypes(){
        return supportedMultiSpanTypes;
    }

    /**
     * Add new type support to AXSpannableText
     */
    public void addType(AXSpannableType type) {
        supportedTypes.add(type);
    }

    /**
     * Add new types support to AXSpannableText
     */
    public void addTypes(AXSpannableType... types) {
        supportedTypes.addAll(Arrays.asList(types));
    }

    /**
     * remove type support to AXSpannableText
     */
    public void removeType(AXSpannableType type) {
        supportedTypes.remove(type);
    }

    /**
     * remove type support to AXSpannableText
     */
    public void removeType(Class<?> classOfType) {
        Iterator<AXSpannableType> i = supportedTypes.iterator();
        while (i.hasNext()) {
            AXSpannableType type = i.next();
            if (type.getClass().equals(classOfType))
                i.remove();
        }
    }

    /**
     * @return all AXSpannableText supported types
     */
    public List<AXSpannableType> getTypes() {
        return supportedTypes;
    }

    /**
     * clear all supported span types
     */
    public void clearSupportedTypes (){
        supportedTypes.clear();
    }

    /**
     * each AXSpanRange will only use one span
     */
    public void setUseOnlyOneSpanInRange(boolean useOnlyOneSpanInRange) {
        this.useOnlyOneSpanInRange = useOnlyOneSpanInRange;
    }

    public SpannableString create(CharSequence text) {
        return create(SpannableString.valueOf(text));
    }

    public SpannableString create(SpannableString spannableString) {

        List<AXSpanRange> ranges = findRanges(spannableString);
        List<SpanArea> areas = new ArrayList<>();

        for (final AXSpanRange range : ranges) {
            if (!canUseArea(areas, range.getStartPoint(), range.getEndPoint())
                    && !supportsMultiSpan(range,spannableString)) continue;

            if (range.getType().apply(range, spannableString))
                areas.add(new SpanArea(range));
        }

        return spannableString;
    }

    public List<AXSpanRange> findRanges(CharSequence text) {
        List<AXSpanRange> ranges = new LinkedList<>();

        for (AXSpannableType type : supportedTypes) {
            Pattern pattern = type.getRegexPattern();
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                AXSpanRange range = new AXSpanRange(matcher.start(), matcher.end(), matcher.group(), type);

                if (matcher.groupCount() > 0) {
                    ArrayList<AXGroupRange> groups = new ArrayList<>();
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        groups.add(new AXGroupRange(matcher.group(i),matcher.start(i),matcher.end(i)));
                    }
                    range.groups = groups;
                }

                if (type.isValidRange(range))
                    ranges.add(range);
            }
        }

        Collections.sort(ranges);
        return ranges;
    }

    protected class SpanArea {
        int start;
        int end;

        SpanArea(AXSpanRange range) {
            this(range.getStartPoint(), range.getEndPoint());
        }

        SpanArea(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    protected boolean canUseArea(List<SpanArea> areas, int start, int end) {
        if (!useOnlyOneSpanInRange) return true;
        if (areas == null || areas.isEmpty()) return true;
        for (final SpanArea area : areas) {
            if ((area.start <= start && area.end >= start) || (area.start <= end && area.end >= end)) {
                return false;
            }
        }
        return true;
    }

    protected boolean canUseArea(int s1,int e1, int start, int end) {
        return (s1 > start || e1 < start) && (s1 > end || e1 < end);
    }

    protected boolean supportsMultiSpan(AXSpanRange range,SpannableString spannableString) {
        if (supportedMultiSpanTypes == null || supportedMultiSpanTypes.size() == 0) return false;
        boolean can = supportedMultiSpanTypes.contains(range.getType().getClass()) ||
                supportedMultiSpanTypes.contains(findRealClass(range.getType().getClass()));
        return can && !hasMonospaceSpan(range, spannableString);
    }

    public boolean hasMonospaceSpan (AXSpanRange range,SpannableString spannableString){
        int next;
        for (int i = 0; i < spannableString.length(); i = next) {
            next = spannableString.nextSpanTransition(i, spannableString.length(), AXClickableSpan.class);

            AXClickableSpan[] spans = spannableString.getSpans(i, next, AXClickableSpan.class);
            for (AXClickableSpan span : spans) {
                if (span.getSpanRange().getType() instanceof MarkdownMonospaceSpanType) {
                    if (!canUseArea(span.getSpanRange().getStartPoint()
                            , span.getSpanRange().getEndPoint()
                            , range.getStartPoint()
                            , range.getEndPoint())) return true;
                }
            }
        }
        return false;
    }

    private static Class<? extends AXSpannableType> findRealClass(@NonNull Class<? extends AXSpannableType> c){
        try {
            Class<?> cl = c;
            while (cl.getSuperclass()!=null
                    && !cl.getSuperclass().equals(AXSpannableStyleType.class)
                    && !cl.getSuperclass().equals(AXSpannableType.class)){
                cl = cl.getSuperclass();
            }
            return (Class<? extends AXSpannableType>) cl;
        }catch (Exception e){
            return c;
        }
    }
}
