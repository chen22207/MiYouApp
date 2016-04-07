package com.firstblood.miyo.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by chenshuai12619 on 2016/4/7 16:24.
 */
public class RxBus {

    private RxBus() {
    }

    private static RxBus instance;

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

	private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

	public void send(Object o) {
		bus.onNext(o);
	}

	public Observable<Object> toObserverable() {
		return bus;
	}

	public boolean hasObservers() {
		return bus.hasObservers();
	}
}