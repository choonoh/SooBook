package com.example.soobook;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class Test extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail_view);

        StrictMode.enableDefaults();
        EditText isbn = findViewById(R.id.isbn_txt);
        TextView title = findViewById(R.id.book_title);
        TextView author = findViewById(R.id.book_author);
        TextView pub = findViewById(R.id.book_pub);
        ImageButton sc = findViewById(R.id.isbn_sc);


        sc.setOnClickListener(v -> {

            boolean inTitle = false, inAuthor = false, inPub = false;


            String Title = null, Author = null, Pub = null;
            try{
                URL url = new URL("http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key=1af3f780faeed316e48de8f0e2541d43eecf78d212859aed298460eddff2bd16" +
                        "&result_style=xml&page_no=1&page_size=10&isbn="+isbn.getText().toString()); //검색 URL부분

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                System.out.println("파싱시작합니다.");

                while (parserEvent != XmlPullParser.END_DOCUMENT){
                    switch(parserEvent){
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if(parser.getName().equals("TITLE")){ //title 만나면 내용을 받을수 있게 하자
                                inTitle = true;
                            }
                            if(parser.getName().equals("AUTHOR")){ //address 만나면 내용을 받을수 있게 하자
                                inAuthor = true;
                            }
                            if(parser.getName().equals("PUBLISHER")){ //mapx 만나면 내용을 받을수 있게 하자
                                inPub = true;
                            }
                            break;
                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if(inTitle){ //isTitle이 true일 때 태그의 내용을 저장.
                                Title = parser.getText();
                                inTitle = false;
                            }
                            if(inAuthor){ //isAddress이 true일 때 태그의 내용을 저장.
                                Author = parser.getText();
                                inAuthor = false;
                            }
                            if(inPub){ //isMapx이 true일 때 태그의 내용을 저장.
                                Pub = parser.getText();
                                inPub = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("docs")){
                                title.setText(Title);
                                author.setText(Author);
                                pub.setText(Pub);

                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch(Exception e){
                Log.e(this.getClass().getName(), "error", e);
            }

        });


    }
}