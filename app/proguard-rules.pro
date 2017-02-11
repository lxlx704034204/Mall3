# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
 ##记录生成的日志数据,gradle build时在本项目根目录输出##

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

-keepattributes SourceFile,LineNumberTable
-keep class com.hxqc.mall.application.SampleApplicationLike
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keep class vi.com.gdi.bgl.android.**{*;}

-keep public class com.google.gson.Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keep class com.loopj.**
-keep class org.apache.**
-keep class com.nostra13.**
-keep class roboguice.** { *; }

#
-keep class com.google.inject.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
#
-keep @com.google.inject.Singleton public class *
-keepattributes *Annotation*
-keepattributes Signature

-dontwarn android.support.**
-dontwarn com.loopj.**
-dontwarn com.google.**
-dontwarn org.apache.**
-dontwarn com.nostra13.**
-dontwarn roboguice.**
-dontwarn org.roboguice.**
-dontwarn com.j256.**
-dontwarn hxqc.com.emojicon.**

-keepclassmembers class ** {
    public void onEvent*(**);
}

-keep public class roboguice.**

#-------------------React Native-------------------------------
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
#-dontobfuscate
-keep,allowobfuscation @interface com.facebook.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.proguard.annotations.KeepGettersAndSetters
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.proguard.annotations.DoNotStrip class *
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.proguard.annotations.DoNotStrip *;
    @com.facebook.common.internal.DoNotStrip *;
}

-keepclassmembers @com.facebook.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}

-keep class * extends com.facebook.react.bridge.JavaScriptModule { *; }
-keep class * extends com.facebook.react.bridge.NativeModule { *; }
#keepclasseswithmembernames
-keepclassmembers,includedescriptorclasses class * { native <methods>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.UIProp <fields>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactProp <methods>; }
-keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactPropGroup <methods>; }

-dontwarn com.facebook.react.**
# okhttp

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# okio

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
# stetho
-dontwarn com.facebook.stetho.**
-keep class com.facebook.imagepipeline.gif.** { *; }
-keep class com.facebook.imagepipeline.webp.** { *; }
#----------------------------------------------------------
-keep public class * implements java.io.Serializable {*;}
#We are not done yet, add the following as well.
-keepclassmembers class * {
    public <init>(android.content.Context);
}

# keep Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-dontshrink
-keep,allowshrinking class com.umeng.message.* {
        public <fields>;
        public <methods>;
}
-keep,allowshrinking class com.umeng.message.protobuffer.MessageResponse$PushResponse$Info {
        public <fields>;
        public <methods>;
}
-keep,allowshrinking class com.umeng.message.protobuffer.MessageResponse$PushResponse$Info$Builder {
        public <fields>;
        public <methods>;
}
-keep,allowshrinking class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}
-keep,allowshrinking class org.android.agoo.service.* {*;}
-keep,allowshrinking class org.android.spdy.**{*;}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers class de.greenrobot.event.util.ThrowableFailureEvent

#讯飞语音
-keep class com.iflytek.**{*;}

 #3D 地图
-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
    #定位
-keep   class com.amap.api.location.**{*;}
-keep   class com.amap.api.fence.**{*;}
-keep   class com.aps.**{*;}
-keep   class com.autonavi.aps.amapapi.model.**{*;}
   # 搜索
-keep   class com.amap.api.services.**{*;}
   # 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
  #  导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
-dontwarn com.amap.api.*
-dontwarn com.amap.navi.*
-dontwarn com.amap.api.navi.*
-dontwarn com.amap.api.mapcore2d.*
  #3D 地图
-keep   class com.amap.api.trace.**{*;}



#square
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {    native <methods>;}

#nineoldandroids动画lib包
-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}

#daimajia
-dontwarn com.daimajia.*
-keep class com.daimajia.** {*;}

#-------------------友盟分享----------------------
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }
-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
#-------------------友盟分享----------------------

#-------------------微信-------------------------
-keep class com.tencent.mm.sdk.** {
   *;
}

#------------------支付宝----------------------
-ignorewarnings
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.** { *;}
-keep class com.ut.** { *;}
-keep class com.alipay.** { *;}
-dontwarn com.alipay.*
-keep class com.taobao.** { *;}
-dontwarn com.taobao.*
-keep class com.taobao.dp.** { *;}
-dontwarn com.taobao.dp.*
-keep class com.alipay.apmobilesecuritysdk.** { *;}
-dontwarn com.alipay.apmobilesecuritysdk.*
-dontwarn com.alipay.android.phone.**

-dontwarn java.lang.invoke**
-dontwarn org.apache.lang.**
-dontwarn org.apache.commons.**
-dontwarn com.nhaarman.**
-dontwarn se.emilsjolander.**
-dontwarn uk.co.senab.photoview.**
#------------------支付宝----------------------

#------------------小能-----------------------
-keep class cn.xiaoneng.** { *; }
-keep class edu.emory.** { *; }
-keep class net.sf.retrotranslator.** { *; }
-keep class org.fusesource.** { *; }
-keep class org.fusesource.** { *; }
-dontwarn cn.xiaoneng.**
-dontwarn edu.emory.**
-dontwarn net.sf.retrotranslator.**
-dontwarn org.fusesource.**
#------------------小能-----------------------

-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontoptimize
-optimizations optimization_filter


