'use strict';
import React from 'react';
import {
    ToastAndroid,
    Platform,
} from 'react-native';
var Config = require('../Config');
var Security = require('../natives/SecurityModule');
var myHeaders = new Headers();
myHeaders.append('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 HXMall Android');
var myInit = { method: 'GET',
               headers: myHeaders,
               mode: 'cors',
               cache: 'default' };
var myRequest;

//单例
function ApiClient(){
  if (typeof ApiClient.instance === 'object') {
    return ApiClient.instance;
  }
  ApiClient.instance = this;
}

//首页 轮播图请求
ApiClient.prototype._HomeADRequest = function(URL,successCallback,errorCallback){
      myRequest = new Request(URL,myInit);
      fetch(myRequest).then(function(res) {
          if (res.ok) {
            console.log("apiClient  请求返回成功", res.status);
            res.json().then(function(data) {
                console.log("apiClient  请求返回成功  数据", data);
                successCallback && successCallback(data);
            });
          } else {
            console.log("apiClient  请求返回失败", res.status);
            errorCallback && errorCallback();
          }
        }, function(e) {
          console.log("apiClient  请求失败", e);
          errorCallback && errorCallback();
        });
};

//首页天气请求
ApiClient.prototype._HomeWeatherRequest = function(successCallback,errorCallback){

};

//首页咨询 分类按钮请求
ApiClient.prototype._HomeAutoInfoBtnsRequest = function(successCallback,errorCallback){

};

//首页咨询 推荐列表请求
ApiClient.prototype._HomeAutoInfoRecommensRequest = function(successCallback,errorCallback){

};
