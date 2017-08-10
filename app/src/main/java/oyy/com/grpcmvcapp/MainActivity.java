package oyy.com.grpcmvcapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.grpc.examples.helloworld.HelloResponse;
import oyy.com.grpcmvcapp.grpc.CallBack;
import oyy.com.grpcmvcapp.grpc.DataManage;
import oyy.com.grpcmvcapp.grpc.GrpcAsyncTask;

public class MainActivity extends BaseActivity {

    private Button startBtn;

    private CallBack callBack;

    private GrpcAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCallBack();
        startBtn = (Button) findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GrpcAsyncTask.isFinish(task)) {
                    showWaitingDialog();
                    task = DataManage.getInstance().SayHello("大家好", callBack);
                }
            }
        });
    }

    public void initCallBack() {
        callBack = new CallBack() {
            @Override
            public void callBack(Object result) {
                cancelWaitingDialog();
                if (result == null) {
                    //报这个异常的，io.grpc.StatusRuntimeException: UNAVAILABLE: Name resolution failed
                    Toast.makeText(MainActivity.this, "老铁们请在 gradle中配置有效的端口号和地址", Toast.LENGTH_LONG).show();
                    return;
                }
                HelloResponse response = (HelloResponse) result;
                //处理数据,刷新UI
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancelTask(true);
            task = null;
        }
    }
}
