/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.SendMessageEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.misc.AutoReconnect;
import dev.luminous.mod.modules.settings.impl.StringSetting;

public class ChatAppend
extends Module {
    public static ChatAppend INSTANCE;
    private final StringSetting message = this.add(new StringSetting("Text", "Alien"));

    public ChatAppend() {
        super("ChatAppend", Module.Category.Misc);
        this.setChinese("\u6d88\u606f\u540e\u7f00");
        INSTANCE = this;
    }

    @EventListener
    public void onSendMessage(SendMessageEvent event) {
        Object message;
        if (!(ChatAppend.nullCheck() || event.isCancelled() || AutoReconnect.inQueueServer || ((String)(message = event.message)).startsWith("/") || ((String)message).startsWith("!") || ((String)message).startsWith("$") || ((String)message).startsWith("#") || ((String)message).endsWith(this.message.getValue()))) {
            String suffix = this.message.getValue();
            event.message = message = (String)message + " " + suffix;
        }
    }
}

