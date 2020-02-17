package com.danielalejandrohc.microservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemoryAlloc {

    private List<Integer> memory = new ArrayList<>();
    private int someNumber = 0;

    @GetMapping("/addToMemory")
    public int addToMemory() {
        memory.add(someNumber++);
        return memory.size();
    }

}
