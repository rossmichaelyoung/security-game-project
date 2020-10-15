
import java.util.*;


public class GameEffects {

    public void greeting() {

        System.out.println(" __  __                      __    __                                          ");
        System.out.println("/\\ \\/\\ \\                    /\\ \\__/\\ \\                                        ");
        System.out.println("\\ \\ \\.\\ \\     __   __  __   \\ \\ ,.\\ \\ \\___      __   _ __    __            ");
        System.out.println(
                " \\ \\  .  \\  / __ \\/\\ \\/\\ \\   \\ \\ \\/\\ \\  .  \\  / __ \\/\\  __\\/ __ \\         ");
        System.out.println(
                "  \\ \\ \\ \\ \\/\\  __/\\ \\ \\_\\ \\   \\ \\ \\_\\ \\ \\ \\ \\/\\  __/\\ \\ \\//\\  __/  __");
        System.out.println(
                "   \\ \\_\\ \\_\\ \\____\\\\/ ____ \\   \\ \\__\\\\ \\_\\ \\.\\ \\____\\\\ \\_\\\\ \\____\\/\\ \\");
        System.out.println("    \\/./\\/./\\/____/  /___/> \\   \\/__/ \\/./\\/./\\/____/ \\/./ \\/____/\\ \\/");
        System.out.println("                       /\\___/                                      \\/           ");
        System.out.println("                       \\/__/                                                     ");

        System.out.println("                      __                                                  ");
        System.out.println("                     /\\ \\__                                               ");
        System.out.println("                 ____\\ \\ ,_\\  _ __    __      ___      __      __   _ __  ");
        System.out.println("                / ,__\\\\ \\ \\/ /\\  __\\/ __ \\  /  _  \\  / _  \\  / __ \\/\\ __\\");
        System.out.println(
                "               /\\__,  \\\\ \\ \\_\\ \\ \\//\\ \\L\\.\\_/\\ \\/\\ \\/\\ \\L\\ \\/\\  __/\\ \\ \\/ ");
        System.out.println(
                "               \\/\\____/ \\ \\__\\\\ \\_\\\\ \\__/.\\_\\ \\_\\ \\_\\ \\____ \\ \\____\\\\ \\.\\ ");
        System.out.println("                \\/___/   \\/__/ \\/./ \\/__/\\/./\\/./\\/./\\/___L\\ \\/____/ \\/./ ");
        System.out.println("                                                       /\\____/            ");
        System.out.println("                                                       \\_/__/             ");
        System.out.println();

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