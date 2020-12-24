package com.aghajari.app.spannabletext;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aghajari.spannabletext.AXSpanRange;
import com.aghajari.spannabletext.AXSpannableText;
import com.aghajari.spannabletext.type.AXSpannableStyleType;
import com.aghajari.spannabletext.type.EmailSpanType;
import com.aghajari.spannabletext.type.HashtagSpanType;
import com.aghajari.spannabletext.type.MarkdownLinkSpanType;
import com.aghajari.spannabletext.type.MarkdownMonospaceSpanType;
import com.aghajari.spannabletext.type.MarkdownMultilineMonospaceSpanType;
import com.aghajari.spannabletext.type.MarkdownStyleSpanType;
import com.aghajari.spannabletext.type.MentionSpanType;
import com.aghajari.spannabletext.type.PhoneSpanType;
import com.aghajari.spannabletext.type.StrikethroughSpanType;
import com.aghajari.spannabletext.type.UnderlineSpanType;
import com.aghajari.spannabletext.type.UrlSpanType;

import java.util.regex.Pattern;

public class MySpannableText {

    private static AXSpannableText spannableText = null;

    public static SpannableString create (CharSequence text){
        init();
        return spannableText.create(text);
    }

    private static void init(){
        if (spannableText!=null) return;

        spannableText = new AXSpannableText();

        //Mention
        spannableText.addType(new MentionSpanType() {
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range,textPaint,isPressed);
                textPaint.setColor(Color.BLUE);
            }
        });

        //Markdown Link
        spannableText.addType(new MarkdownLinkSpanType(){
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range,textPaint,isPressed);
                textPaint.setColor(Color.BLUE);
                textPaint.setUnderlineText(true);
            }

            @Override
            public void onSpanClick(@NonNull View view, AXSpanRange range) {
                super.onSpanClick(view, range);
                Toast.makeText(view.getContext(),getLinkValue(range),Toast.LENGTH_SHORT).show();
            }
        });

        // Hashtag
        spannableText.addType(new HashtagSpanType(){
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range,textPaint,isPressed);
                textPaint.setColor(Color.RED);
            }
        });

        // Url
        spannableText.addType(new UrlSpanType(){

            @Override
            protected void init() {
                super.init();
                setLongClickable(true);
            }

            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range,textPaint,isPressed);
                textPaint.setColor(Color.BLUE);
                textPaint.setUnderlineText(true);
            }

            @Override
            public void onSpanClick(@NonNull View view, AXSpanRange range) {
                super.onSpanClick(view, range);
                Toast.makeText(view.getContext(),range.getMatchedText(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSpanLongClick(@NonNull View view, AXSpanRange range) {
                super.onSpanLongClick(view, range);
                Toast.makeText(view.getContext(),"LongClicked : "+range.getMatchedText(),Toast.LENGTH_SHORT).show();
            }
        });

        //Markdown style (bold-italic-bold_italic)
        spannableText.addType(new MarkdownStyleSpanType());
        //Strikethrough style
        spannableText.addType(new StrikethroughSpanType());
        //Underline style
        spannableText.addType(new UnderlineSpanType());

        // Markdown multiline code
        spannableText.addType(new MarkdownMultilineMonospaceSpanType() {
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range, textPaint, isPressed);
                textPaint.bgColor = Color.LTGRAY;
                textPaint.setColor(Color.DKGRAY);
            }
        });

        // Markdown code
        spannableText.addType(new MarkdownMonospaceSpanType(){
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range, textPaint, isPressed);
                textPaint.bgColor = Color.LTGRAY;
                textPaint.setColor(Color.DKGRAY);
            }
        });

        // Phone
        spannableText.addType(new PhoneSpanType() {
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range, textPaint, isPressed);
                textPaint.setColor(Color.BLUE);
            }
        });

        // Email
        spannableText.addType(new EmailSpanType(){
            @Override
            public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
                super.applyTextStyle(range, textPaint, isPressed);
                textPaint.setColor(Color.MAGENTA);
            }
        });

        // CUSTOM SPAN
        spannableText.addType(new CustomSpan());
    }

    // CustomSpan
    // [INFO|ID] -> INFO|ID
    public static class CustomSpan extends AXSpannableStyleType {

        public CustomSpan(){
            super();

            // INFO : ForegroundColorSpan(RED)
            addGroupSpan(1,new ForegroundColorSpan(Color.RED));
            // ID : ForegroundColorSpan(BLUE)
            addGroupSpan(2,new ForegroundColorSpan(Color.BLUE));

            // ClickableSpan
            setClickable(true);
        }

        @Override
        public Pattern getRegexPattern() {
            return Pattern.compile("\\[(\\s*[^*]*)\\|(\\s*[^*]*)\\]");
        }

        @Override
        public void applyTextStyle(AXSpanRange range, TextPaint textPaint, boolean isPressed) {
            super.applyTextStyle(range, textPaint, isPressed);
            textPaint.setColor(Color.DKGRAY);
            textPaint.setUnderlineText(isPressed);
        }

        @Override
        public boolean isValidRange(AXSpanRange range) {
            // check if INFO & ID exists
            if (range.getGroups() == null || range.getGroups().size()!=2) return false;
            return super.isValidRange(range);
        }

        @Override
        protected int getStartTransparentLength(AXSpanRange range) {
            return 1; // remove [ at the start
        }

        @Override
        protected int getEndTransparentLength(AXSpanRange range) {
            return 1; // remove ] at the end
        }

        @Override
        public void onSpanClick(@NonNull View view, AXSpanRange range) {
            super.onSpanClick(view, range);
            Toast.makeText(view.getContext(),range.getMatchedText(),Toast.LENGTH_SHORT).show();
        }
    }
}
