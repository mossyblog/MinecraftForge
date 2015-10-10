package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator
{
    private static final String __OBFID = "CL_00002318";

    public static Map createMap(Iterable keys, Iterable values)
    {
        return populateMap(keys, values, Maps.newLinkedHashMap());
    }

    public static Map populateMap(Iterable keys, Iterable values, Map map)
    {
        Iterator iterator = values.iterator();
        Iterator iterator1 = keys.iterator();

        while (iterator1.hasNext())
        {
            Object object = iterator1.next();
            map.put(object, iterator.next());
        }

        if (iterator.hasNext())
        {
            throw new NoSuchElementException();
        }
        else
        {
            return map;
        }
    }
}