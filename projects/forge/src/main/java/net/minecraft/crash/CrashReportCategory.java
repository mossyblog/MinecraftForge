package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class CrashReportCategory
{
    private final CrashReport crashReport;
    private final String name;
    private final List children = Lists.newArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];
    private static final String __OBFID = "CL_00001409";

    public CrashReportCategory(CrashReport report, String name)
    {
        this.crashReport = report;
        this.name = name;
    }

    public static String getCoordinateInfo(double x, double y, double z)
    {
        return String.format("%.2f,%.2f,%.2f - %s", new Object[] {Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), getCoordinateInfo(new BlockPos(x, y, z))});
    }

    public static String getCoordinateInfo(BlockPos pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        StringBuilder stringbuilder = new StringBuilder();

        try
        {
            stringbuilder.append(String.format("World: (%d,%d,%d)", new Object[] {Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k)}));
        }
        catch (Throwable throwable2)
        {
            stringbuilder.append("(Error finding world loc)");
        }

        stringbuilder.append(", ");
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;

        try
        {
            l = i >> 4;
            i1 = k >> 4;
            j1 = i & 15;
            k1 = j >> 4;
            l1 = k & 15;
            i2 = l << 4;
            j2 = i1 << 4;
            k2 = (l + 1 << 4) - 1;
            l2 = (i1 + 1 << 4) - 1;
            stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] {Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2)}));
        }
        catch (Throwable throwable1)
        {
            stringbuilder.append("(Error finding chunk loc)");
        }

        stringbuilder.append(", ");

        try
        {
            l = i >> 9;
            i1 = k >> 9;
            j1 = l << 5;
            k1 = i1 << 5;
            l1 = (l + 1 << 5) - 1;
            i2 = (i1 + 1 << 5) - 1;
            j2 = l << 9;
            k2 = i1 << 9;
            l2 = (l + 1 << 9) - 1;
            int i3 = (i1 + 1 << 9) - 1;
            stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] {Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2), Integer.valueOf(i3)}));
        }
        catch (Throwable throwable)
        {
            stringbuilder.append("(Error finding world loc)");
        }

        return stringbuilder.toString();
    }

    public void addCrashSectionCallable(String sectionName, Callable callable)
    {
        try
        {
            this.addCrashSection(sectionName, callable.call());
        }
        catch (Throwable throwable)
        {
            this.addCrashSectionThrowable(sectionName, throwable);
        }
    }

    public void addCrashSection(String sectionName, Object value)
    {
        this.children.add(new CrashReportCategory.Entry(sectionName, value));
    }

    public void addCrashSectionThrowable(String sectionName, Throwable throwable)
    {
        this.addCrashSection(sectionName, throwable);
    }

    public int getPrunedStackTrace(int size)
    {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();

        if (astacktraceelement.length <= 0)
        {
            return 0;
        }
        else
        {
            int len = astacktraceelement.length - 3 - size;
            // Really Mojang, Still, god damn...
            if (len <= 0) len = astacktraceelement.length;
            this.stackTrace = new StackTraceElement[len];
            System.arraycopy(astacktraceelement, astacktraceelement.length - len, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2)
    {
        if (this.stackTrace.length != 0 && s1 != null)
        {
            StackTraceElement stacktraceelement2 = this.stackTrace[0];

            if (stacktraceelement2.isNativeMethod() == s1.isNativeMethod() && stacktraceelement2.getClassName().equals(s1.getClassName()) && stacktraceelement2.getFileName().equals(s1.getFileName()) && stacktraceelement2.getMethodName().equals(s1.getMethodName()))
            {
                if (s2 != null != this.stackTrace.length > 1)
                {
                    return false;
                }
                else if (s2 != null && !this.stackTrace[1].equals(s2))
                {
                    return false;
                }
                else
                {
                    this.stackTrace[0] = s1;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public void trimStackTraceEntriesFromBottom(int amount)
    {
        StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
        System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
        this.stackTrace = astacktraceelement;
    }

    public void appendToStringBuilder(StringBuilder builder)
    {
        builder.append("-- ").append(this.name).append(" --\n");
        builder.append("Details:");
        Iterator iterator = this.children.iterator();

        while (iterator.hasNext())
        {
            CrashReportCategory.Entry entry = (CrashReportCategory.Entry)iterator.next();
            builder.append("\n\t");
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0)
        {
            builder.append("\nStacktrace:");
            StackTraceElement[] astacktraceelement = this.stackTrace;
            int j = astacktraceelement.length;

            for (int i = 0; i < j; ++i)
            {
                StackTraceElement stacktraceelement = astacktraceelement[i];
                builder.append("\n\tat ");
                builder.append(stacktraceelement.toString());
            }
        }
    }

    public StackTraceElement[] getStackTrace()
    {
        return this.stackTrace;
    }

    public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData)
    {
        final int i = Block.getIdFromBlock(blockIn);
        category.addCrashSectionCallable("Block type", new Callable()
        {
            private static final String __OBFID = "CL_00001426";
            public String call()
            {
                try
                {
                    return String.format("ID #%d (%s // %s)", new Object[] {Integer.valueOf(i), blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName()});
                }
                catch (Throwable throwable)
                {
                    return "ID #" + i;
                }
            }
        });
        category.addCrashSectionCallable("Block data value", new Callable()
        {
            private static final String __OBFID = "CL_00001441";
            public String call()
            {
                if (blockData < 0)
                {
                    return "Unknown? (Got " + blockData + ")";
                }
                else
                {
                    String s = String.format("%4s", new Object[] {Integer.toBinaryString(blockData)}).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] {Integer.valueOf(blockData), s});
                }
            }
        });
        category.addCrashSectionCallable("Block location", new Callable()
        {
            private static final String __OBFID = "CL_00001465";
            public String call()
            {
                return CrashReportCategory.getCoordinateInfo(pos);
            }
        });
    }

    public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final IBlockState state)
    {
        category.addCrashSectionCallable("Block", new Callable()
        {
            private static final String __OBFID = "CL_00002617";
            public String call()
            {
                return state.toString();
            }
        });
        category.addCrashSectionCallable("Block location", new Callable()
        {
            private static final String __OBFID = "CL_00002616";
            public String call()
            {
                return CrashReportCategory.getCoordinateInfo(pos);
            }
        });
    }

    static class Entry
        {
            private final String key;
            private final String value;
            private static final String __OBFID = "CL_00001489";

            public Entry(String key, Object value)
            {
                this.key = key;

                if (value == null)
                {
                    this.value = "~~NULL~~";
                }
                else if (value instanceof Throwable)
                {
                    Throwable throwable = (Throwable)value;
                    this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
                }
                else
                {
                    this.value = value.toString();
                }
            }

            public String getKey()
            {
                return this.key;
            }

            public String getValue()
            {
                return this.value;
            }
        }
}