# Android JS Bridge Library

> 此项目用于快速开发需要Html与Android原生交互的项目，开盒即用


## 快速开始

### Step1:添加项目依赖

```
implementation 'me.yamlee:android-jsBridge:0.0.6'
```

### Step2:实现WebActivity

建议在项目中新建WebActivity，项目中所有WebView的跳转都交给WebActivity，可参考如下代码

```java
class WebActivity : BaseActivity() {

    companion object {
        const val ARG_URL = "url"

        fun getIntent(url: String, context: Activity): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(ARG_URL, url)
            return intent
        }
    }

    private lateinit var delegate: CustomWebDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = CustomWebDelegate(this)
        setContentView(delegate.contentView)
        addJsCallProcessor()
        val url = intent?.getStringExtra(ARG_URL)
        if (!TextUtils.isEmpty(url)) {
            delegate.loadUrl(url!!)
        }
    }

    private fun addJsCallProcessor() {
        delegate.addJsCallProcessor(SayHelloProcessor(delegate))
    }
}
```

如上封装好WebActivity后，就可以在需要的地方加上跳转webview的逻辑，如点击按钮跳转小米官网

```
    fun onClickToXiaomi() {
        val intent = WebActivity.getIntent("https://www.xiaomi.com",context)
        startActivity(intent)
    }
```

### Step3:自定义JsBridge方法

在实现Step1实现WebActivity时，可以看到通过**delegate**添加了**SayHelloProcessor(delegate)**对象，这个SayHelloProcessor就是自定义的JsBridge方法，具体的实现可参考如下代码

```
class SayHelloProcessor(provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {

    private val context = provider.provideApplicationContext()

    override fun getFuncName(): String {
        return "sayHello"
    }

    override fun onHandleJsQuest(callData: JsCallData, callback: JsCallback): Boolean {
        Toast.makeText(context, "Hello World!", Toast.LENGTH_LONG).show()
        return true
    }

}
```

可以看到，实现自定义的JsBridge方法只需要继承BaseJsCallProcessor类，这个类需要接受一个为实现了**NativeComponentProvider**接口对象，这个provider对象是为了注入自定义Processor所需对象的。接着就是需要实现两个方法

**getFuncName**:定义JsBridge方法名称

**onHandleJsRequest**:这个JsBridge方法原生Native层需要实现的了逻辑，上面可以看到就仅仅弹出了一个“Hello World”的toast

### Step4: Web前端调用

前端代码实现在使用JsBridge调用方法是首先需要初始化AndroidJsBridge需要调用**JSBridge.init()**方法，具体实现可参考如下代码

```Js
<!DOCTYPE HTML>
<html lang="zh-CN" data-rem="375">
<head>
  <meta charset="UTF-8">
  <title>JSBridge Test</title>
  <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
  <meta name="apple-mobile-web-app-capable" content="no"/>
  <meta name="format-detection" content="telephone=no">

  <style>
      .container {
        padding: 10px;
      }
      button{
          width:300px;
          height:40px;
      }
      .margin{
          margin:5px
      }
  </style>

  <script type="text/javascript" charset="utf-8">
      function sayHello(){
        JSBridge.call('sayHello',
        {}, //this is params,now is empty
        //this is JsBridge method callback
        function(ret){
         console.log(JSON.stringify(ret))
        })
      }
  </script>
</head>

<body>

<div class="container">
  <button class=".margin" type="button" onclick="sayHello()">sayHello</button>
   <br>
</div>

<script type="text/javascript" charset="utf-8">
    (function () {
      var script = document.createElement('script');
      document.body.appendChild(script); script.onload = function () {
        JSBridge.init();
      }
    })()
</script>

</body>
</html>

```

### Step5: 大功告成

![](http://ww1.sinaimg.cn/large/6b051377ly1ft8jkz1is4g20880ftjs4.gif)

如果需要了解更多使用的方法，可参考**demo**项目

## 自定义WebHeader

## 深入WebDelegate

## 深入JsCallProcessor


