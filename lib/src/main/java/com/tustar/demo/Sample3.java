package com.tustar.demo;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Sample3 {
    public static void main(String[] args) {
        Observable.just(1)
                .map(integer -> String.valueOf(integer))
                .doOnNext(s -> System.out.println("doOnNext :: " + s))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(s -> System.out.println("onNext :: " + s));

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
