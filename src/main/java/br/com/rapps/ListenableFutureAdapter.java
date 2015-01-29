package br.com.rapps;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author erico
 */
public class ListenableFutureAdapter<T> extends DeferredResult<T> {

    public ListenableFutureAdapter(final ListenableFuture<T> target) {
        Futures.addCallback(target, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                setResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                setErrorResult(t);
            }
        });
    }
}
