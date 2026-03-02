/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.api.events.eventbus;

import dev.luminous.api.events.eventbus.ICancellable;
import dev.luminous.api.events.eventbus.IListener;
import dev.luminous.api.events.eventbus.LambdaListener;

public interface IEventBus {
    public void registerLambdaFactory(LambdaListener.Factory var1);

    public <T> T post(T var1);

    public <T extends ICancellable> T post(T var1);

    public void subscribe(Object var1);

    public void subscribe(Class<?> var1);

    public void subscribe(IListener var1);

    public void unsubscribe(Object var1);

    public void unsubscribe(Class<?> var1);

    public void unsubscribe(IListener var1);
}

