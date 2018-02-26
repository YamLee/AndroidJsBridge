package me.yamlee.jsbridge.jscall;

import android.content.Context;

import java.io.IOException;

import me.yamlee.jsbridge.BaseJsCallProcessor;
import me.yamlee.jsbridge.HybridOkHttpManager;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.NativeComponentProvider;
import me.yamlee.jsbridge.WVJBWebViewClient;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * H5调用原生网络请求
 *
 * @author yamlee
 * @version 1.0
 */
public class HttpRequestProcessor extends BaseJsCallProcessor {

    public static final String FUNC_NAME = "httpRequest";
    private Context context;

    public HttpRequestProcessor(NativeComponentProvider componentProvider) {
        super(componentProvider);
    }

    @Override
    public boolean onHandleJsQuest(JsCallData callData) {
        if (FUNC_NAME.equals(callData.getFunc())) {
            String params = callData.getParams();
            HybridHttpRequest request = convertJsonToObject(params, HybridHttpRequest.class);
            context = componentProvider.provideApplicationContext();
            doRequest(context, request);
            return true;
        }
        return false;
    }

    public Observable<String> doRequest(final Context context,
                                        final HttpRequestProcessor.HybridHttpRequest request) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String data = null;
                try {
                    data = HybridOkHttpManager.doRequest(context, request);
                } catch (IOException e) {
                    Timber.e(e);
                }
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public boolean onResponse(WVJBWebViewClient.WVJBResponseCallback callback) {
        return super.onResponse(callback);
    }

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }

    public static class HybridHttpRequest {
        public interface Method {
            String GET = "GET";
            String POST = "POST";
        }

        public String method;
        public String url;
        public String jsonParams;
    }
}
