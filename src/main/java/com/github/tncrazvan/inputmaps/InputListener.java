package com.github.tncrazvan.inputmaps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
/*import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;*/

public class InputListener implements NativeKeyListener/*, NativeMouseListener, NativeMouseMotionListener*/ {
    JsonObject config = new JsonObject();
    private String[] keys;
    private Map<String,Map<String,Boolean>> pressed = new HashMap<>();
    private Runtime runtime = Runtime.getRuntime();
    public InputListener(File config) throws IOException {
        try(InputStream is = new FileInputStream(config)) {
            Gson gson = new Gson();
            this.config = gson.fromJson(new String(is.readAllBytes()), JsonObject.class);
            keys = this.config.keySet().toArray(new String[0]);

            for(int i = 0; i < keys.length; i++){
                JsonArray carray = this.config.get(keys[i]).getAsJsonArray();
                int len = carray.size();
                Map<String,Boolean> ints = new HashMap<>();
                for(int j = 0; j < len; j++){
                    ints.put(carray.get(j).getAsString(), false);
                }
                pressed.put(keys[i], ints);
            }
            System.out.println("Configuration mapped.");
        } 
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String name = e.getKeyText(e.getKeyCode());
        System.out.println("############ KEY PRESSED: "+name+" ############");
        for(String key : keys){
            Map<String,Boolean> item = pressed.get(key);

            //set
            for(String index : item.keySet()){
                if(!item.get(index).booleanValue() && index.equals(name)){
                    item.put(index, true);
                }
            }

            boolean trigger = true;
            //check
            for(String index : item.keySet()){
                if(!item.get(index).booleanValue()){
                    trigger = false;
                    break;
                }
            }
            if(trigger){
                try {
                    runtime.exec("cmd /c start "+key);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        
    }
    
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        
        for(String key : keys){
            Map<String,Boolean> item = pressed.get(key);

            //set
            for(String index : item.keySet()){
                item.put(index, false);
            }
        }

    }
    
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        
    }
/*
    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
        
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
        
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
        
    }*/
}