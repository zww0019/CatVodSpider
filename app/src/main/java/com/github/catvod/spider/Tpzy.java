package com.github.catvod.spider;

import android.text.TextUtils;

import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.Trans;
import com.github.catvod.utils.Utils;

import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tpzy extends XPath {
  @Override
  public String detailContent(List<String> ids) {
    fetchRule();
    String webUrl = rule.getDetailUrl().replace("{vid}", ids.get(0));
    String webContent = fetch(webUrl);
    JXDocument doc = JXDocument.create(webContent);
    JXNode vodNode = doc.selNOne(rule.getDetailNode());
    String cover = "",
        title = "",
        desc = "",
        category = "",
        area = "",
        year = "",
        remark = "",
        director = "",
        actor = "";
    title = vodNode.selOne(rule.getDetailName()).asString().trim();
    title = rule.getDetailNameR(title);
    cover = vodNode.selOne(rule.getDetailImg()).asString().trim();
    cover = rule.getDetailImgR(cover);
    cover = Utils.fixUrl(webUrl, cover);
    if (!rule.getDetailCate().isEmpty()) {
      try {
        category = vodNode.selOne(rule.getDetailCate()).asString().trim();
        category = rule.getDetailCateR(category);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }
    if (!rule.getDetailYear().isEmpty()) {
      try {
        year = vodNode.selOne(rule.getDetailYear()).asString().trim();
        year = rule.getDetailYearR(year);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }
    if (!rule.getDetailArea().isEmpty()) {
      try {
        area = vodNode.selOne(rule.getDetailArea()).asString().trim();
        area = rule.getDetailAreaR(area);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }
    if (!rule.getDetailMark().isEmpty()) {
      try {
        remark = vodNode.selOne(rule.getDetailMark()).asString().trim();
        remark = rule.getDetailMarkR(remark);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }
    if (!rule.getDetailActor().isEmpty()) {
      try {
        actor = vodNode.selOne(rule.getDetailActor()).asString().trim();
        actor = rule.getDetailActorR(actor);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }
    if (!rule.getDetailDirector().isEmpty()) {
      try {
        director = vodNode.selOne(rule.getDetailDirector()).asString().trim();
        director = rule.getDetailDirectorR(director);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }
    if (!rule.getDetailDesc().isEmpty()) {
      try {
        desc = vodNode.selOne(rule.getDetailDesc()).asString().trim();
        desc = rule.getDetailDescR(desc);
      } catch (Exception e) {
        SpiderDebug.log(e);
      }
    }

    Vod vod = new Vod();
    vod.setVodId(ids.get(0));
    vod.setVodName(title);
    vod.setVodPic(cover);
    vod.setTypeName(category);
    vod.setVodYear(year);
    vod.setVodArea(area);
    vod.setVodRemarks(remark);
    vod.setVodActor(actor);
    vod.setVodDirector(director);
    vod.setVodContent(desc);

    ArrayList<String> playFrom = new ArrayList<>();
    List<JXNode> fromNodes = doc.selN(rule.getDetailFromNode());
    for (int i = 0; i < fromNodes.size(); i++) {
      String name = fromNodes.get(i).selOne(rule.getDetailFromName()).asString().trim();
      name = rule.getDetailFromNameR(name);
      playFrom.add(name);
    }

    ArrayList<String> playList = new ArrayList<>();
    List<JXNode> urlListNodes = doc.selN(rule.getDetailUrlNode());
    List<String> vodItems = new ArrayList<>();
    for (int i = 0; i < urlListNodes.size(); i++) {
      if(i%2!=0){
        continue;
      }
      String name = urlListNodes.get(i).selOne(rule.getDetailUrlName()).asString().trim();
      name = rule.getDetailUrlNameR(name);
      String id = urlListNodes.get(i).selOne(rule.getDetailUrlId()).asString().trim();
      id = rule.getDetailUrlIdR(id);
      vodItems.add(Trans.get(name) + "$" + id);
      // 排除播放列表為空的播放源
      if (vodItems.size() == 0 && playFrom.size() > i) {
        playFrom.set(i, "");
      }
    }
    playList.add(TextUtils.join("#", vodItems));

    // 排除播放列表為空的播放源
    for (int i = playFrom.size() - 1; i >= 0; i--) {
      if (playFrom.get(i).isEmpty()) playFrom.remove(i);
    }
    for (int i = playList.size() - 1; i >= 0; i--) {
      if (playList.get(i).isEmpty()) playList.remove(i);
    }
    for (int i = playList.size() - 1; i >= 0; i--) {
      if (i >= playFrom.size()) playList.remove(i);
    }
    vod.setVodPlayFrom(TextUtils.join("$$$", playFrom));
    vod.setVodPlayUrl(TextUtils.join("$$$", playList));
    return Result.string(vod);
  }
}
