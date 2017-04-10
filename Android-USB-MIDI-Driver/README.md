# Android-USB-MIDI-Driver Lite
--------------------------------

### 说明

该项目拷贝自 [USB-MIDI-Driver](https://github.com/kshoji/USB-MIDI-Driver) 修改后用于适配安卓通过usb链接我们的钢琴

lite修改于master分支，只保留了链接和通信模块

### 依赖

Andoird 3.1(API Level 12) 及其以上

### 使用

1. 下载项目放到和主项目中 **settings.gradle** 同级的目录
2. 在主项目的 **settings.gradle** 中添加如下配置:

    `include ':Android-USB-MIDI-Driver'`

    在主项目的 **build.gradle** 中添加如下配置:

    `
    dependencies {
        compile project(':Android-USB-MIDI-Driver')
    }
    `

3. 在主项目的 **AndroidManifest.xml** 添加usb特性:

```xml
    <uses-feature android:name="android.hardware.usb.host" /> 
```

### 帮助

* [点这查看USB-MIDI-Driver项目Wiki](https://github.com/kshoji/USB-MIDI-Driver/wiki/How-to-setup-the-library-to-the-application)

* [点这里参考智能钢琴项目的接入](http://codereview.xiaoyezi.com/#/admin/projects/alice)
