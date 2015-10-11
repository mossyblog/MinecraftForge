package com.riagenic.Events;

import com.riagenic.Utils.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Created by Scott on 10/10/2015.
 */
public class EntityWithinRangeEvent extends EntityEvent {

    public EntityWithinRangeEvent(Entity entity) {
        super(entity);
    }
}
