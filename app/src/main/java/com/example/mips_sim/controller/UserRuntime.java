package com.example.mips_sim.controller;

public class UserRuntime {

    private static Boolean isCorrect = false;
    private static Integer status = 2; // << ----------------- FIX TO ZERO AFTER
    private static Integer selection = 0;

    private Integer regDst;
    private Integer regWrite;
    private Integer ALUSrc;
    private Integer memWrite;
    private Integer memRead;
    private Integer memToReg;
    private Integer branchGate;

    private static String[] FIRST_INSTRUCTION_SET =
            {"00000101","00000000","00001000","00100001", //    addi $8 $8 5
            "00000000","00000000","00101000","10101101", //     sw $8 0($9)
            "00000000","00000000","00101001","10001101"}; //    lw $9 0($9)

    private static String[] SECOND_INSTRUCTION_SET = //         (pre-load $9 w/5)
            {"00000001","00000000","00001000","00100001", //    addi $8 $8 1
            "00100010","01001000","00101000","00000001", //     sub $9 $9 $8
            "11111101","11111111","00001001","00010101"}; //    bne $8 $9 -3 (2's compliment)

    private static String[] USER_INSTRUCTION_SET;

    public UserRuntime() {


    }

    public static String[] getInstructionSet() {

        String[] correctInstructionSet = new String[]{};

        switch (selection) {
            case 0:
                correctInstructionSet = FIRST_INSTRUCTION_SET;
                break;
            case 1:
                correctInstructionSet = SECOND_INSTRUCTION_SET;
                break;
            case 2:
                correctInstructionSet = USER_INSTRUCTION_SET;
                break;
        }
        return correctInstructionSet;
    }

    public static Boolean getIsCorrect() {

        return isCorrect;
    }

    public static void toggleCorrect() {

        isCorrect = !isCorrect;
    }

    public static Integer getStatus() {

        return status;
    }

    public static void levelUp() {

        ++status;
    }

    public static Integer getSelection() {

        return selection;
    }

    public static void setStageSelection(Integer newSelection) {

        selection = newSelection;
    }

    public void setSelection(Integer regDstRef, Integer regWriteRef, Integer ALUSrcRef, Integer memWriteRef, Integer memReadRef, Integer memToRegRef, Integer branchGateRef) {

        regDst = regDstRef;
        regWrite = regWriteRef;
        ALUSrc = ALUSrcRef;
        memWrite = memWriteRef;
        memRead = memReadRef;
        memToReg = memToRegRef;
        branchGate = branchGateRef;
    }

    public static void setCustomInstruction(String[] customInstruction) {

        USER_INSTRUCTION_SET = customInstruction;
    }

    public Boolean validate(ControlUnit testAgainst) {

        return testAgainst.testUserSignals(regDst, regWrite, ALUSrc, memWrite, memRead, memToReg, branchGate);
    }

    public String translateInstruction(SignExtender extender) {

        String theInstructionTranslation = "";

        switch ( DecoderWires.getOpCode() ) {

            case 0:
                switch ( DecoderWires.getFunct() ) {
                    case 0b100000: // add
                        theInstructionTranslation = "add" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " $" + DecoderWires.getRd();
                        break;
                    case 0b100010: // sub
                        theInstructionTranslation = "sub" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " $" + DecoderWires.getRd();
                        break;
                    case 0b100100: // and
                        theInstructionTranslation = "and" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " $" + DecoderWires.getRd();
                        break;
                    case 0b100101: // or
                        theInstructionTranslation = "or" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " $" + DecoderWires.getRd();
                        break;
                    case 0b101010: // set-on-less-than (slt)
                        theInstructionTranslation = "slt" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " $" + DecoderWires.getRd();
                }
                break;
            case 8:
                theInstructionTranslation = "addi" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " " + extender.getExtended();
                break;
            case 35:
                theInstructionTranslation = "lw" + " $" + DecoderWires.getRt() + " " + DecoderWires.getImmediate() + "($" + DecoderWires.getRs() + ")";
                break;
            case 43:
                theInstructionTranslation = "sw" + " $" + DecoderWires.getRt() + " " + DecoderWires.getImmediate() + "($" + DecoderWires.getRs() + ")";
                break;
            case 4:
                theInstructionTranslation = "beq" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " " + extender.getExtended();
                break;
            case 5:
                theInstructionTranslation = "bne" + " $" + DecoderWires.getRs() + " $" + DecoderWires.getRt() + " " + extender.getExtended();
                break;

        }

        return theInstructionTranslation;
    }

    public static String translateInstruction(String opcode, String rs, String rt, String rd, String shamt, String funct, String immediate) {

        String theInstructionTranslation = "";
        short imm = (short)Integer.parseInt(immediate, 2);

        switch ( opcode ) {

            case "000000":
                switch ( funct ) {
                    case "100000": // add
                        theInstructionTranslation = "add" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " $" + Integer.parseInt(rd, 2);
                        break;
                    case "100010": // sub
                        theInstructionTranslation = "sub" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " $" + Integer.parseInt(rd, 2);
                        break;
                    case "100100": // and
                        theInstructionTranslation = "and" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " $" + Integer.parseInt(rd, 2);
                        break;
                    case "100101": // or
                        theInstructionTranslation = "or" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " $" + Integer.parseInt(rd, 2);
                        break;
                    case "101010": // set-on-less-than (slt)
                        theInstructionTranslation = "slt" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " $" + Integer.parseInt(rd, 2);
                }
                break;
            case "001000":
                theInstructionTranslation = "addi" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " " + imm; //maybe needs more
                break;
            case "100011":
                theInstructionTranslation = "lw" + " $" + Integer.parseInt(rt, 2) + " " + imm + "($" + Integer.parseInt(rs, 2) + ")";
                break;
            case "101011":
                theInstructionTranslation = "sw" + " $" + Integer.parseInt(rt, 2) + " " + imm + "($" + Integer.parseInt(rs, 2) + ")";
                break;
            case "000100":
                theInstructionTranslation = "beq" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " " + imm;
                break;
            case "000101":
                theInstructionTranslation = "bne" + " $" + Integer.parseInt(rs, 2) + " $" + Integer.parseInt(rt, 2) + " " + imm;
                break;

        }

        theInstructionTranslation += "\n";

        return theInstructionTranslation;
    }

}
