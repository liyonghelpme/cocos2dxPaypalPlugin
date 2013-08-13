#ifndef __PAYPAL_H__
#define __PAYPAL_H__
#include "cocos2d.h"
#include "ProtocolIAP.h"
#include <map>
#include <string>
using namespace cocos2d;
using namespace cocos2d::plugin;
class PayPal : public ProtocolIAP {
    PLUGIN_REGISTER_DECL(PayPal);
public:
    virtual bool init();
    /*
     * CLIENT_ID
     * RECEIVER_EMAIL 注册的账号EMAIL
     */ 
    virtual void configDeveloperInfo(TIAPDeveloperInfo devInfo);
    //PAYER_ID 游戏中用户的id
    //number 8.75 小数花费的金额
    //currency USD 货币单位
    //productName crystal0 购买对象名称
    virtual void payForProduct(TProductInfo info);
    
    //设定当前环境 在onCreate 的时候 构建service 就要确定环境sandbox
    virtual void setDebugMode(bool debug);
    
    virtual const char *getPluginVersion() {return "1.0";};
    virtual const char * getSDKVersion();

    virtual ~PayPal();
};
#endif 
