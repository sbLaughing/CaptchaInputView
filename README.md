# CaptchaInputView
a widget for captcha or password input








| 属性       | 描述         | 默认值|
| --------- |-------------| :-------:|
| mode      | underline  下划线样式<br/> border 边框模式-聚合<br>borderSparse 边框模式-疏散| border
| contentLength|密码或验证码个数|6
| borderRadius | 边框圆角半径<br>underline模式下不生效<br>borderSparse模式下对每个框生效      | 0dp
| borderColor | 边框线或者下划线的颜色     | Color.BLACK
| borderHighlightColor | 被选中那一栏边框线或者下划线高亮显示颜色    | borderColor
| borderWidth|边框线或者下划线的线条宽度|1dp
| borderLength|线条长度，为下划线长度或者边框的边长|40dp
| itemSpace|分开的间距，border模式不生效|12dp
| autoComplete|当输入个数达到设定个数时是否自动回调onComplete()|true|
| cipherEnable|是否对输入的内容显示为圆点|true
| textSize|输入内容的字体大小，也可能是圆点的绘制半径|14dp
| textColor|输入内容的字体或者圆点颜色|borderColor




