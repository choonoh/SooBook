package com.example.soobook;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
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

        TextView status1 = findViewById(R.id.result); //파싱된 결과확인!

        boolean inAddr = false, inChargeTp = false, inCpId = false;


        String addr = null, chargeTp = null, cpId = null;


        try{
            URL url = new URL("http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key=1af3f780faeed316e48de8f0e2541d43eecf78d212859aed298460eddff2bd16" +
                    "&result_style=xml&page_no=1&page_size=10&isbn=8958801077"); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("TITLE")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("AUTHOR")){ //address 만나면 내용을 받을수 있게 하자
                            inChargeTp = true;
                        }
                        if(parser.getName().equals("PUBLISHER")){ //mapx 만나면 내용을 받을수 있게 하자
                            inCpId = true;
                        }
                        break;
                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            addr = parser.getText();
                            inAddr = false;
                        }
                        if(inChargeTp){ //isAddress이 true일 때 태그의 내용을 저장.
                            chargeTp = parser.getText();
                            inChargeTp = false;
                        }
                        if(inCpId){ //isMapx이 true일 때 태그의 내용을 저장.
                            cpId = parser.getText();
                            inCpId = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("docs")){
                            String result = "주소 : " + addr + "\n 충전기 타입: " + chargeTp +"\n 충전소ID : " + cpId + "\n";
                            status1.setText(result);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            String result = "에러가..났습니다..." + e;
            status1.setText(result);
        }
    }
}