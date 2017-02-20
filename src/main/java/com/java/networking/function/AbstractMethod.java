package com.java.networking.function;

import com.java.networking.api.NetworkMethod;
import com.java.networking.main.MainUI;
import java.io.InputStream;

public abstract class AbstractMethod implements NetworkMethod {

	protected final int loss;
	protected final MainUI f;

	public AbstractMethod(int loss, MainUI f) {
		this.loss = loss;
		this.f = f;
	}

	@Override
	public abstract void start(String host, int port, InputStream inputStream);

}
