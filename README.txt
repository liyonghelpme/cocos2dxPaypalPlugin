
新版本的PayPal 原理

客户端流程:
主要查看 Paypal.java MyWeb.java 文件

1.客户端lua脚本 在购买水晶的时候 首先向我们服务器申请一个订单号  genRecordId    {invoice=invoice}
    这个订单包含有用户信息 和购买水晶的信息
    genRecordId  uid=? kind=?
    
2.接着将订单号以及产品名称信息 价格 货币单位 传给 java层:  PaypalJava 中 payForProduct 

3.PaypalJava 打开一个新的WebView 页面将订单号和产品名称 以Intent Extra数据方式传递给它

4.用户支付成功将会跳转到我方成功页面 WebView 根据跳转到成功页面 来判定支付成功， 关闭webview的时候 将会客户端 支付成功

5.如果用户在没有跳转到Success 页面就关闭掉WebView， 则通过检测后台服务器状态 checkBuyRecord 判定是否购买成功

6.用户支付成功PayPal 将调用我方后台接口， ipn.php, ipn将会将成功记录插入到数据库里面, 便于客户端验证是否购买成功 
    checkBuyRecord uid=? invoice=?

后台流程:
主要查看 app.py  
页面 charge.php success.php ipn.php  
数据库 buyRecord record 

1.客户端请求 charge.php 页面 传入相应的参数 item_name商品名称 amount 价格 currency_code 货币 invoice 订单号 

2.支付成功 paypal自动跳转到 success.php 页面, 同时后台通知 ipn.php 页面

3.用户取消 跳转到 cancel.html 页面





出错:
补救措施1:
    用户提前退出了WebView 导致PayPal 扣款成功 但是 没有增加水晶
    通过检测数据库，可以找到用户购买水晶记录，以及该记录状态 = 0
    IPN.php 插入数据库记录, 表示订单完成
    设定购买水晶记录 状态 = 1
    
    将水晶发给用户之后, 记录状态 = 2 即可

补救措施2:
    可以在收到IPN 消息的时候 向用户客户端发送一条消息



Google IAB 支付流程

1.HelloWorldScene.cpp  productName 中使用 在play.google.com/apps/publish 上面注册的商品ID
2.PaypalJava.java   payForProduct 时候启动IabActivity  并且传入商品的ID
3.IapActivity.java  调用utils 里面的IabHelper 类的接口: 
    startSetup  连接手机上的 google play 服务
    queryInventoryAsync 查询google play 上商品信息
    launchPurchaseFlow 启动购买流程 
    consumeAsync 购买成功商品之后，再调用接口消费掉商品，这样用户才能再次购买



4.使用liyonghelpme1@gmail.com 作为支付测试账号  
5.手机上面的 google play 必须使用 vpn 连接 （翻越vpn免费版本）, 使用上面账号登陆 


Amazon IAB 支付流程：
1.测试时 在sd卡 根出 放置文件amazon.sdktester.json 文件
2.安装 AmazonSDKTester.apk 文件
3.修改代码位置
4.增加库文件 
in-app-purchase
HelloCpp.java
PayPalJava.java
AmazonIAP.java
xml 配置文件



Google IAP 没有安装时的bug修正:

IapActivity.java 中 当检测到没有google的服务的时候，将错误信息装入data中
PaypalJava.java 中 检测到CANCELED 并且服务不可用，则返回FAIL, message是  "not available"

检测到Fail 且msg "not available" 通知用户不可用即可
