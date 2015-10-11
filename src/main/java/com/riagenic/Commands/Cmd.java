package com.riagenic.Commands;

/**
 * Created by Scott on 10/10/2015.
 */

import com.riagenic.MossyClient;
import com.riagenic.Utils.EntityHelper;
import com.riagenic.Utils.MiscUtils;
import net.minecraft.util.BlockPos;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public abstract class Cmd {
    private String name = getClass().getAnnotation(Info.class).name();
    private String help = getClass().getAnnotation(Info.class).help();
    private String[] syntax = getClass().getAnnotation(Info.class).syntax();

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();

        String help();

        String[] syntax();
    }

    public class SyntaxError extends Error {
        public SyntaxError() {
            super();
        }

        public SyntaxError(String message) {
            super(message);
        }
    }

    public class Error extends Throwable {
        public Error() {
            super();
        }

        public Error(String message) {
            super(message);
        }
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public String[] getSyntax() {
        return syntax;
    }

    public void printHelp() {
        for (String line : help.split("\n"))
            MossyClient.INSTANCE.chat.message(line);
    }

    public void printSyntax() {
        String output = "�o." + name + "�r";
        if (syntax.length != 0) {
            output += " " + syntax[0];
            for (int i = 1; i < syntax.length; i++)
                output += "\n    " + syntax[i];
        }
        for (String line : output.split("\n"))
            MossyClient.INSTANCE.chat.message(line);
    }

    protected final int[] argsToPos(String... args) throws Cmd.Error {
        int[] pos = new int[3];
        if (args.length == 3) {
            int[] playerPos =
                    new int[]{(int) Minecraft.getMinecraft().thePlayer.posX,
                            (int) Minecraft.getMinecraft().thePlayer.posY,
                            (int) Minecraft.getMinecraft().thePlayer.posZ};
            for (int i = 0; i < args.length; i++)
                if (MiscUtils.isInteger(args[i]))
                    pos[i] = Integer.parseInt(args[i]);
                else if (args[i].startsWith("~"))
                    if (args[i].equals("~"))
                        pos[i] = playerPos[i];
                    else if (MiscUtils.isInteger(args[i].substring(1)))
                        pos[i] =
                                playerPos[i]
                                        + Integer.parseInt(args[i].substring(1));
                    else
                        syntaxError("Invalid coordinates.");
                else
                    syntaxError("Invalid coordinates.");
        } else if (args.length == 1) {
            EntityLivingBase entity =
                    EntityHelper.searchEntityByNameRaw(args[0]);
            if (entity == null)
                error("Entity \"" + args[0] + "\" could not be found.");
            BlockPos blockPos = new BlockPos(entity);
            pos = new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
        } else
            syntaxError("Invalid coordinates.");
        return pos;
    }

    protected final void syntaxError() throws SyntaxError {
        throw new SyntaxError();
    }

    protected final void syntaxError(String message) throws SyntaxError {
        throw new SyntaxError(message);
    }

    protected final void error(String message) throws Error {
        throw new Error(message);
    }

    public abstract void execute(String[] args) throws Cmd.Error;
}
