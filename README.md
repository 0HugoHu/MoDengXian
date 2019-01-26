<div align=center> 
	
# 莫等闲 MoDengXian

## `项目简介 Intro`

<div align=left> 
	
> **社会价值：** 解决了用户必须观看视频前漫长广告的问题，缓解了网络不佳时用户等待应用数据加载的焦虑。填充零碎时间，助力高效生活。
> 
> **项目介绍：** 一款构建在通知栏的工具类APP。下滑通知栏，便可阅读每日趣闻、背几个单词、做些缓解放松，甚至浏览以前各大应用收藏的内容！
> 
> **项目优势：**   
> 1、团队设计出了完整的产品交互demo。  
> 2、想法独一无二，下拉通知栏不必担心广告暂停播放、应用正常加载。  
> 3、汇集所有应用收藏于一处，有网时自动缓存，随时随地均可便捷浏览。   
> 4、市场无竞争，未来前景广阔，商业收益高。
> 
> Read pieces of news while waiting for the ads, relax your mind while loading the app data.  
>  All you need is to merely slide down the notification bar. Say **GOOD BYE** to the ads and **NEVER** wait when there is no network!

***
<div align=center>
	
## `图片展示 Picture`

[![FO75hF.md.jpg](https://s2.ax1x.com/2019/01/10/FO75hF.md.jpg)](https://imgchr.com/i/FO75hF)

***
## `开发日志 Dev-Log`

<div align=left> 

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
    2.新增“莫等闲助手”程序以获取新闻并上传到服务器

	Debug：
    1.安卓6.0后写入sd卡必须动态申请权限得到用户许可

***
| 2019 01 26 | Version d5.0 | Code: 12 |
|--|--|--|

	Dev：
    1.通过jsoup解析html获取新闻内容（澎湃新闻可用）
    2.

	Debug：
    1.无法解析网易新闻html？
    2.无法获取澎湃新闻相关推荐图片的地址？

