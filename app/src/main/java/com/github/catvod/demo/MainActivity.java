package com.github.catvod.demo;

import android.app.Activity;
import android.os.Bundle;

import com.github.catvod.R;
import com.github.catvod.spider.Init;
import com.github.catvod.spider.Tpzy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init.init(getApplicationContext());
    new Thread(
            () -> {
              Tpzy tpzy = new Tpzy();
              tpzy.init(
                  MainActivity.this,
                  "https://gh-proxy.com/https://raw.githubusercontent.com/zww0019/CatVodSpider/main/json/tpzy.json");
              String homeContent = tpzy.homeContent(true);
              String categoryContent = tpzy.categoryContent("66", "1", false, new HashMap<>());
              String detail = tpzy.detailContent(Arrays.asList("243_71291"));
              String searcht = "";
              try {
                searcht = tpzy.searchContent("斗破", true);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
              try {
                JSONObject jsonObject = new JSONObject(homeContent);
                JSONObject category = new JSONObject(categoryContent);
                JSONObject aaa = new JSONObject(detail);
                JSONObject search = new JSONObject(searcht);
                System.out.println();
              } catch (JSONException e) {
                throw new RuntimeException(e);
              }
            })
        .start();
    }
}