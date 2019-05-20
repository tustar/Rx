package com.tustar.demo;

import io.reactivex.Observable;

public class Sample2 {
    public static void main(String[] args) {
        // Lambda表达式简洁版本
        Observable.just(1)
                .map(integer -> String.valueOf(integer))
                .subscribe(s -> System.out.println("onNext :: " + s));
    }
}
