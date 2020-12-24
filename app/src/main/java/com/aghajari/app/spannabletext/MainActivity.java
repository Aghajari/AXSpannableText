package com.aghajari.app.spannabletext;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.iosprovider.AXIOSEmojiProvider;

import com.aghajari.spannabletext.AXLinkMovementMethod;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AXEmojiView
        AXEmojiManager.install(this,new AXIOSEmojiProvider(this));


        TextView textView = findViewById(R.id.tv);
        textView.setMovementMethod(new AXLinkMovementMethod());
        textView.setText(MySpannableText.create("Hi @Aghajari, How you doin?\n"+
                "this is a [Test **Link**](https://google.com)\n"+
                "this is a **Bold** text\n"+
                "this is an *Italic* text\n"+
                "this is a ***Bold & Italic*** text\n"+
                "this is an ~~StrikeThrough~~ text\n"+
                "this is an ++Underline++ text\n"+
                "this is a ***~~++Custom++~~*** text\n\n"+
                "this is a `code`\n"+
                "MultilineCode : \n```line 1\nline 2```\n"+
                "AXEmojiView : \uD83D\uDE0D\uD83D\uDE1D\uD83D\uDE12\uD83D\uDE31\uD83C\uDF1A \n\n"+
                "Phone : +99123456\n"+
                "Email : test@email.com\n"+
                "URL : https://github.com\n"+
                "Hashtag : #AXSpannableText #Android\n\n"+
                "Custom : [Github \uD83D\uDE0E | Aghajari]"));
    }

}