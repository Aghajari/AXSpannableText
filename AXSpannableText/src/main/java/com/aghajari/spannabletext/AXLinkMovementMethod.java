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

import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import com.aghajari.spannabletext.span.AXClickableSpan;

public class AXLinkMovementMethod extends LinkMovementMethod {

    private AXClickableSpan pressedSpan;
    private Handler mLongClickHandler;
    private static long LONG_CLICK_TIME = 500L;

    public AXLinkMovementMethod() {
        mLongClickHandler = new Handler();
    }

    @Override
    public boolean onTouchEvent(final TextView textView, final Spannable spannable, MotionEvent event) {
        int action = event.getAction();

        // PRESS CLICKABLE SPAN
        if (action == MotionEvent.ACTION_DOWN) {
            pressedSpan = getPressedSpan(textView, spannable, event);
            if (pressedSpan != null) {
                pressedSpan.setPressed(true);
                if (pressedSpan.isSelectionEnabled())
                    Selection.setSelection(spannable, spannable.getSpanStart(pressedSpan),
                            spannable.getSpanEnd(pressedSpan));
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            AXClickableSpan touchedSpan = getPressedSpan(textView, spannable, event);
            if (pressedSpan != null && touchedSpan != pressedSpan) {
                pressedSpan.setPressed(false);
                pressedSpan = null;
                Selection.removeSelection(spannable);
            }
        } else {
            if (pressedSpan != null) {
                pressedSpan.setPressed(false);
                super.onTouchEvent(textView, spannable, event);
            }
            pressedSpan = null;
            Selection.removeSelection(spannable);
        }


        // LONG CLICK
        if (action == MotionEvent.ACTION_CANCEL) {
            if (mLongClickHandler != null) {
                mLongClickHandler.removeCallbacksAndMessages(null);
            }
        }
        if (pressedSpan != null && pressedSpan.isLongClickable() && (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN)) {

            if (action == MotionEvent.ACTION_UP) {
                if (mLongClickHandler != null) {
                    mLongClickHandler.removeCallbacksAndMessages(null);
                }
            } else {
                mLongClickHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pressedSpan != null && pressedSpan.isLongClickable()) {
                            pressedSpan.onLongClick(textView);
                        }
                    }
                }, LONG_CLICK_TIME);
            }
        }

        return true;
    }

    private AXClickableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int verticalLine = layout.getLineForVertical(y);
        int horizontalOffset = layout.getOffsetForHorizontal(verticalLine, x);

        AXClickableSpan[] link = spannable.getSpans(horizontalOffset, horizontalOffset, AXClickableSpan.class);
        AXClickableSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }
}