-keep class de.greenrobot.event.util.ThrowableFailureEvent.** { *;}

-keep class org.apache.http.** { *;}

#------------------银联-----------------------
-keep class org.simalliance.openmobileapi.** {*;}
-keep class org.simalliance.openmobileapi.service.** {*;}
-keep class com.unionpay.** {*;}
-dontwarn  com.unionpay.**
-dontwarn  org.simalliance.**
#------------------银联-----------------------

#图表 https://github.com/PhilJay/MPAndroidChart/wiki/Proguard
-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**

#------------------eventBus-----------------------
#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#------------------eventBus-----------------------

#------------------rxjava-----------------------
#rxjava
-dontwarn rx.**

#1
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

#2
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
 }

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
 }

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

#------------------rxjava-----------------------

#------------------dbflow-----------------------
-keep class com.raizlabs.** { *; }
-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
#------------------dbflow-----------------------

#------------------滴滴-----------------------
-keepnames class com.sdu.didi.openapi.utils.Utils{}
-keep public class com.sdu.didi.openapi.Methods{}
-keepclassmembers class com.sdu.didi.openapi.Methods{
public java.lang.String *(java.lang.String);}
-keepclassmembers class com.sdu.didi.openapi.utils.Utils{
public static java.lang.String getTimestamp();
public static java.lang.String getRandomString(int);}
-keep public class com.sdu.didi.uuid.ed{*;}
-keep public class com.sdu.didi.uuid.SigLib{*;}
#------------------滴滴-----------------------
#------------------恒信-----------------------
-keep class com.hxqc.tinker.model.**{*;}
-keep class com.hxqc.mall.core.model.**{*;}
-keep class com.hxqc.pay.model.**{*;}
-keep class com.hxqc.mall.launch.model.**{*;}

-keep class com.hxqc.mall.qr.model.**{*;}
-keep class com.hxqc.socialshare.pojo.**{*;}
-keep class com.hxqc.mall.comment.model.**{*;}
-keep class com.hxqc.mall.thirdshop.model.**{*;}
-keep class com.hxqc.mall.paymethodlibrary.model.**{*;}
-keep class com.hxqc.mall.thirdshop.accessory.model.**{*;}
-keep class com.hxqc.mall.thirdshop.maintenance.model.**{*;}
-keep class com.hxqc.mall.usedcar.model.**{*;}
-keep class com.hxqc.newenergy.bean.**{*;}
-keep class com.hxqc.mall.auto.model.**{*;}
-keep class com.hxqc.aroundservice.model.**{*;}
-keep class com.hxqc.mall.payment.model.**{*;}
-keep class com.hxqc.fastreqair.model.**{*;}
-keep class com.hxqc.mall.reactnative.model.**{*;}
-keep class com.hxqc.mall.thirdshop.accessory4s.model.**{*;}
-keep class com.hxqc.autonews.model.**{*;}
-keep class com.hxqc.mall.config.router.RouteResponse{*;}
-keep class com.hxqc.mall.config.router.RouteModule{*;}
-keep class com.hxqc.mall.config.update.VersionPo{*;}
-keep class com.hxqc.mall.config.push.PushCustomMessage{*;}
-keep class com.hxqc.autonews.model.**{*;}
-keep class com.hxqc.mall.interfaces.**{*;}
-keep class com.hxqc.carcompare.model.**{*;}

-keep class WebYeepayInterface
-keep class com.hxqc.mall.thirdshop.views.InfoIntroduceView
-keep class com.hxqc.mall.thirdshop.control.**{*;}
-keep class com.hxqc.mall.amap.**{*;}
-keep class com.hxqc.mall.thirdshop.activity.ThirdShopAMapActivity
-keep class com.hxqc.mall.activity.AMapAutoSaleActivity
-keep class com.hxqc.newenergy.activity.Ev_NewsWebActivity
-keep class com.hxqc.newenergy.inter.EVWikiJSInterface
-keep class com.hxqc.mall.thirdshop.maintenance.views.MaintenanceHTMLView
-keep class com.hxqc.mall.thirdshop.maintenance.inter.TPSMaintenancePJSInterface
-keep class com.hxqc.mall.push.RouterModule

-keep class com.hxqc.mall.core.db.**{*;}
-keep class com.hxqc.mall.drivingexam.db.model.**{*;}
-keep class com.hxqc.mall.drivingexam.model.**{*;}
-keep class com.hxqc.mall.amap.*

#------------------恒信-----------------------
#------------------友盟推送-------------
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}
#------------------友盟推送-------------
#---------------------蒲公英------------------

-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }
#---------------------蒲公英------------------
# android v4 v7扩展包
-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }

-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

-keep class com.umeng.message.protobuffer.* {
	 public <fields>;
         public <methods>;
}

-keep class com.umeng.message.* {
	 public <fields>;
         public <methods>;
}

-keep class org.android.agoo.impl.* {
	 public <fields>;
         public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}
#-----------------------Glide---------------
#https://github.com/bumptech/glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#----------------------Glide-----------------
#---------------切图
#https://github.com/Yalantis/uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#---------------切图
-keep class com.google.android.cameraview.**{*;}
#------------------
#https://github.com/Raizlabs/DBFlow/blob/ad83cbacc0d945d5bae93bb05127804f83d22ab8/usage2/SQLCipherSupport.md
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**
#--------------------