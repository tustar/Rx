package com.tustar.demo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Sample4_1 {
    public static void main(String[] args) {
        Disposable subscribe = Observable.just(1)
                .doOnSubscribe(disposable ->
                        System.out.println("3=>just :: doOnSubscribe :: " + Thread.currentThread().getName()))
                .doOnNext(disposable ->
                        System.out.println("4=>just :: doOnNext :: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(disposable ->
                        System.out.println("2=>subscribeOn1[Computation] :: doOnSubscribe :: " + Thread.currentThread().getName()))
                .doOnNext(disposable ->
                        System.out.println("5=>subscribeOn1[Computation]:: doOnNext :: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.single())
                .doOnNext(disposable ->
                        System.out.println("6=>observeOn1[Single] :: doOnNext :: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable ->
                        System.out.println("1=>subscribeOn2[IO] :: doOnSubscribe :: " + Thread.currentThread().getName()))
                .doOnNext(disposable ->
                        System.out.println("7=>subscribeOn2[IO] :: doOnNext :: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.newThread())
                .doOnNext(disposable ->
                        System.out.println("8=>observeOn2[New] :: doOnNext :: " + Thread.currentThread().getName()))
                .subscribe(s -> {
                    System.out.println("9=>subscribe :: onNext :: " + Thread.currentThread().getName());
                    System.out.println("10=>subscribe :: onNext :: " + s);
                }, throwable -> {

                }, () -> {

                }, disposable -> {
                });

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
