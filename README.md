# APKManage

------

## 引言

好久以前给公司写了个APK管理的平台，因为我们是小外包所以app会变得多，并且还有版本更新所以不好管理，加上我们基本都是安卓项目所以管理不复杂，我就写了个平台来管理每个项目对应的APP文件的上传下载，每个版本号的更新记录这些，当时用的Springboot2+vue2+ElementUI搭的，后续技术栈变更后不想再用之前那一套了，所以刚好放假花一天时间给重构一下。

## Tips ：

> **前后端项目在子分支里面 不在master分支！！！**



## 项目技术栈

### Web

- UI：NaiveUI + vanilla-tilt + vicons
- 语言：TS
- 框架：Vue.js 3
- 构建工具：Vite

### Server

- 框架：Springboot 3.2.0
- 语言：Kotlin 1.9.20
- 构建工具：Grade+kts
- ORM相关：Mybatis-flex-kotlin + actable

> 项目没做安全验证相关的，秉承着能跑就行的原则，而且本身就是管理个APK作为前后端方便沟通的平台，个人感觉没必要做安全验证其实，后面有时间写死个登录请求就差不多了。
