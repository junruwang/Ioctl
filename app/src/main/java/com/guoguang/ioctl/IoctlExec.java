package com.guoguang.ioctl;

/**
 * Created by 40303 on 2017/5/26.
 */

public class IoctlExec {
    /**
     * 加载动态库
     */
    static {
        System.loadLibrary("ioctljni");
    }

    /**
     * 打开端口
     * 打开后必须关闭
     * @param port 端口地址
     * @return 返回是否打开成功，成功为0，不成功为-1
     */
    private native int openSel(String port);


    /**
     * 关闭端口
     */
    private native int closeSel();

    /**
     * 选择UART进行置位操作
     * @param gpio 选择控制的位置
     * @param state 设置的状态，0为低电平，1为高电平
     * @return 返回置位是否成功，成功为0，不成功为-1
     */
    private native int iocSetData(int gpio,int state);


    /**
     * 获取状态信息
     * @return 返回是否获取成功，成功为0，不成功为-1
     */
    private native int iocGetData();



    public int openPort(String port) {
       return openSel(port);
    }

    public int closePort(){
        return closeSel();
    }

    public int getIocData(){
        return iocGetData();
    }

    /**
     * 置位使灯亮起
     * @return
     */
    public int setLightOn(){
        return iocSetData(2,1);
    }

    /**
     * 置位使灯熄灭
     * @return
     */
    public int setLightOff(){
        return iocSetData(2,0);
    }
}
