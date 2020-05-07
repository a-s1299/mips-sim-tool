package com.example.mips_sim.controller;

public class ALUControl {

    private DecoderWires wiresRef;
    private Integer ALUOp;
    private Integer funct;
    private ALU mainALURef;

    public ALUControl() {

    }

    public void addDependency(DecoderWires wires, ALU mainALU) {

        wiresRef = wires;
        mainALURef = mainALU;
    }

    public void receiveSignal(Integer newOp) {

        ALUOp = newOp;
        funct = wiresRef.getFunct();
        mainALURef.setOp( computeOp() );
    }

    private Integer computeOp() {

        int tempResult = 0;

        switch (ALUOp) {

            case 0b00:
                tempResult = 0b010;
                break;
            case 0b01:
                tempResult = 0b110;
                break;
            case 0b10:
                switch (funct) {
                    case 0b100000: // add
                        tempResult = 0b010;
                        break;
                    case 0b100010: // sub
                        tempResult = 0b110;
                        break;
                    case 0b100100: // and
                        tempResult = 0b000;
                        break;
                    case 0b100101: // or
                        tempResult = 0b001;
                        break;
                    case 0b101010: // set-on-less-than (slt)
                        tempResult = 0b111;
                        break;
                    default: // debugging purposes
                        System.out.println("ERROR: ALU control computeOp() funct case fail");
                }
                break;
            case 0b11:
                tempResult = 0b011;
                break;
            default: // debugging purposes
                System.out.println("ERROR: ALU control computeOp() funct case fail");
        }

        return tempResult;
    }
}
