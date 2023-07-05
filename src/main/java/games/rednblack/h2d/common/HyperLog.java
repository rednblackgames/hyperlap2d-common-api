package games.rednblack.h2d.common;

import games.rednblack.puremvc.Facade;

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

    public static void infoUnderline(String string) {
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[246BCE][UNDERLINE]");
        System.out.println(string);
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[ffffff][NORMAL]");
    }

    public static void infoStrike(String string) {
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[246BCE][STRIKE]");
        System.out.println(string);
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[ffffff][NORMAL]");
    }

    public static void cInfo(String string) {
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[246BCE]");
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, string);
        facade.sendNotification(MsgAPI.WRITE_TO_CONSOLE, "[ffffff]\n");
    }
}
