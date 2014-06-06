package com.cosooki.adb.data;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.cosooki.adb.util.MapUtils;

public class TopData {

    public String startDate;
    public String endDate;    
    private HashMap<String, Integer> processes;

    public TopData(String _startTime) {
        startDate = _startTime;
        processes = new LinkedHashMap<String, Integer>();
    }

    public void append(String name, int cpu) {
        int sum = 0;
        if (processes.containsKey(name)) {
            sum = processes.get(name);
        }

        sum += cpu;
        processes.put(name, sum);
    }

    public void sort() {
        processes = (HashMap<String, Integer>) MapUtils.sortByValue(processes);
    }
}
