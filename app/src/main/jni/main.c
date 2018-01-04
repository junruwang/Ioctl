
#include <jni.h>

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <syslog.h>
#include <android/log.h>

#include <sys/resource.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/inotify.h>
#include <sys/limits.h>
#include <sys/poll.h>
#include<fcntl.h>
#include <errno.h>

static const char *TAG="TestJni";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define GPIOSEL_IOC_MAGIC  'g'

#define GPIOSEL_IOCPRINT   		_IO(GPIOSEL_IOC_MAGIC, 1)
#define GPIOSEL_IOCGETDATA  	_IOR(GPIOSEL_IOC_MAGIC, 2, int)
#define GPIOSEL_IOCSETDATA  	_IOW(GPIOSEL_IOC_MAGIC, 3, int)

#define GPIOSEL_IOC_MAXNR   3

#define UART_GPIO_SEL_MAJOR  156

 int fd =0;
 int cmd;
 int arg=0;

 struct user_gpio{
      int gpio;
      int state;
 };

 /**
  *打开端口
  *port 为端口号
  *返回打开状态，0为打开成功，-1为打开失败
  *同一时间只允许打开一个端口号
  */
 JNIEXPORT jint JNICALL Java_com_work_testjni_IoctlControl_openSel
   (JNIEnv *env, jobject obj, jstring port){
         jint flag=-1;
         const char * str = (*env)->GetStringUTFChars(env,port,0);
         if(fd<=0){
            fd = open(str,O_RDWR);
         }
          LOGD("\niocjni fd is %d",fd);
         if(fd>0){
             LOGD("iocjni succeed");
             flag=0;
         }else{
            LOGD("\niocjni Open Dev Mem0 Error!");
            flag=-1;
         }
         (*env)->ReleaseStringChars(env,port,str);
         return flag;
  };

 /**
  *关闭端口
  */
 JNIEXPORT void JNICALL Java_com_work_testjni_IoctlControl_closeSel
   (JNIEnv *env, jobject obj){
        if(fd>0){
          close(fd);
        }
  };

 /**
  *ioctl操作,选择UART
  *port，start设置UART状态，（0,0）、（0,1）、（1,0）、（1、1）
  *返回设置状态，置位成功则返回0，不成功返回-1
  */
 JNIEXPORT jint JNICALL Java_com_work_testjni_IoctlControl_iocSetData
   (JNIEnv *env, jobject obj, jint port, jint state){
        jint flag=-1;
        cmd = GPIOSEL_IOCSETDATA;
        struct user_gpio gpio_data;
        gpio_data.gpio=port;
        gpio_data.state=state;

        if (ioctl(fd, cmd, &gpio_data) < 0)
            {
               flag=-1;
               LOGD("\n iocjni Call cmd MEMDEV_IOCSETDATA fail");
        }
        else{
            flag=0;
            LOGD("\n iocjni Call cmd MEMDEV_IOCSETDATA success! ");
        }
        return flag;
  };

 /**
  *Ioctl操作，获取UART信息
  *返回获取状态，成功为0，不成功为-1
  */
 JNIEXPORT jint JNICALL Java_com_work_testjni_IoctlControl_iocGetData
   (JNIEnv *env, jobject obj){
          jint flag=-1;
          cmd = GPIOSEL_IOCGETDATA;

          if (ioctl(fd, cmd, &arg) < 0)
              {
                  flag=-1;
                  LOGD("\n iocjni Call cmd MEMDEV_IOCGETDATA fail ");
          }else{
                flag=0;
                LOGD("<--- In User Space MEMDEV_IOCGETDATA Get Data is %d --->\n\n",arg);
          }
          return flag;
 };





