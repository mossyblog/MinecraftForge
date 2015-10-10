package com.riagenic.Events;

/**
 * Created by Scott on 10/10/2015.
 */
public class UpdateEvent extends Event
{
    @Override
    public String getAction()
    {
        return "updating";
    }
}