# MoDengXian
A Fragment Time Utilization and Collection Management Android App.

# Notice
This App was developed during **2019 winter** when I was a freshman in university. The technology used in the project is inevitably outdated. The server has stopped renewing, so the oneline service including daily news, hot search, or log in is no longer functioning properly.

# Introduction

MoDengXian (莫等闲) diagnosed pain points include anxiety when network is poor in metro, time wasted for too long video ads, and never reviewed content collections, and a plethora of small features were developed to creatively address people's needs.

Utilized web crawler to get daily information and hot searches, cached data offline, and implemented content browsing and gaming functions with notification bar and hover window.

**Won Hewlett-Packard Entrepreneurship Competition 2nd Place Nationwide.**

# Preview

|  |  |  |
|--|--|--|
|![](https://s1.ax1x.com/2022/07/27/vShPfI.jpg)|![](https://s1.ax1x.com/2022/07/27/vShCtA.jpg)|![](https://s1.ax1x.com/2022/07/27/vShSTH.jpg)|
![](https://s1.ax1x.com/2022/07/27/vSh9kd.jpg)|![](https://s1.ax1x.com/2022/07/27/vSfz0e.jpg)|![](https://s1.ax1x.com/2022/07/27/vShFpt.jpg)|
![](https://s1.ax1x.com/2022/07/27/vShk1P.jpg)|![](https://s1.ax1x.com/2022/07/27/vShA6f.jpg)|![](https://s1.ax1x.com/2022/07/27/vShEX8.jpg)|
![](https://s1.ax1x.com/2022/07/27/vShZnS.jpg)|![](https://s1.ax1x.com/2022/07/27/vShe0g.jpg)|![](https://s1.ax1x.com/2022/07/27/vShm7Q.jpg)|
![](https://s1.ax1x.com/2022/07/27/vShukj.jpg)|![](https://s1.ax1x.com/2022/07/27/vShMhn.jpg)|![](https://s1.ax1x.com/2022/07/27/vShKts.jpg)|



# Development Log

<div align=left> 

***
| 2018 12 06 | Version d1.0 | Code: 1 |
|--|--|--|

	Dev：
	1.实现通知栏自定义大图显示
	2.完成大图显示布局
	3.加入通知底栏操作按钮布局

	Debug：
	1.安卓8.0以后必须规范通知格式     
 
 ***
| 2018 12 07 | Version d1.1 | Code: 2 |
|--|--|--|

	Dev：
    1.完成图文显示布局

***
| 2018 12 08 | Version d1.2 | Code: 3 |
|--|--|--|

	Dev：
    1.调整已有内容布局
    2.加入收藏、游戏与放松版块（仅实现布局）

***
| 2018 12 09 | Version d2.0 | Code: 4 |
|--|--|--|

	Dev：
    1.实现通过Remoteview更新通知栏视图
    2.确定通知栏导航板块布局
    3.确定应用icon
    4.实现通知栏按钮点击事件
    5.实现加载本地文字图片资源

	Debug：
    1.资源文件命名不能含有大写字母

***
| 2018 12 17 | Version d2.1 | Code: 5 |
|--|--|--|

	Dev：
    1.规范变量命名（未完成）
    2.确定基本版块
    3.确定通知栏导航板块与内容板块组合显示样式
    4.实现导航栏点击效果
    5.调整收缩情况下通知栏显示

	Debug：
    1.普通view要设置onclick()才能运用点击事件

***
| 2018 12 18 | Version d2.2 | Code: 6 |
|--|--|--|

	Dev：
    1.规范变量命名（完成）
    2.按钮大小等细节布局调整
    3.实现按钮点击切换图标
    4.重新整理项目，删除无用代码

***
| 2019 01 10 | Version d3.0 | Code: 7 |
|--|--|--|

	Dev：
    1.实现无障碍功能的文字内容获取功能（仅针对TextView）
    2.项目添加到GitHub

	Debug：
    1.无障碍功能无法获取一般由程序绘制的View的文字内容

***
| 2019 01 11 | Version d3.1 | Code: 8 |
|--|--|--|

	Dev：
    1.Tim、知乎、新浪新闻等可以获取点击的整段文字
    2.使用SharePreference保存是否开启文字拾取设置
    3.其它应用可以分享内容到莫等闲
    4.读取剪切板，在测试WebView中打开网页
    5.尝试使用腾讯开源项目VasSonic的WebView控件

	Debug：
    1.无障碍功能“this service is malfunctioning”错误：部分点击区域没有node或内容，强行getText或toString出错
    2.安卓9.0后对未加密链接拒绝访问（非https字段）net:err_cleartext_not_permitted

***
| 2019 01 13 | Version d3.2 | Code: 9 |
|--|--|--|

	Dev：
    1.更改应用图标及通知栏显示图标
    2.更新最近新闻
    3.完善收藏和放松版块的展示视图

	Debug：
    1.Android Sutdio默认的app:srcCompat方法设置的ImageView图片会无法显示？
    
***
| 2019 01 14 | Version d3.3 | Code: 10 |
|--|--|--|

	Dev：
    1.通过设置setGroupSummary(false)禁止系统合并通知
    2.通知内容页面添加关闭按钮
    3.达到第一阶段的演示功能

***
| 2019 01 25 | Version d4.0 | Code: 11 |
|--|--|--|

	Dev：
    1.通过js实现模拟选择获取网站新闻内容
    2.新增“莫等闲助手”程序

	Debug：
    1.安卓6.0后写入sd卡必须动态申请权限得到用户许可

***
| 2019 01 26 | Version d5.0 | Code: 12 |
|--|--|--|

	Dev：
    1.通过jsoup解析html获取新闻及相关内容（澎湃新闻可用）
    2.完善“莫等闲助手”UI界面
    3.“莫等闲助手”加入用户身份检查
    3.将新闻内容重组格式写入本地文件
    4.添加leancloud云服务器并构建用户分类
    5.通过字符串replace()函数、split()函数和正则运算为文章缩句
    6.改进图文显示界面的布局

	Debug：
    1.无法解析网易新闻html？
    2.无法获取澎湃新闻相关推荐图片的地址？
    3.a=b+a，则插入内容在原a的前面
    4.Toast在handler中需包围在Looper.prepare()和Looper.loop()中使用

***
| 2019 01 27 | Version d6.0 | Code: 13 |
|--|--|--|

	Dev：
    1.增加了ProgressBar动画
    2.打开“莫等闲助手”时自动清除上次已下载内容
    3.实现下载新闻图片
    4.实现裁剪新闻图片
    5.改善“莫等闲助手”的视觉界面
    6.实现在dialog界面浏览并修改上传新闻的功能

	Debug：
    1.static方法中只能访问static变量，而非static方法可以访问所有变量
    2.在线程中使用animator必须包含在loop中，且一个线程最多启用一个loop
    3.属性动画只能对对象，不能对对象的id实施
    4.无法判断node是否存在？
    5.dialog在自定义view时，若重复调用必须对其父视图清除所有子元素
    
***
| 2019 01 28 | Version d7.0 | Code: 14 |
|--|--|--|

	Dev：
    1.优化内容裁剪功能
    2.改进“莫等闲助手”布局
    3.加入“使用说明，重置应用，选择新闻源，我的信息”等模块
    4.新增“我的信息”布局，引入“积分，贡献量”，添加排名布局
    5.实现上传已编辑文本到leancloud

	Debug：
    1.系统级代码优先于后台代码运行
    2.findviewbyId只能绑定显示的view的控件，若是dialog的自定义视图的控件，则需view.findviewbyId
    3.leancloud通过file方式无法上传文件？
 
 ***
| 2019 01 29 | Version d7.1 | Code: 15 |
|--|--|--|

	Dev：
    1.修复了若干小bug
    2.“莫等闲助手”的编辑新闻界面可跳过
    3.增加贡献值和积分属性，个人数据已同步
    4.发布“莫等闲助手”到蒲公英分发平台
    5.优化图片大小和动画流畅度

	Debug：
    1.List的add方法即便明确index也不能实现替换，替换为set方法
    2.java的Number变量可通过.intValue()直接获取数值，强行转int值会出错

 ***
| 2019 02 10 | Version d8.0 | Code: 16 |
|--|--|--|

	Dev：
    1.实现检查内容更新及下载视图，并将更新内容保存到本地
    2.拓展纯文本显示界面
    3.实现在通知栏阅读每日新闻
    4.“莫等闲助手”小错误的修复
    5.添加应用主界面（抽屉布局和导航栏布局）

	Debug：
    1.leancloud按照日期删除数据时会将最早创建的排序为第一个
    2.误用英文逗号导致无法按照逗号分隔文段
    3.导航栏fragment切换非放在@Override前无法监听点击？
    
 ***
| 2019 02 11 | Version d9.0 | Code: 17 |
|--|--|--|

	Dev：
    1.实现应用更换主题（应用哔哩哔哩开源库）
    2.适配抽屉页和底部导航栏主题更换

	Debug：
    1.安卓原生底部导航栏不提供某些自定义调整接口，需重写名称变量
    2.应用第三方抽屉页后，toolbar不支持自动变主题色？
    3.无法实现抽屉页覆盖于状态栏？
    
    
 ***
| 2019 02 12 | Version d9.1 | Code: 18 |
|--|--|--|

	Dev：
    1.toolbar适配主题变色
    2.添加设置界面布局
    3.优化交互逻辑

	Debug：
    1.RecyclerView无法在fragment中添加数据？（使用Listview代替）
    
 ***
| 2019 02 13 | Version d10.0 | Code: 19 |
|--|--|--|

	Dev：
    1.添加选择主题Activity及布局
    2.优化交互逻辑
    3.新增选择主题并查看主题界面
    4.添加两款主题

	Debug：
    1.返回MainActivity时加载设置的fragment仍会显示首页fragment？（设置fragment非透明背景直接覆盖）
    
 ***
| 2019 02 15 | Version d10.1 | Code: 20 |
|--|--|--|

	Dev：
    1.添加应用至快捷开关后台启动
    2.隐藏应用后台活动

	Debug：
    1.隐式启动被启动activity必须注册intent-category属性
    
 ***
| 2019 02 16 | Version d11.0 | Code: 21 |
|--|--|--|

	Dev：
    1.MainActivity通知部分代码重构，缩减75%代码量
    2.为用户自定义显示功能的显示通知方法、数据库做好准备
    3.设置-通知，可手动关闭通知提醒声音或应用中手机静音
    4.完成ListView中带Switch的实现

	Debug：
    1.ListView中嵌套Switch会自动屏蔽item焦点，设置Switch不可点击不可获得焦点以实现item的点击效果
    
 ***
| 2019 02 19 | Version d12.0 | Code: 22 |
|--|--|--|

	Dev：
    1.完成首页BannerBar功能（展示图片和文字）
    2.实现自定义通知栏显示功能

	Debug：
    1.调用其他activity的必须是statistic方法
    2.statistic变量无法修改
    
 ***
| 2019 02 20 | Version d12.1 | Code: 23 |
|--|--|--|

	Dev：
    1.设置加入还原布局及其它功能
    2.“莫等闲助手”已可以获取百度热搜和微博热搜的内容

 ***
| 2019 02 25 | Version d13.0 | Code: 24 |
|--|--|--|

	Dev：
    1.完成热搜界面显示布局
    2.实现热搜功能
    
	Debug：
    1.RemoteView中setImageViewResource的资源参数只需输入R.color.xxx，不能通过getResource...获取
    2.安卓8.0以上需动态权限开启悬浮窗
    3.RemoteView不支持ConstraitLayout

