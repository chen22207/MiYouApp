package com.firstblood.miyo.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by chenshuai12619 on 2016/4/7 16:24.
 */
public class RxBus {


	private static volatile RxBus instance;
	private final Subject bus;

    public static RxBus getInstance() {
	    RxBus rxBus = instance;
	    if (instance == null) {
		    synchronized (RxBus.class) {
			    rxBus = instance;
			    if (instance == null) {
				    rxBus = new RxBus();
				    instance = rxBus;
			    }
		    }
	    }
	    return instance;
    }

	private RxBus() {
		bus = new SerializedSubject<>(PublishSubject.create());
	}

	public void send(Object o) {
		bus.onNext(o);
	}

	public <T> Observable<T> toObserverable(Class<T> type) {
		return bus.ofType(type);
	}

	public boolean hasObservers() {
		return bus.hasObservers();
	}
}
