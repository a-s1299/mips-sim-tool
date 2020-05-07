package com.example.mips_sim.model;
import com.example.mips_sim.controller.DecoderWires;


public class InstructionMemory {

    private ProgramCounter PCRef;
    private String[] instructions; // each index is one byte
    private String byte0;
    private String byte1;
    private String byte2;
    private String byte3;

    public InstructionMemory(String[] instructionSet) {

        instructions = instructionSet;
    }

    public void addDependency(ProgramCounter PC){

        PCRef = PC;
    }

    public void loadWords() throws ArrayIndexOutOfBoundsException {

        try {
            byte0 = instructions[PCRef.getIndex()];
            byte1 = instructions[PCRef.getIndex()+1];
            byte2 = instructions[PCRef.getIndex()+2];
            byte3 = instructions[PCRef.getIndex()+3];
        } catch (Exception err) {
            throw err;
        }

    }

    public void setNextInstruction() {

        DecoderWires.setOpCode(byte3);
        DecoderWires.setRs(byte3, byte2);
        DecoderWires.setRt(byte2);
        DecoderWires.setRd(byte1);
        DecoderWires.setShamt(byte1, byte0);
        DecoderWires.setFunct(byte0);
        DecoderWires.setImmediate(byte1, byte0);
        DecoderWires.setAddress(byte3, byte2, byte1, byte0);

    }

    public String testAllCodes() { // debugging purposes

        String allCodes;

        allCodes = " opcode: " + DecoderWires.getOpCode() + "\n" +
                    " rs: " + DecoderWires.getRs() + "\n" +
                    " rt: " + DecoderWires.getRt() + "\n" +
                    " rd: " + DecoderWires.getRd() + "\n" +
                    " shamt: " + DecoderWires.getShamt() + "\n" +
                    " funct: " + DecoderWires.getFunct() + "\n" +
                    " imm: " + DecoderWires.getImmediate() + "\n" +
                    " addr: " + DecoderWires.getAddress() + "\n";

        return allCodes;
    }

}
