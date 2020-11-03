
import java.util.*;


public class GameEffects {

    public String greeting() {

        String ln1 = " __  __                      __    __                                           \n";
        String ln2 = "/\\ \\/\\ \\                    /\\ \\__/\\ \\                                         \n";
        String ln3 = "\\ \\ \\.\\ \\     __   __  __   \\ \\ ,.\\ \\ \\___      __   _ __    __             \n";
        String ln4 = " \\ \\  .  \\  / __ \\/\\ \\/\\ \\   \\ \\ \\/\\ \\  .  \\  / __ \\/\\  __\\/ __ \\          \n";
        String ln5 = "  \\ \\ \\ \\ \\/\\  __/\\ \\ \\_\\ \\   \\ \\ \\_\\ \\ \\ \\ \\/\\  __/\\ \\ \\//\\  __/  __ \n";
        String ln6 = "   \\ \\_\\ \\_\\ \\____\\\\/ ____ \\   \\ \\__\\\\ \\_\\ \\.\\ \\____\\\\ \\_\\\\ \\____\\/\\ \\ \n";
        String ln7 = "    \\/./\\/./\\/____/  /___/> \\   \\/__/ \\/./\\/./\\/____/ \\/./ \\/____/\\ \\/ \n";
        String ln8 = "                       /\\___/                                      \\/            \n";
        String ln9 = "                       \\/__/                                                      \n";

        String ln10 = "                      __                                                   \n";
        String ln11 = "                     /\\ \\__                                                \n";
        String ln12 = "                 ____\\ \\ ,_\\  _ __    __      ___      __      __   _ __   \n";
        String ln13 = "                / ,__\\\\ \\ \\/ /\\  __\\/ __ \\  /  _  \\  / _  \\  / __ \\/\\ __\\ \n";
        String ln14 = "               /\\__,  \\\\ \\ \\_\\ \\ \\//\\ \\L\\.\\_/\\ \\/\\ \\/\\ \\L\\ \\/\\  __/\\ \\ \\/  \n";
        String ln15 = "               \\/\\____/ \\ \\__\\\\ \\_\\\\ \\__/.\\_\\ \\_\\ \\_\\ \\____ \\ \\____\\\\ \\.\\  \n";
        String ln16 = "                \\/___/   \\/__/ \\/./ \\/__/\\/./\\/./\\/./\\/___L\\ \\/____/ \\/./  \n";
        String ln17 = "                                                       /\\____/             \n";
        String ln18 = "                                                       \\_/__/              \n";
        String ln19 = "\n";

        return ln1 + ln2 + ln3 + ln4 + ln5 + ln6 + ln7 + ln8 + ln9 + ln10 + ln11 + ln12 + ln13 + ln14 + ln15 + ln16 + ln17 + ln18 + ln19;
    } // greeting()

    public void loading_bar() {
        String bar = "#";

        System.out.print("Loading ... ");
        for (int i = 0; i < 50; i++) {
            System.out.print(bar);
            try {
                Thread.sleep(35);
            } catch (Exception e) {
                System.err.println("Thread.sleep error: loading bar");
            }
        }
        System.out.print(" 100%");

    } // end loading_bar()

}   // end class