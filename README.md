# GRPCMvcApp
一个用MVC模式封装的GRPC网络调用
# 以下是重要代码：
1  Activity里面 初始化callback

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
    
 2  调用获取数据的方法
 DataManage.getInstance().SayHello("xxxx", callBack);  
 
 SayHello详细方法：
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
 
 
 3  callback的调用时机：
    
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
 
 4 结束。



