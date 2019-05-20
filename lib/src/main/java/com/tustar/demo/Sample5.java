package com.tustar.demo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Sample5 {
    public static void main(String[] args) {
        Disposable subscribe = Observable.just(1)
                .doOnSubscribe(disposable ->
                        System.out.println("3=>just :: doOnSubscribe :: " + Thread.currentThread().getName()))
                .doOnNext(disposable ->
                        System.out.println("4=>just :: doOnNext :: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(disposable ->
                        System.out.println("2=>subscribeOn[Computation] :: doOnSubscribe :: " + Thread.currentThread().getName()))
                .doOnNext(disposable ->
                        System.out.println("5=>subscribeOn[Computation] :: doOnNext :: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.newThread())
                .doOnNext(disposable ->
                        System.out.println("6=>observeOn[New] :: doOnNext :: " + Thread.currentThread().getName()))
                .map(integer -> {
                    System.out.println("7=>map :: accept ::" + Thread.currentThread().getName());
                    return String.valueOf(integer);
                })
                .doOnNext(disposable ->
                        System.out.println("8=>map :: doOnNext :: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable ->
                        System.out.println("1=>subscribeOn[IO] :: doOnSubscribe ::  " + Thread.currentThread().getName()))
                .doOnNext(disposable ->
                        System.out.println("9=>subscribeOn[IO] :: doOnNext :: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.newThread())
                .doOnNext(disposable ->
                        System.out.println("10=>observeOn[New] :: doOnNext :: " + Thread.currentThread().getName()))
                .subscribe(s -> {
                    System.out.println("11=>subscribe :: onNext :: " + Thread.currentThread().getName());
                    System.out.println("12=>subscribe :: onNext :: " + s);
                }, throwable -> {

                }, () -> {

                }, disposable -> {
                });


        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
