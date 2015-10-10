package net.minecraft.util;

public class TupleIntJsonSerializable
{
    private int integerValue;
    private IJsonSerializable jsonSerializableValue;
    private static final String __OBFID = "CL_00001478";

    public int getIntegerValue()
    {
        return this.integerValue;
    }

    public void setIntegerValue(int integerValueIn)
    {
        this.integerValue = integerValueIn;
    }

    public IJsonSerializable getJsonSerializableValue()
    {
        return this.jsonSerializableValue;
    }

    public void setJsonSerializableValue(IJsonSerializable jsonSerializableValueIn)
    {
        this.jsonSerializableValue = jsonSerializableValueIn;
    }
}