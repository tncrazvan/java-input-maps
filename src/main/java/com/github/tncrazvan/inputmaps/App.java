package com.github.tncrazvan.inputmaps;

import java.io.File;
import java.io.IOException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        if(args.length == 0){
            System.out.println("Please provide an input configuration file");
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener( 
            new InputListener( 
                new File(args[0])
            ) 
        );
    }
}
