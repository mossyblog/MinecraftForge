package com.riagenic;

import com.riagenic.Events.EventManager;

/**
 * Created by Scott on 10/10/2015.
 */
public enum MossyClient {

    INSTANCE;
    public static final String VERSION = "1.8.1.0";
    public EventManager eventManager;


    public void startClient() {
        eventManager = new EventManager();
    }
}
