package com.tustar.action;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class RxPreLoader<T> {

    private BehaviorSubject<T> mData;
    private Disposable disposable;

    public RxPreLoader(T defaultValue) {
        mData = BehaviorSubject.createDefault(defaultValue);
    }

    public void publish(T object) {
        mData.onNext(object);
    }

    public Disposable subscribe(Consumer onNext) {
        disposable = mData.subscribe(onNext);
        return disposable;
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public BehaviorSubject<T> getCacheDataSubject() {
        return mData;
    }

    public T getLastCacheData() {
        return mData.getValue();
    }
}
