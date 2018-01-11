package com.guoguang.ioctl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IoctlExec ioctlExec;
    private static final String TAG="iocjni";
    private TextView tvShowResult;
    private Spinner gpioList;
    private Button btChmodPort,btOpenSel,btCloseSel,btsetLightOn,btsetLightOff;
    public volatile boolean isOver=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShowResult=(TextView)findViewById(R.id.showResult);
        btChmodPort=(Button)findViewById(R.id.chmodPort);
        btOpenSel=(Button)findViewById(R.id.openPort);
        btCloseSel=(Button)findViewById(R.id.closePort);
        btsetLightOn=(Button)findViewById(R.id.setLightOn);
        btsetLightOff=(Button)findViewById(R.id.setLightOff);
        gpioList=(Spinner)findViewById(R.id.gpioList);

        btOpenSel.setOnClickListener(this);
        btCloseSel.setOnClickListener(this);
        btsetLightOn.setOnClickListener(this);
        btsetLightOff.setOnClickListener(this);
        btChmodPort.setOnClickListener(this);


        String[] str={"电源灯1-gpio0","电源灯2-gpio1","蓝牙-gpio2","USB电源-gpio3","升压使能-gpio4"};
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,str);
        gpioList.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        if(ioctlExec==null){
            ioctlExec=new IoctlExec();
        }
        String str="";
        String gpio="";
        final int flag;
        switch (v.getId()){
            case R.id.chmodPort:
                isOver=false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!isOver){
                            ExecShellCmd.exec("chmod 777 /dev/ghgpiosel");
                            isOver=true;
                        }
                    }
                }).start();
                break;
            case R.id.openPort:
                flag=ioctlExec.openPort("/dev/ghgpiosel");
                if(flag==0){
                    str="打开端口成功";
                }else{
                    str="打开端口失败";
                }
                break;
            case R.id.setLightOn:
                gpio=gpioList.getSelectedItem().toString();
                flag=ioctlExec.setLightOn(Integer.parseInt(gpio.substring(gpio.length()-1)));

                if(flag==0){
                    str="设置灯亮成功";
                }else if(flag==-1){
                    str="设置灯亮失败";
                }else {
                    str="请输入正确的gpio口";
                }
                break;
            case R.id.setLightOff:
                gpio=gpioList.getSelectedItem().toString();
                flag=ioctlExec.setLightOff(Integer.parseInt(gpio.substring(gpio.length()-1)));
                if(flag==0){
                    str="设置灯灭成功";
                }else if(flag==-1){
                    str="设置灯灭失败";
                }else {
                    str="请输入正确的gpio口";
                }
                break;
            case R.id.closePort:
                flag=ioctlExec.closePort();
                if(flag==0){
                    str="关闭端口成功";
                }else{
                    str="端口状态为关闭，无需关闭";
                }
                break;
        }
        tvShowResult.setText(str);
    }
}
