---
title: 数据可视化开发指南
tag: 前端开发
publishDate: 2018-03-13
---

## 前言

本文档针对数据图表可视化开发框架开发指南

## 开发模板下载

[<idv 0.1.0>](/#)

## 目录结构

```
├─assets
│  ├─css
│  │  └─font
│  ├─fonts
│  ├─img
│  └─js
├─data
│  ├─config
│  └─map
├─public
│  ├─css
│  ├─fonts
│  └─img
├─server
└─src
    ├─routes
    │  ├─404
    │  ├─app
    │  │  └─pageone
    │  ├─login
    │  └─public
    └─utils
```

## 数据请求独立线程 worker.js

* 参数配置

```
current:   当前页名称 ;

hostName:  api服务器地址 ;

time:      数据请求间隔时间（单位毫秒）；

api:       api服务接口地址；



var current = '';

var hostName: 'http://localhost:8000';

var time = 300000 ;

var api = {
    pageOne:{
      componentsOne: {
          url: '/example/list',
      },
    }
};
```

## 应用主页面

* 全局变量

```
var myWorker;                    // 数据请求独立线程
var $mask = $('.mask-overlay');  // 页面切换遮罩层
var interVals = {};              // 滚动定时器集合
var pageOne = {                  // 配置页面接口回调方法
    componentsOne: function(data){
      //执行一些操作
    }
};
```

* 初始化页面

```
$(function(){
    initPage();     // 初始化sessionstorage
    initWorker();   // 初始化独立线程
    initHeight();   // 初始化容器高度设置
    initMaps();     // 执行初始化地图
    bindEvent();    // 绑定事件
});
```

* 初始化地图
  > （将地图初始化信息放在 initMaps（）方法中，加载数据时只需要更新 option 里的 data，不需要再重新初始化地图）

- 初始化当前页面

```
function loadPageOne(){
    var pageOption = {
        name: 'pageOne',
    };
    myWorker.postMessage(pageOption);
    if (isCurrentPage()) return;
    setTimeout(function() {
        scrollTimer(rockAndRoll,{name: 'example', page: pageOption.name, id:'example'});
    }, 2000);
}
```

* 事件绑定

```
function bindEvent(){
    $(document).on('click','.selector',function(e){
        // 一些操作
    });
}
```

* 页面结构,其中 data-number 属性是用于计算当前模块所占高度百分比

```
<div class="container-fluid model">
    <!--page1-->
    <div id="page1">
        <div class="row">
            <div class="col-md-3">
                <div id="total-number" data-number="0.15">
                </div>
                <div id="online-number" data-number="0.375">
                </div>
                <div id="history" data-number="0.375">
                </div>
            </div>
            <div class="col-md-6">
                <div id="online-total" data-number="0.15">
                </div>
                <div id="map" data-number="0.55">
                </div>
                <div id="top" data-number="0.2">
                </div>
            </div>
        </div>
    </div>
    <div id="page2">
      ...
    </div>
</div>
```
