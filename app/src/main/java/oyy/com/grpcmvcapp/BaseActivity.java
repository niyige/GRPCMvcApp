package oyy.com.grpcmvcapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }


    public void showWaitingDialog() {
    }


    public void cancelWaitingDialog() {

    }


    @Override
    public void finish() {
        super.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelWaitingDialog();
    }


}
