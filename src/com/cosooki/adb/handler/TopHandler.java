package com.cosooki.adb.handler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cosooki.adb.command.Command;
import com.cosooki.adb.command.TopCommand;
import com.cosooki.adb.data.TopData;
import com.cosooki.adb.item.TopItem;
import com.cosooki.adb.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class TopHandler extends Handler{
    private TopData topData;
    private ArrayList<TopCommand> topCommands;
    
    public TopHandler() {
        super();
        topData = new TopData(_startTime);
        topCommands = new ArrayList<TopCommand>();
    }

    @Override
    public String getCommand() {
        return "top";
    }
    
    @Override
    public <T> void handle(Command<T> command) {
        TopCommand tc = (TopCommand)command;
        
        List<TopItem> items = tc.getItems();
        
        String cmd = getCommand();
        
        for (TopItem item : items) {
            String name = item.getValue(TopItem.FIELD_NAME);
            String cpu = item.getValue(TopItem.FIELD_CPU);
            
            if (!IOUtils.isEmpty(name) && cmd.equals(name)) {
                continue;
            }
            topData.append(name, IOUtils.parseInt(cpu));
        }
        
        topCommands.add(tc);
    }

    @Override
    public void onWriteTo(FileWriter writer, boolean verbose) {
        
        topData.endDate = _endTime;
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            topData.sort();

            String json = gson.toJson(topData);
            writer.write(json);
            if (verbose) {
                json = gson.toJson(topCommands);
                writer.write(json);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
