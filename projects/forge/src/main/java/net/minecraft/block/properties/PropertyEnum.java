package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.util.IStringSerializable;

public class PropertyEnum extends PropertyHelper
{
    private final ImmutableSet allowedValues;
    private final Map nameToValue = Maps.newHashMap();
    private static final String __OBFID = "CL_00002015";

    protected PropertyEnum(String name, Class valueClass, Collection allowedValues)
    {
        super(name, valueClass);
        this.allowedValues = ImmutableSet.copyOf(allowedValues);
        Iterator iterator = allowedValues.iterator();

        while (iterator.hasNext())
        {
            Enum oenum = (Enum)iterator.next();
            String s1 = ((IStringSerializable)oenum).getName();

            if (this.nameToValue.containsKey(s1))
            {
                throw new IllegalArgumentException("Multiple values have the same name \'" + s1 + "\'");
            }

            this.nameToValue.put(s1, oenum);
        }
    }

    public Collection getAllowedValues()
    {
        return this.allowedValues;
    }

    public String getName(Enum value)
    {
        return ((IStringSerializable)value).getName();
    }

    public static PropertyEnum create(String name, Class clazz)
    {
        return create(name, clazz, Predicates.alwaysTrue());
    }

    public static PropertyEnum create(String name, Class clazz, Predicate filter)
    {
        return create(name, clazz, Collections2.filter(Lists.newArrayList(clazz.getEnumConstants()), filter));
    }

    public static PropertyEnum create(String name, Class clazz, Enum ... values)
    {
        return create(name, clazz, Lists.newArrayList(values));
    }

    public static PropertyEnum create(String name, Class clazz, Collection values)
    {
        return new PropertyEnum(name, clazz, values);
    }

    public String getName(Comparable value)
    {
        return this.getName((Enum)value);
    }
}