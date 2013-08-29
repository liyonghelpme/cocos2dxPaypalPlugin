#include "HelloWorldScene.h"
#include "AppMacros.h"
#include <stdlib.h>
#include "PluginManager.h"
#include "PayPal.h"
USING_NS_CC;


CCScene* HelloWorld::scene()
{
    // 'scene' is an autorelease object
    CCScene *scene = CCScene::create();
    
    // 'layer' is an autorelease object
    HelloWorld *layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

void HelloWorld::createGirl(int id) {
}

void MyPurchaseResult::onPayResult(PayResultCode ret, const char*msg, TProductInfo info) {
    CCLog("ret %d %s %s", ret, msg, info["PAYER_ID"].c_str());

}

void HelloWorld::preOne(CCObject *send) {
    CCLog("onBuyIt");
    if(paypal == NULL) {
        result = new MyPurchaseResult();

        CCLog("loadPlugin");
        paypal = dynamic_cast<PayPal*>(PluginManager::getInstance()->loadPlugin("PayPal"));
        CCLog("load finish");

        CCLog("setDebugMode now");
        paypal->setDebugMode(true);

        TIAPDeveloperInfo paypalInfo;
        paypalInfo["CLIENT_ID"] = "AX-XExBYZ0SxFVwhret3TPXaCsDhdZ2eJQO83Wyj_jRmuTruCt-3-nABxMI1";

        paypalInfo["RECEIVER_EMAIL"] = "liyonghelpme@gmail.com";
        CCLog("config now");
        paypal->configDeveloperInfo(paypalInfo);
        CCLog("finish config");
        paypal->setResultListener(result);
    }
    //从服务器获取一个 invoice 号码
    /*
    CCHttpRequest *request = new CCHttpRequest();
    //用户的uid 和 购买的商品编号
    request->setUrl("http://www.caesarsgame.com:9070/genRecordId?uid=1&kind=0");
    request->setRequestType(CCHttpRequest::kHttpGet);
    request->setResponseCallback(this, httpresponse_selector());
    */

    TProductInfo info;
    info["PAYER_ID"] = "xiaoming";
    info["number"] = "100.2";
    info["currency"] = "USD";
    //info["productName"] = "crystal0";

    info["productName"] = "com.liyong.test2";
    //info["invoice"] = "";
    paypal->payForProduct(info);

    return;



    /*
    if(currentGirl > 0) {
        currentGirl--;

        char temp[100];
        ccBlendFunc bf = {GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA};
        sprintf(temp, "girls/after%d.png", currentGirl);
        CCTexture2D *tex = CCTextureCache::sharedTextureCache()->addImage(temp);
        sp1->setTexture(tex);
        sp1->setBlendFunc(bf);

        sprintf(temp, "girls/pre%d.png", currentGirl);
        t2 = CCTextureCache::sharedTextureCache()->addImage(temp);
        sp2->setTexture(t2);
        sp2->setBlendFunc(bf);


        glPixelStorei(GL_PACK_ALIGNMENT, 4);
        CCRenderTexture *render = CCRenderTexture::create(t2->getPixelsWide(), t2->getPixelsHigh(), kCCTexture2DPixelFormat_RGBA8888);
        render->begin();
        sp2->draw();
        glReadPixels(0, 0, t2->getPixelsWide(), t2->getPixelsHigh(), GL_RGBA, GL_UNSIGNED_BYTE, data);
        render->end();
    }
    */
}
void HelloWorld::nextOne(CCObject *send) {
    if(currentGirl < maxGirl) {
        currentGirl++;
        char temp[100];

        ccBlendFunc bf = {GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA};
        sprintf(temp, "girls/after%d.png", currentGirl);
        CCTexture2D *tex = CCTextureCache::sharedTextureCache()->addImage(temp);
        sp1->setTexture(tex);
        sp1->setBlendFunc(bf);

        sprintf(temp, "girls/pre%d.png", currentGirl);
        t2 = CCTextureCache::sharedTextureCache()->addImage(temp);
        sp2->setTexture(t2);
        sp2->setBlendFunc(bf);

        glPixelStorei(GL_PACK_ALIGNMENT, 4);
        CCRenderTexture *render = CCRenderTexture::create(t2->getPixelsWide(), t2->getPixelsHigh(), kCCTexture2DPixelFormat_RGBA8888);
        render->begin();
        sp2->draw();
        glReadPixels(0, 0, t2->getPixelsWide(), t2->getPixelsHigh(), GL_RGBA, GL_UNSIGNED_BYTE, data);
        render->end();

    }
}
// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !CCLayer::init() )
    {
        return false;
    }
    paypal = NULL;
    currentGirl = 0;
    maxGirl = 51;
    
    CCSize visibleSize = CCDirector::sharedDirector()->getVisibleSize();
    CCPoint origin = CCDirector::sharedDirector()->getVisibleOrigin();
    	
    CCSprite *back = CCSprite::create("settingbg.png");
    back->setPosition(ccp(visibleSize.width/2, visibleSize.height/2));
    addChild(back);
    CCSize sz = back->getContentSize();
    back->setScaleX(visibleSize.width/sz.width);
    back->setScaleY(visibleSize.height/sz.height);

    //480*360
    girl = CCNode::create();
    
    girl->setPosition(ccp(visibleSize.width/2, (visibleSize.height-50)/2));
    

    ccBlendFunc bf = {GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA};
    CCTexture2D *tex = CCTextureCache::sharedTextureCache()->addImage("girls/after0.png");
    sp1 = CCSprite::createWithTexture(tex);
    girl->addChild(sp1);
    sp1->setBlendFunc(bf);



    t2 = CCTextureCache::sharedTextureCache()->addImage("girls/pre0.png");
    sp2 = CCSprite::createWithTexture(t2);
    girl->addChild(sp2);

    sp2->setBlendFunc(bf);
    CCTexture2DPixelFormat mat = t2->getPixelFormat();
    CCLog("tex format %d  %d", mat, kCCTexture2DPixelFormat_RGBA8888);


    CCLog("t2 contentSize %f %f %d %d", t2->getContentSize().width, t2->getContentSize().height, t2->getPixelsWide(), t2->getPixelsHigh());

    //参照CCTexture2D initWithData 写入数据时候的格式
    //读取数据的时候的格式
    //glPixelStorei(GL_PACK_ALIGNMENT, 4);

    //read girl data
    //glActiveTexture(GL_TEXTURE0);
    //glBindTexture(GL_TEXTURE_2D, t2->getName());

    glPixelStorei(GL_PACK_ALIGNMENT, 4);
    //glPixelStorei(GL_PACK_ROW_LENGTH, t2->getPixelsWide());
    data = (unsigned char *)malloc(4*t2->getPixelsWide()*t2->getPixelsHigh());
    CCLog("assign data");
    GLint inf;
    //glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_COMPONENTS, &inf);
    CCRenderTexture *render = CCRenderTexture::create(t2->getPixelsWide(), t2->getPixelsHigh(), kCCTexture2DPixelFormat_RGBA8888);
    render->begin();
    sp2->draw();
    glReadPixels(0, 0, t2->getPixelsWide(), t2->getPixelsHigh(), GL_RGBA, GL_UNSIGNED_BYTE, data);
    render->end();
    for(int i = 0; i < t2->getPixelsWide(); i++) {
        for(int j = 0; j < t2->getPixelsHigh(); j++) {
            data[j*t2->getPixelsWide()*4+i*4+3] = 255;
        }
    }

    //glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);


    //read circle data
    circle = CCTextureCache::sharedTextureCache()->addImage("circle.png");
    cirData = (unsigned char*)malloc(4*circle->getPixelsWide()*circle->getPixelsHigh());
    CCLog("assign circle");
    transferData = (unsigned char*)malloc(4*circle->getPixelsWide()*circle->getPixelsHigh());
    CCLog("assign transfer data");
    CCSprite *tempCir = CCSprite::createWithTexture(circle);

    //glPixelStorei(GL_PACK_ROW_LENGTH, circle->getPixelsWide());
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, circle->getName());

    render->beginWithClear(0, 0, 0, 0);
    tempCir->draw();
    glReadPixels(0, 0, circle->getPixelsWide(), circle->getPixelsHigh(), GL_RGBA, GL_UNSIGNED_BYTE, cirData);
    render->end();

    //glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, cirData);
    
    setTouchMode(kCCTouchesOneByOne);
    setTouchEnabled(true);
    CCLog("init Hello World Scene");

	addChild(girl);

    CCMenuItemImage *pre = CCMenuItemImage::create("button.png", "buttonOn.png", this, menu_selector(HelloWorld::preOne));
    CCMenuItemImage *next = CCMenuItemImage::create("button.png", "buttonOn.png", this, menu_selector(HelloWorld::nextOne));
    pre->setPosition(ccp(93, 80));



    next->setPosition(ccp(visibleSize.width-93, 80));

    CCLabelTTF *label = CCLabelTTF::create("前一个","Arial", 20);
    pre->addChild(label);
    label->setPosition(ccp(70, 30));

    CCLabelTTF *label2 = CCLabelTTF::create("后一个", "Arial", 20);
    next->addChild(label2);
    label2->setPosition(ccp(70, 30));


    CCMenu *menu = CCMenu::create();
    menu->addChild(pre);
    menu->addChild(next);
    menu->setPosition(ccp(0, 0));
    addChild(menu);
    return true;
}


