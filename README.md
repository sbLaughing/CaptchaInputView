# CaptchaInputView
-

一个方便的密码或验证码输入控件，继承自`View`。
相比一些实现方式为添加多个EditText或者单个隐藏EditText的方案，此控件更为简洁。

支持明文与密文显示，支持自定义长度，**只支持输入数字**。

放个截图




![截图](http://upload-images.jianshu.io/upload_images/787640-4e9ad7d9e212a6fe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# Download
-

**Step 1**. Add the JitPack repository to your build file<br/>Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```

**Step 2**. Add the dependency

```
	dependencies {
	        compile 'com.github.sbLaughing:CaptchaInputView:v1.1'
	}

```
# How to use
-
直接在你的布局xml中使用：

```
<com.alien.captchainputview.CaptchaInputView
        android:layout_width="match_parent"
        android:id="@+id/mCaptchaInputView"
        android:layout_height="wrap_content"
        android:layout_marginTop="12dp"
        app:borderColor="@android:color/holo_orange_dark"
        app:borderRadius="14dp"
        app:borderWidth="2dp"
        app:textSize="40sp" />
```

在jiava中设置回调：

```
((CaptchaInputView)findViewById(R.id.mCaptchaInputView)).setICaptchaViewListener(new ICaptchaViewListener() {
            @Override
            public void onContentChanged(String content) {
            }

            @Override
            public void onComplete(String content) {
            }
        });

```



***支持的属性：***


| 属性       | 描述         | 默认值|
| --------- |-------------| :-------:|
| mode      | underline  下划线样式<br/> border 边框模式-聚合<br>borderSparse 边框模式-疏散| border
| contentLength|密码或验证码个数|6
| borderRadius | 边框圆角半径<br>underline模式下不生效<br>borderSparse模式下对每个框生效      | 0dp
| borderColor | 边框线或者下划线的颜色     | Color.BLACK
| borderHighlightColor | 被选中那一栏边框线或者下划线高亮显示颜色  <br>**border不支持高亮**  | borderColor
| borderWidth|边框线或者下划线的线条宽度|1dp
| borderLength|线条长度，为下划线长度或者边框的边长|40dp
| itemSpace|分开的间距，border模式不生效|12dp
| autoComplete|当输入个数达到设定个数时是否自动回调onComplete()|true|
| cipherEnable|是否对输入的内容显示为圆点|true
| textSize|输入内容的字体大小，也可能是圆点的绘制半径|14dp
| textColor|输入内容的字体或者圆点颜色|borderColor


#LICENSE
-

```
Copyright (c) 2017-present, CaptchaInputView Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




