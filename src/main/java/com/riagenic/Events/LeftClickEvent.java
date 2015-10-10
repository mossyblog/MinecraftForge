package com.riagenic.Events;

/**
 * Created by Scott on 10/10/2015.
 */
public class LeftClickEvent extends Event {
    @Override
    public String getAction() {
        return "left-clicking";
    }
}