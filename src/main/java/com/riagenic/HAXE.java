package com.riagenic;

/**
 * Created by Scott on 10/10/2015.
 */

import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the associated element as being only available on a certain {@link Side}. This is
 * generally meant for internal Forge and FML use only and should only be used on mod classes
 * when other more common mechanisms, such as using a {@link SidedProxy} fail to work.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface HAXE {
    public Side value();
}
