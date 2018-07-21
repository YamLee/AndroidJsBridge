# Android JS Bridge Library

> This project is used to quickly develop projects that require Html to interact with Android natively, out of the box.

[中文文档]()

## Quick Start

### Step1: Add project dependencies

```
implementation 'me.yamlee:android-jsBridgeAll:0.0.19'
```

### Step2: Create WebActivity

It is recommended to create a new WebActivity in the project.
All WebView jumps in the project are given to WebActivity.
 Please refer to the following code.

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

After creating the WebActivity as above, you can add the logic of the jump
webView where needed, such as clicking the button to jump to google website.

```
    fun onClickToXiaomi() {
        val intent = WebActivity.getIntent("https://www.google.com",context)
        startActivity(intent)
    }
```

### Step3: Create Js Call Native Method Processor

When creating WebActivity in Step1, you can see that
**SayHelloProcessor(delegate)** object has been added by **delegate**.
This SayHelloProcessor is a custom JsBridge method.
For the specific implementation, please refer to the following code.

```java
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

As you can see, implementing the custom JsBridge method only needs to
inherit the BaseJsCallProcessor class. This class needs to accept an
object that implements the **NativeComponentProvider** interface.
This provider object is to inject the object required by the custom processor.
Then there are two methods you need to implement

**getFuncName**: Define the JsBridge method name

**onHandleJsRequest**: This JsBridge method native logic layer needs
to implement the logic, you can see that just pop up a "Hello World" toast

### Step4: Frotend JS call the native method

The front-end code implementation needs to call **JSBridge.init()** method
when first calling AndroidJsBridge when using JsBridge calling method.
For the specific implementation, please refer to the following code.

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

### Step5: You're done

![](http://ww1.sinaimg.cn/large/6b051377ly1ft8jkz1is4g20880ftjs4.gif)

If you need to know more ways to use, please refer to the **demo** project

License
-------

    Copyright 2018 Yam Lee

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
