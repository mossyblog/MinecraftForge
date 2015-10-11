package com.riagenic.Events;

import net.minecraftforge.fml.common.eventhandler.*;

/**
 * Created by Scott on 10/10/2015.
 */
@Cancelable
public class ChatOutputEvent extends Event
{
    private String message;
    private boolean automatic;


    public ChatOutputEvent(String message, boolean automatic)
    {
        this.message = message;
        this.automatic = automatic;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isAutomatic()
    {
        return automatic;
    }
}
