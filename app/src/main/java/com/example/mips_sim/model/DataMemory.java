package com.example.mips_sim.model;

import com.example.mips_sim.controller.ALU;

import java.util.Vector;

public class DataMemory {

    private Integer memWrite;
    private Integer memRead;
    private String[] memory;
    private Integer address;
    private String writeData;

    private ALU mainALURef;
    private RegisterFile regFileRef;

    public DataMemory() {

        memory = new String[32];

        for (int i = 0; i < 32; ++i)
            memory[i] = "";
    }

    public void addDependency(ALU mainALU, RegisterFile regFile){

        mainALURef = mainALU;
        regFileRef = regFile;
    }

    public void receiveSignal(Integer isReading, Integer isWriting) {

        memRead = isReading;
        memWrite = isWriting;

        setAddress();
        setWriteData();

        if (memWrite == 1)
            memory[address] = writeData;
    }

    public void setAddress() {

        address = mainALURef.getResult();
    }

    public void setWriteData() {

        writeData = Integer.toBinaryString( regFileRef.getRead2Data() );
    }

    public Integer getReadData() {

        return (memRead == 1) ? Integer.parseInt( memory[address], 2) : 0;
    }

    public Vector<String> getMemoryState() {

        Vector<String> allOccupiedMemory = new Vector<>();
        Integer iter = 0;

        for (String eachMem : memory){
            if (eachMem != "")
                allOccupiedMemory.add( "0x" + Integer.toHexString(iter) + " = " + Integer.parseInt(eachMem, 2) + "\n" );

            ++iter;
        }

        if (allOccupiedMemory.size() == 0)
            allOccupiedMemory.add( "Currently Empty");

        return allOccupiedMemory;
    }

    public void preloadMem(Integer memNum, String memVal) {

        memory[memNum] = memVal;
    }

    public Boolean checkMem(Integer index, String query) {

        return ( memory[index].equals(query) );
    }
}
