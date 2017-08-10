package oyy.com.grpcmvcapp.grpc;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.examples.helloworld.HelloResponse;

/**
 * Created by
 * ouyangyi on 17/8/10.
 */

public class DataManage {

    public static DataManage dataManage;

    private DataManage() {

    }

    public static DataManage getInstance() {
        if (dataManage == null) {
            dataManage = new DataManage();
        }
        return dataManage;
    }

    /**
     * 获取stub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public GreeterGrpc.GreeterBlockingStub getStub(ManagedChannel channel) {
        return GreeterGrpc.newBlockingStub(channel)
                .withDeadlineAfter(8, TimeUnit.SECONDS);

    }

    /**
     * 测试方法
     *
     * @param mMessage
     * @return
     */
    public GrpcAsyncTask<String, Void, HelloResponse> SayHello(final String mMessage, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, HelloResponse>(callBack) {
            @Override
            protected HelloResponse doRequestData(ManagedChannel channel) {
                HelloRequest message = HelloRequest.newBuilder()
                        .setName(mMessage)
                        .build();
                HelloResponse response = null;
                try {
                    response = getStub(channel).sayHello(message);
                }catch (Exception e) {
                    Log.i("DataManage", e.getMessage());
                }

                return response;
            }
        }.doExecute();
    }
}
