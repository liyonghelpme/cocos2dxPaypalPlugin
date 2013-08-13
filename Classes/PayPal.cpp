#include "PayPal.h"
#include "PluginUtils.h"

PLUGIN_REGISTER_IMPL(PayPal)

PayPal::~PayPal() {
}

bool PayPal::init() {
    CCLog("init PayPal");
    return PluginUtils::initJavaPlugin(this, "com.liyong.paypal.PaypalJava");
}

    /*
     * CLIENT_ID  PayPal 注册的clientID
     * RECEIVER_EMAIL 注册的账号EMAIL
     */ 
void PayPal::configDeveloperInfo(TIAPDeveloperInfo devInfo) {
    ProtocolIAP::configDeveloperInfo(devInfo);
}

void PayPal::payForProduct(TProductInfo info) {
    ProtocolIAP::payForProduct(info);
}

const char* PayPal::getSDKVersion() {
    return "1.0";
}
//设定是否沙盒环境
void PayPal::setDebugMode(bool debug) {
    CCLog("PayPal set Debug %d", debug);
    ProtocolIAP::setDebugMode(debug);
}


