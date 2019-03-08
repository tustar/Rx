package com.tustar.action;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class Ch9 {

    public static ObservableTransformer<Integer, String>transformer() {
        return upstream -> upstream.map((Function<Integer, String>) integer -> {
            return String.valueOf(integer);
        });
    }
}
