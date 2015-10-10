package com.riagenic.Events;

import net.minecraft.client.Minecraft;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

/**
 * Created by Scott on 10/10/2015.
 */
public class EventManager {


    private static final EventListenerList listenerList = new EventListenerList();

    public synchronized <T extends Event> void fireEvent(Class<T> type, T event) {

        try {
            if (type == UpdateEvent.class)
                fireUpdate();
            else
                throw new IllegalArgumentException("Invalid event type: " + type.getName());
        } catch (Exception e) {
            handleException(e, this, "processing events", "Event type: " + event.getClass().getSimpleName());
        }
    }

    private void fireUpdate() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == UpdateListener.class)
                ((UpdateListener) listeners[i + 1]).onUpdate();
    }

    public void handleException(final Exception e, final Object cause, final String action, final String comment) {
        if (e.getMessage() != null && e.getMessage().equals("No OpenGL context found in the current thread."))
            return;

        add(UpdateListener.class, new UpdateListener() {
            @Override
            public void onUpdate() {
                System.out.println("ERROR IN EVENTMANAGER");
                // TODO : EventManager - Create custom Bug Screen.
//                Minecraft.getMinecraft().displayGuiScreen(null);
                remove(UpdateListener.class, this);
            }
        });
    }

    public <T extends EventListener> void add(Class<T> type, T listener) {
        listenerList.add(type, listener);
    }

    public <T extends EventListener> void remove(Class<T> type, T listener) {
        listenerList.remove(type, listener);
    }
}
