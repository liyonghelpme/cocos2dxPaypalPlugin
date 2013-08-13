#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "PayPal.h"
using namespace cocos2d;
using namespace cocos2d::plugin;

class MyPurchaseResult : public cocos2d::plugin::PayResultListener
{
public:
	virtual void onPayResult(cocos2d::plugin::PayResultCode ret, const char* msg, cocos2d::plugin::TProductInfo info);
};


class HelloWorld : public cocos2d::CCLayer
{
public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // there's no 'id' in cpp, so we recommend returning the class instance pointer
    static cocos2d::CCScene* scene();
    
    // a selector callback
    void menuCloseCallback(CCObject* pSender);
    
    // implement the "static node()" method manually
    CREATE_FUNC(HelloWorld);

    unsigned char *data;

    virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
    virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
    virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);

    CCNode *girl;
    CCSprite *sp1;
    CCTexture2D *t2;
    CCTexture2D *circle;
    unsigned char *cirData;
	CCSprite *sp2;

    unsigned char *transferData;
    void drawPoint(CCTouch*);
    int currentGirl;
    int maxGirl;

    void createGirl(int);
    void preOne(CCObject *);
    void nextOne(CCObject *);

private:
    PayPal *paypal;
    MyPurchaseResult *result;
};

#endif // __HELLOWORLD_SCENE_H__
