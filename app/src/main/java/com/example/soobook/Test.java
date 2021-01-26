package com.example.soobook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.soobook.Login;
import com.example.soobook.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Test extends AppCompatActivity {

    TextView lib_name, lib_type, lib_number;
    String name, type, close_day, weekOpenTime, weekCloseTime, satOpenTime, satCloseTime, holidayOpenTime, holidayCloseTime;
    EditText et_search_text;
    ImageButton search_btn;
    LinearLayout detail_view;
    String requestUrl;
    String isbn="9788926406441";

    ArrayList<Book> list;
    Book book;
    AlertDialog dialog;
    String[][] book_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lib_name = findViewById(R.id.name);
        lib_type = findViewById(R.id.type);
        lib_number = findViewById(R.id.number);
        et_search_text = findViewById(R.id.search_text);
        search_btn = findViewById(R.id.search_btn);
        detail_view =findViewById(R.id.detail_view);
        getIntentString();

                list = new ArrayList<>();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();

          //      Log.e(this.getClass().getName(),"돼라"+book.getAuth());


    }

    public void getIntentString() {
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        close_day = getIntent().getStringExtra("close_day");}

    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {



            Log.e(this.getClass().getName(), isbn);
            requestUrl = "http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key=1af3f780faeed316e48de8f0e2541d43eecf78d212859aed298460eddff2bd16&" +
                    "result_style=xml&page_no=1&page_size=10&isbn=" +isbn;

            try {
                boolean name = false;
                boolean type = false;
                boolean closeDay = false;


                Log.e(this.getClass().getName(), requestUrl);

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

            /*    int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("</e>") && book != null) {
                                list.add(book);
                            }

                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("<o>")) {
                                book = new Book();
                            }

                            if (parser.getName().equals("<TITLE type=\"string\">")) name = true;
                            if (parser.getName().equals("<AUTHOR type=\"string\">")) type = true;
                            if (parser.getName().equals("<PUBLISHER type=\"string\">")) closeDay = true;

                            break;
                        case XmlPullParser.TEXT:
*/
                            if (parser.getName().equals("<TITLE")) name = true;
                            if (parser.getName().equals("<AUTHOR")) type = true;
                            if (parser.getName().equals("<PUBLISHER")) closeDay = true;
                            if (name) {
                                book.settitle(parser.getText());
                                name = false;
                            } else if (type) {
                                book.setAuth(parser.getText());
                                type = false;
                            } else if (closeDay) {
                                book.setPub(parser.getText());
                                closeDay = false;
                            }
                      //      break;
              //      }

               //     eventType = parser.next();

               // }
            } catch (Exception e) { Log.e(this.getClass().getName(), "에러!!! : " + e); }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            lib_name.setText(book_info[0][0]);
            lib_type.setText(book_info[1][1]);
            lib_number.setText(book_info[2][2]);
        }
    }
}

