package com.hxqc.autonews.util;

import android.text.TextUtils;

import com.hxqc.autonews.activities.AutoInfoDetailActivity;
import com.hxqc.autonews.model.pojos.AutoInfoData;
import com.hxqc.autonews.model.pojos.AutoInfoDetail;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-21
 * FIXME
 * Todo 拼接Html页面工具(新车上市和新车日历的详情也是共用的（已做判断处理）)
 *  如果需求改动可以分段打印出来拼接起来在sublime中查看，保存后缀.html使用浏览器预览调试修改
 */

public class NewAutoDetailHelper {
    private static final String DATA_TAG = "[DATA]";
    private static final String IMAGE_TAG = "[IMG_1]";

    private static final String ARTICEL_TAG = "[ARTICEL]";

    public static final String completeHtml(AutoInfoDetail detail, int from) {
        ArrayList<AutoInfoData> data = detail.data;
        ArrayList<AutoInformation> articles = detail.relevant;
        StringBuilder sb = new StringBuilder();
        String head = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1,maximum-scale=1.0, user-scalable=no\" />\n" +
                "    <title>新车资讯</title>\n" +
                "    <style type=\"text/css\">\n" +
                "    * {\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "    }\n" +
                "    \n" +
                "   body {\n" +
                "        font-family: \"微软雅黑\";\n" +
                "        background-color: #FFF;\n" +
                "    }" +
                "    autoDatas {\n" +
                "        font-family: \"微软雅黑\";\n" +
                "        background-color: #F0F0F0;\n" +
                "    }\n" +
                "    \n" +
                "    .container {\n" +
                "        width: calc(100% - 32px);\n" +
                "        margin-left: 16px;\n" +
                "        margin-right: 16px;\n" +
//                "        padding: 1rem 0;\n" +
                "        color: #333333;\n" +
                "        background-color: #ffffff;" +
                "    }\n" +
                "    \n" +
                "    .wrapBox {\n" +
//                "        margin: 0 16px;"+
//                "        margin-left: 16px;\n" +
                "        margin-top: 16px;" +
                "        color: #f0f0f0;\n" +
                "        position: relative;\n" +
                "        background-color: #F0F0F0;\n" +
                "        box-shadow: 0.1px 0.1px 5px rgba(64, 64, 64, .01);\n" +
                "        display: none;\n" +
                "    }\n" +
                "    \n" +
                "    .img {\n" +
                "        width: calc(100%);\n" +
//                "        margin-left: 16px;\n" +
                "        height: auto;\n" +
                "    }" +
                "    .title {\n" +
//                "        width: 75px;\n" +
                "        height: 24px;\n" +
                "        text-align: center;\n" +
                "        padding-left: 0.5rem;" +
                "        padding-right: 0.75rem;" +
                "        line-height: 24px;\n" +
                "        font-size: 15px;\n" +
                "        background: url(file:///android_asset/right_arror.png) no-repeat;\n" +
                "        background-size: 100% 24px;\n" +
                "        text-indent: -0.5em;\n" +
                "        font-weight: bold;" +
                "        display: inline-block;" +
                "    }\n" +
//                "   .wrapBox .long {\n" +
//                "        width: 108px;\n" +
//                "    }\n" +
                "    .articleTitle {\n" +
                "        padding: 0.6rem 0;\n" +
                "    }\n" +
                "    \n" +
                "    .articleTitle a{\n" +
                "       display: block;\n" +
                "       text-decoration: underline;\n" +
                "       color: #444;\n" +
                "       font-size: 14px;\n" +
                "       padding:0.15rem 0.5rem;\n" +
                "       max-height: 1rem;\n" +
                "       overflow: hidden;\n" +
                "       text-overflow:ellipsis;\n" +
                "       white-space: nowrap;\n" +
                "    }" +
                "    .innerBox {\n" +
                "        padding: 0 0.5rem;\n" +
                "        border-bottom: 1px solid #F0F0F0;\n" +
                "    }\n" +
                "     .innerBox:nth-last-of-type(1){padding-bottom: 0.4rem;}" +
                "    \n" +
                "    .innerBox img {\n" +
                "        width: 100%;\n" +
                "        height: auto;\n" +
                "        margin-top: 0.4rem;\n" +
                "    }\n" +
                "    \n" +
                "    .innerBox h4 {\n" +
                "        font-weight: normal;\n" +
                "        color: #252525;\n" +
                "        font-size: 16px;\n" +
                "        word-break: break-all;\n" +
                "        text-align: justify;\n" +
                "    }\n" +
                "    \n" +
                "    .innerBox p {\n" +
                "        font-weight: normal;\n" +
                "        color: #909090;\n" +
                "        font-size: 13px;\n" +
                "        padding-bottom: 8px;\n" +
                "        line-height: 24px;\n" +
                "    }\n" +
                "    \n" +
                "    .innerBox p span {\n" +
                "        font-weight: bold;\n" +
                "        color: #C73949;\n" +
                "        font-size: 13px;\n" +
                "    }\n" +
                "     #modelBox {\n" +
                "        padding-bottom: 0.3rem;\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner {\n" +
                "        padding: 0.25rem;\n" +
                "        background-color: #fff;\n" +
                "        margin: 0.6rem 0.25rem;\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner:nth-last-of-type(1) {\n" +
                "        margin-bottom: 0.6rem\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner > h4 {\n" +
                "        font-weight: normal;\n" +
                "        color: #252525;\n" +
                "        font-size: 16px;\n" +
                "        text-align: justify;\n" +
                "        line-height: 27px;\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner p {\n" +
                "        position: relative;\n" +
                "        color: #909090;\n" +
                "        font-size: 13px;\n" +
                "        padding-top: 6px;\n" +
                "        padding-bottom: 2px;\n" +
                "        line-height: 1.2rem;\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner p span {\n" +
                "        color: #C73949;\n" +
                "        font-size: 13px;\n" +
                "        font-weight: bold;\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner p a {\n" +
                "        display: inline-block;\n" +
                "        height: 1.2rem;\n" +
                "        width: 72px;\n" +
                "        right: 0;\n" +
                "        text-align: center;\n" +
                "        color: #fff;\n" +
                "        font-size: 15px;\n" +
                "        position: absolute;\n" +
                "        background-color: #ff7043;\n" +
                "        border-radius: 4px;\n" +
                "        cursor: pointer;\n" +
                "        transition: background-color 0.1s;\n" +
                "        -webkit-transition: all 0.1s;\n" +
                "    }\n" +
                "    \n" +
                "    #modelBox > .inner p a.added {\n" +
                "        background-color: #aeaeae;\n" +
                "        transition: background-color 0.1s;\n" +
                "        -webkit-transition: background-color 0.1s;\n" +
                "        cursor: pointer;\n" +
                "    }" +
                "    \n" +
                "    @media screen and (min-width: 414px) {\n" +
                "        .title {\n" +
                "            font-size: 16px;\n" +
                "            width: 85px;\n" +
                "            height: 26px;\n" +
                "            line-height: 26px;\n" +
                "        }\n" +
                "        .wrapBox .long {\n" +
                "            width: 110px;\n" +
                "        }\n" +
                "        .articleTitle a {\n" +
                "            font-size: 15px;\n" +
                "        }" +
                "        .innerBox h4 {\n" +
                "            font-size: 17px;\n" +
                "        }\n" +
                "        .innerBox p {\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "        .innerBox p span {\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "        #modelBox > .inner > h4 {\n" +
                "            font-size: 17px;\n" +
                "        }\n" +
                "        #modelBox > .inner p {\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "        #modelBox > .inner p span {\n" +
                "            font-size: 14px;\n" +
                "        }" +
                "    }\n" +
                "    </style>\n" +
                "    <script type=\"text/javascript\">\n" +
                "    (function(doc, win) {\n" +
                "        var docEl = doc.documentElement,\n" +
                "            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',\n" +
                "            recalc = function() {\n" +
                "                var clientWidth = docEl.clientWidth;\n" +
                "                if (clientWidth > 1080) {\n" +
                "                    clientWidth = 750;\n" +
                "                    docEl.style.fontSize = 48 + 'px';\n" +
                "                }\n" +
                "                if (!clientWidth) return;\n" +
                "                docEl.style.fontSize = 20 * (clientWidth / 320) + 'px';\n" +
                "            };\n" +
                "        recalc();\n" +
                "        if (!doc.addEventListener) return;\n" +
                "        win.addEventListener(resizeEvt, recalc, false);\n" +
                "        doc.addEventListener('DOMContentLoaded', recalc, false);\n" +
                "    })(document, window);\n" +
                "    var data = ";


        String autoDatas = "[]";
        if (data != null && from == AutoInfoDetailActivity.AUTO_INFO) {
            autoDatas = JSONUtils.toJson(data);
        }
        String comma = ";\nvar articles = ";
        String articlesStr = "[]";
        if (articles != null) {
            articlesStr = JSONUtils.toJson(articles);
        }
        String comma2 = ";\nvar models = ";
        String models = "[]";
        //models的数据添加在这里
        if (data != null && from == AutoInfoDetailActivity.AUTO_CALENDAR) {
            models = JSONUtils.toJson(data);
        }

        String com = ";";
        String date = detail.date;
        if (!TextUtils.isEmpty(date) && date.length() > 10) {
            date = date.substring(0, 10);
        }
        String javaScript = " window.onload = function() {\n" +
                "        var wrapBox = document.getElementById(\"wrapBox\");\n" +
                "         if (data.length) {\n" +
                "           wrapBox.style.display = \"block\";\n " +
                "           data.forEach(function(obj,index){\n" +
                "           var price = obj.itemOrigPrice;\n" +
                "           if(price){\n" +
                "               var newPrice =\"\";\n" +
                "                   price = price.split('-');\n" +
                "                   if(price.length == 2){\n" +
                "                   newPrice = price.map(function(item){\n" +
                "                   return item = (item/10000).toFixed(2)+'万';\n" +
                "           });\n" +
                "           newPrice = newPrice.join('-');\n" +
                "           }else if(price[0] == '暂无'){" +
                "               newPrice = '暂无'" +
                "           }else{\n" +
                "               newPrice = (price[0]/10000).toFixed(2)+'万';\n" +
                "           }\n" +
                "           }\n" +
                "           var innerBox = document.createElement('div');\n" +
                "       innerBox.className='innerBox';\n" +
                "       innerBox.onclick = function(){\n" +
                "       toSeeDetail(index);\n" +
                "       } \n" +
                "       innerBox.innerHTML = '<img src=\\\"'+obj.thumb+'\\\"/><h4>'+obj.name+'</h4><p>厂商指导价：<span>'+newPrice+'</span></p>';    \n" +
                "       wrapBox.appendChild(innerBox);\n" +
                "});\n" +
//                "}else{\n" +
//                "    wrapBox.style.display = \"none\";" +
                "}\n" +
                "var articleBox = document.getElementById(\"articleBox\");" +
                "if (articles.length) {\n" +
                "       articleBox.style.display = \"block\";" +
                "       var articleTitle = document.getElementById(\"articleTitle\");\n" +
                "       articles.forEach(function(obj, index) {\n" +
                "           if(index < 4){\n" +
                "             var title = obj.title;\n" +
                "             var linkA = document.createElement('a');\n" +
                "             linkA.onclick = function() {\n" +
//                "                    //点击某条文章跳转事件\n" +
                "                  toAutoInfoDetail(index);\n" +
                "             }\n" +
                "             linkA.innerHTML = title;\n" +
                "             articleTitle.appendChild(linkA);\n" +
                "           }\n" +
                "       });\n" +
//                "        }else{\n" +
//                "            var articleBox = document.getElementById(\"articleBox\");\n" +
//                "            articleBox.style.display = \"none\";\n" +
                "       }" +
//                "    }\n" +
                "     var modelBox = document.getElementById(\"modelBox\");\n" +
                "        if (models.length) {\n" +
                "            /*2016/11/16*/\n" +
                "            modelBox.style.display = \"block\";\n" +
                "            //设置需要对比的车型标题\n" +
                "            var title = modelBox.querySelector('.title');\n" +
                "            title.innerHTML = '<span>' + models[0].seriesName + '最新车型</span>';\n" +
                "            models.forEach(function(obj, index) {\n" +
                "                var price = obj.itemOrigPrice;\n" +
                "                if (price) {\n" +
                "                    var newPrice = \"\";\n" +
                "                    price = price.split('-');\n" +
                "                    if (price.length == 2) {\n" +
                "                        newPrice = price.map(function(item) {\n" +
                "                            return item = (item / 10000).toFixed(2)+'万';\n" +
                "                        });\n" +
                "                        newPrice = newPrice.join('-');\n" +
                "                    }else if(price[0] == '暂无'){" +
                "                        newPrice = '暂无'" +
                "                    } else {\n" +
                "                        newPrice = (price[0] / 10000).toFixed(2)+'万';\n" +
                "                    }\n" +
                "                }\n" +
                "                var inner = document.createElement('div');\n" +
                "                inner.className = 'inner';\n" +
                "                /*inner.onclick = function(){\n" +
                "                \ttoSeeDetail(index);\n" +
                "                }*/\n" +
                "                //添加对比按钮\n" +
                "                var btn = document.createElement('a');\n" +
                "                btn.className = 'addCompare';\n" +
                "                btn.innerHTML = '加入对比';\n" +
                "                //点击对比按钮\n" +
                "                btn.onclick = function() {\n" +
                "                    compare(index);\n" +
                "                }\n" +
                "                inner.innerHTML = '<h4>' + obj.modelName + '</h4><p>厂商指导价：<span>' + newPrice + '</span></p>';\n" +
                "                inner.querySelector('p').appendChild(btn);\n" +
                "                modelBox.appendChild(inner);\n" +
                "            });\n" +
                "        } /*2016/11/16*/\n" +
                "    }\n" +
                "\n" +
                "      function compare(index) {\n" +
                "        window.JavaScriptMonth.toCompare(index); \n" +
                "      }\n" +
                "      function toCompare(index) {\n" +
//                "        alert(index);" +
                "        var btn = document.getElementsByClassName('addCompare');\n" +
                "        var currBtn = btn[index];\n" +
                "        c = currBtn.className;\n" +
                "        //查找 空格more 而非more\n" +
                "        if (c != null && c.indexOf(' added') > -1) {\n" +
                "            //连空格一起替换\n" +
                "            currBtn.className = c.replace(' added', '');\n" +
                "            currBtn.innerHTML = '加入对比';\n" +
                "        } else {\n" +
                "            currBtn.className = c + ' added';\n" +
                "            currBtn.innerHTML = '取消对比';\n" +
                "        }\n" +
                "\n" +
                "    }" +
//                "     function compare(that,index) {\n" +
//                "       window.ToCompare.runOnAndroidJavaScript(index);" +
//                "        //var unAdd= document.querySelector(that).classList.contains(\"unAdd\")\n" +
//                "        //var t = document.getElementById('t'),\n" +
//                "        c = that.className;\n" +
//                "        //查找 空格more 而非more\n" +
//                "        if (c != null && c.indexOf(' added') > -1) {\n" +
//                "            //连空格一起替换\n" +
//                "            that.className = c.replace(' added', '');\n" +
//                "            that.innerHTML = '加入对比';\n" +
//                "        } else {\n" +
//                "            that.className = c + ' added';\n" +
//                "            that.innerHTML = '取消对比';\n" +
//                "        }\n" +
//                "    }" +
                "    function toSeeDetail(index) {\n" +
                "        window.JavaScriptMonth.toDetail(index);\n" +
                "    }\n" +
                "    function toAutoGallery(index) {\n" +
                "        window.JavaScriptMonth.toAutoGalley(index);\n" +
                "    }\n" +
                "    function toAutoInfoDetail(index){\n" +
//                "        alert(\"index:\");"+
                "        window.JavaScriptMonth.toAutoInfoDetail(index);\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n <div class=\"container\">\n" +
                " <div style=\"border-bottom: 1px solid #D5D5D5;margin-bottom: 7px\">\n" +
                "        <h4 style=\"font-size: 24px;line-height: 28px;margin-top: 13px;margin-bottom: 0;color: #444;\">" +
                detail.title +
                " </h4>\n" +
                "        <p style=\"color: #999999;font-size: 14px;line-height: 17px;margin-top: 10px;margin-bottom:10px\"><span>" +
                detail.sourceForm +
                "</span><span>&nbsp;&nbsp;</span><span>" +
                date + "</span></p></div>";
        String foot = " </div>\n</body>" +
                "</html>";

        String body = clearATag(detail.body);
        body = body.replace(DATA_TAG, "<div id=\"articleBox\" class=\"wrapBox\">\n" +
                "                   <div class=\"title long\">相关文章推荐</div>\n" +
                "                   <div id=\"articleTitle\" class=\"articleTitle\">\n" +
                "                   </div>\n" +
                "            </div> " +
                "            <div id=\"wrapBox\" class=\"wrapBox\">" +
                "                   <div class=\"title\">车辆详情</div>" +
                "            </div>" +
                "           <div id=\"modelBox\" class=\"wrapBox\">\n" +
                "                   <div class=\"title\"><span></span></div>\n" +
                "           </div>"

        );
//        body = body.replace(ARTICEL_TAG, "<div id=\"articleBox\" class=\"wrapBox\">\n" +
//                "            <div class=\"title long\">相关文章推荐</div>\n" +
//                "            <div id=\"articleTitle\" class=\"articleTitle\">\n" +
//                "            </div>\n" +
//                "        </div>");
        //body 替换图片
        ArrayList<String> bodyImg = detail.bodyImg;
        if (bodyImg != null) {
            for (int i = 0; i < bodyImg.size(); i++) {
                body = body.replace("[IMG_" + (i + 1) + "]",
                        "<p><img class=\"img\" src=\"" + bodyImg.get(i)
                                + "\" onclick=\"toAutoGallery(" + i + ")\"" +
                                " /></p>");
//            style="width:calc(100% - 32px);margin-left:16px;margin-right:16px;height: auto;"
            }
        }
        sb
                .append(head)

                .append(autoDatas)
                .append(comma)
                .append(articlesStr)
                .append(comma2)
                .append(models)
                .append(com)
                .append(javaScript)
                .append(body)
                .append(foot);
        DebugLog.d("HTML", head);
//        DebugLog.d("HTML", head1);
        DebugLog.d("HTML", autoDatas);
        DebugLog.d("HTML", comma);
        DebugLog.d("HTML", articlesStr);
        DebugLog.d("HTML", comma2);
        DebugLog.d("HTML", models);
        DebugLog.d("HTML", com);
        DebugLog.d("HTML", javaScript);
//        DebugLog.d("HTML", javaScript1);
        DebugLog.d("HTML", body);
        DebugLog.d("HTML", foot);
        return sb.toString();

    }

    /**
     * 清除a标记
     */
    public static String clearATag(String contentHtml) {
        if (contentHtml == null)
            return "";
        contentHtml = contentHtml.replaceAll("<a [^>]+>", "");
        contentHtml = contentHtml.replaceAll("</[ ]*a>", "");
        return contentHtml;
    }
}