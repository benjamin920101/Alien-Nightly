/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.api.events;

import dev.luminous.api.events.eventbus.Cancelable;

public class Event {
    public Stage stage;
    private boolean cancel = false;
    private final boolean cancelable = this.getClass().isAnnotationPresent(Cancelable.class);
    private boolean canceled;

    public Event() {
        this(Stage.Pre);
    }

    public Event(Stage stage) {
        this.stage = stage;
    }

    public void cancel() {
        this.setCancelled(true);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isPost() {
        return this.stage == Stage.Post;
    }

    public boolean isPre() {
        return this.stage == Stage.Pre;
    }

    public void setCanceled(boolean cancel) {
        if (this.isCancelable()) {
            this.canceled = cancel;
            return;
        }
        throw new IllegalStateException("Cannot set event canceled");
    }

    public boolean isCancelable() {
        return this.cancelable;
    }

    public static enum Stage {
        Pre,
        Post;

    }
}

