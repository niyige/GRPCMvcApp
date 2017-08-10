package oyy.com.grpcmvcapp.grpc;

/**
 * 通信回调接口
 *
 * @param <Data>
 * @author ouyangyi
 */
public abstract class CallBack<Data> {

    /**
     * 回调接口
     *
     * @param result 返回的数据
     */
    public abstract void callBack(Data result);  //refresh view


}