void HelloWorld::menuCloseCallback(CCObject* pSender)
{
    CCDirector::sharedDirector()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    exit(0);
#endif
}

void HelloWorld::drawPoint(CCTouch *pTouch) {

    CCPoint p = pTouch->getLocation();
    p = sp2->convertToNodeSpace(p);

    CCLog("touchPoint %f %f", p.x, p.y);

    int wide = circle->getPixelsWide();
    //wide -= wide%2;
    int high = circle->getPixelsHigh();
    //high -= high%2;
    CCLog("circle size %d %d", wide, high);

    int dw = t2->getPixelsWide();
    int dh = t2->getPixelsHigh();
    CCLog("data width height %d %d", dw, dh);

    p.x = (std::max)(wide/2, (int)p.x);
	p.x = (std::min)((int)p.x, dw-wide/2);
    p.y = (std::max)(high/2, (int)p.y);
	p.y = (std::min)((int)p.y, dh-high/2);

    int left = p.x-wide/2;
    int right = p.x+wide/2;
    int bottom = p.y-high/2;
    int top = p.y+high/2;

    CCLog("touchBegan %d %d %d %d", left, right, bottom, top);
    
    //绘制防止超出边界
    //半个边缘
    
    for(int i = left ; i < right; i++) {
        for(int j = bottom; j < top; j++) {
            int drow = j;
            //drow = dh-drow;
            int dcol = i;
            int dIndex = drow*dw*4+dcol*4+0;
            int cirIndex = (j-bottom)*circle->getPixelsWide()*4+(i-left)*4;
            int temp = (int)(data[dIndex+3])-(int)(cirData[cirIndex]);
            data[dIndex+3] = (std::max)(temp, 0); 

            /*
            temp = (int)(data[dIndex+0])-(int)(cirData[cirIndex]);
            data[dIndex+0] = (std::max)(temp, 0); 

            temp = (int)(data[dIndex+1])-(int)(cirData[cirIndex]);
            data[dIndex+1] = (std::max)(temp, 0); 

            temp = (int)(data[dIndex+2])-(int)(cirData[cirIndex]);
            data[dIndex+2] = (std::max)(temp, 0); 
            */

            int transIndex = ((high-1)-(j-bottom))*circle->getPixelsWide()*4+(i-left)*4;
            transferData[transIndex+0] = data[dIndex+0];
            transferData[transIndex+1] = data[dIndex+1];
            transferData[transIndex+2] = data[dIndex+2];
            transferData[transIndex+3] = data[dIndex+3];
        }
    }
    
    
    //int offset = bottom*dw*4+left*4;

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, t2->getName());
    //glTexSubImage2D(GL_TEXTURE_2D, 0, left, bottom, wide, high, GL_RGBA, GL_UNSIGNED_BYTE, data+)
    //参考 CCTexture2d 写入数据
    unsigned int bytesPerRow = dw*32/8;
    if(bytesPerRow % 8 == 0)
    {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 8);
    }
    else if(bytesPerRow % 4 == 0)
    {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
    }
    else if(bytesPerRow % 2 == 0)
    {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 2);
    }
    else
    {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    }

    //glPixelStorei(GL_PACK_ROW_LENGTH, wide);
    //glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, dw, dh, GL_RGBA, GL_UNSIGNED_BYTE, data);
    glTexSubImage2D(GL_TEXTURE_2D, 0, left, dh-top, wide, high, GL_RGBA, GL_UNSIGNED_BYTE, transferData);

    //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)dw, (GLsizei)dh, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
}

bool HelloWorld::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent) {
    drawPoint(pTouch);
	return true;
}
void HelloWorld::ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent) {
    drawPoint(pTouch);
}
void HelloWorld::ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent) {
}
