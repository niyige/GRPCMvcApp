package oyy.com.grpcmvcapp.grpc;

import android.os.AsyncTask;
import android.os.Build;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import oyy.com.grpcmvcapp.BuildConfig;

/**
 * 异步请求封装
 * Created by
 * ouyangyi on 17/8/10.
 */

public abstract class GrpcAsyncTask<Params, Progress, Data> extends AsyncTask<Params, Progress, Data> {

    private ManagedChannel mChannel;

    private WeakReference<CallBack<Data>> callBack;

    public GrpcAsyncTask() {
    }

    public GrpcAsyncTask(CallBack<Data> callBack) {
        this.callBack = new WeakReference<>(callBack);
    }


    @Override
    protected void onPreExecute() {
        //做一些前提工作
    }

    /**
     * 异步操作
     *
     * @param channel
     * @return
     */
    protected abstract Data doRequestData(ManagedChannel channel);

    @Override
    protected Data doInBackground(Params... params) {
        try {
            mChannel = getChannel();
            return doRequestData(mChannel);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Data result) {
        if (callBack != null) {
            CallBack<Data> c = callBack.get();
            if (c != null) {
                c.callBack(result);
            }
        }
        try {
            if (mChannel != null) {
                mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public GrpcAsyncTask<Params, Progress, Data> doExecute(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            execute(params);
        }
        return this;
    }

    /**
     * 请求是否结束
     *
     * @param task
     * @return
     */
    public static boolean isFinish(AsyncTask<?, ?, ?> task) {
        return task == null || Status.FINISHED.equals(task.getStatus());
    }

    /**
     * 取消任务
     *
     * @param mayInterruptIfRunning
     * @return
     */
    public boolean cancelTask(boolean mayInterruptIfRunning) {
        boolean cancel = cancel(mayInterruptIfRunning);
        callBack = null;
        return cancel;
    }


    /**
     * 获取唯一的连接
     *
     * @return
     */
    public static ManagedChannel getChannel() {
        //  if (mChannel == null || mChannel.isShutdown() || mChannel.isTerminated()) {
        ManagedChannel mChannel = ManagedChannelBuilder.forAddress(BuildConfig.BASE_HOST, Integer.parseInt(BuildConfig.IP_PORT))
                .usePlaintext(true)
                .build();
        //  }
        return mChannel;
    }


}
