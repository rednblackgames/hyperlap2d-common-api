package games.rednblack.h2d.common;

import org.puremvc.java.patterns.facade.Facade;

public class HyperLog {

    private static Facade facade;

    public static void setFacade(Facade f) {
        facade = f;
    }

    public static void info(String string) {
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[246BCE]");
        System.out.println(string);
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[ffffff]");
    }

    public static void cInfo(String string) {
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[246BCE]");
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, string);
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[ffffff]\n");
    }
}
