package com.example.mips_sim.controller;

import com.example.mips_sim.model.DataMemory;
import com.example.mips_sim.model.RegisterFile;

public class ControlUnit {

    private Integer opCode;

    private DecoderWires wiresRef;
    private MUX regDstRef;
    private MUX ALUSrcRef;
    private MUX memToRegRef;
    private ALUControl ALUControlRef;
    private RegisterFile regFileRef;
    private DataMemory dataMemRef;
    private BranchGate branchGateRef;
    // maybe add jumping later

    public ControlUnit() {

    }

    public void addDependency(DecoderWires wires, MUX regDst, MUX ALUSrc, MUX memToReg, ALUControl aluControl,
                              RegisterFile regFile, DataMemory dataMem, BranchGate BranchGate) {

        wiresRef = wires;
        regDstRef = regDst;
        ALUSrcRef = ALUSrc;
        memToRegRef = memToReg;
        ALUControlRef = aluControl;
        regFileRef = regFile;
        dataMemRef = dataMem;
        branchGateRef = BranchGate;
    }

    public void computeSignals() {

        opCode = wiresRef.getOpCode();

        switch (opCode) {

            case 0: // R-type, 0b000000
                regDstRef.receiveSignal(1);
                regFileRef.receiveSignal(1);
                ALUSrcRef.receiveSignal(0);
                ALUControlRef.receiveSignal(0b10);
                dataMemRef.receiveSignal(0, 0);
                memToRegRef.receiveSignal(1);
                branchGateRef.setSrc0(0);
                break;

            case 8: // addi, 0b001000
                regDstRef.receiveSignal(0);
                regFileRef.receiveSignal(1);
                ALUSrcRef.receiveSignal(1);
                ALUControlRef.receiveSignal(0b00);
                dataMemRef.receiveSignal(0, 0);
                memToRegRef.receiveSignal(1);
                branchGateRef.setSrc0(0);
                break;

            case 35: // lw 0b100011
                regDstRef.receiveSignal(0);
                regFileRef.receiveSignal(1);
                ALUSrcRef.receiveSignal(1);
                ALUControlRef.receiveSignal(0b00);
                dataMemRef.receiveSignal(1, 0);
                memToRegRef.receiveSignal(0);
                branchGateRef.setSrc0(0);
                break;

            case 43: // sw, 0b101011
                regDstRef.receiveSignal(0);
                regFileRef.receiveSignal(0);
                ALUSrcRef.receiveSignal(1);
                ALUControlRef.receiveSignal(0b00);
                dataMemRef.receiveSignal(0, 1);
                memToRegRef.receiveSignal(0);
                branchGateRef.setSrc0(0);
                break;

            case 4: // beq, 0b000100
                regDstRef.receiveSignal(0);
                regFileRef.receiveSignal(0);
                ALUSrcRef.receiveSignal(0);
                ALUControlRef.receiveSignal(0b01);
                dataMemRef.receiveSignal(0, 0);
                memToRegRef.receiveSignal(0);
                branchGateRef.setSrc0(1);
                break;

            case 5: // bne, 0b000100
                regDstRef.receiveSignal(0);
                regFileRef.receiveSignal(0);
                ALUSrcRef.receiveSignal(0);
                ALUControlRef.receiveSignal(0b11);
                dataMemRef.receiveSignal(0, 0);
                memToRegRef.receiveSignal(0);
                branchGateRef.setSrc0(1);
                break;

            default: // debugging purposes
                System.out.println("ERROR: ControlUnit computeSignals() case fail");
        }
    }

    public Boolean testUserSignals(Integer regDst, Integer regWrite, Integer ALUSrc, Integer memWrite, Integer memRead, Integer memToReg, Integer branchGate) {

        opCode = wiresRef.getOpCode();
        Boolean userCorrect = false;

        switch (opCode) {

            case 0: // R-type
                if (regDst == 1 &&
                        regWrite == 1 &&
                        ALUSrc == 0 &&
                        memWrite == 0 &&
                        memRead == 0 &&
                        memToReg == 1 &&
                        branchGate == 0)
                    userCorrect = true;
                break;
            case 8: // addi
                if (regDst == 0 &&
                        regWrite == 1 &&
                        ALUSrc == 1 &&
                        memWrite == 0 &&
                        memRead == 0 &&
                        memToReg == 1 &&
                        branchGate == 0)
                    userCorrect = true;
                break;
            case 35: // lw
                if (regDst == 0 &&
                        regWrite == 1 &&
                        ALUSrc == 1 &&
                        memWrite == 0 &&
                        memRead == 1 &&
                        memToReg == 0 &&
                        branchGate == 0)
                    userCorrect = true;
                break;
            case 43: // sw
                if (regDst == 0 &&
                        regWrite == 0 &&
                        ALUSrc == 1 &&
                        memWrite == 1 &&
                        memRead == 0 &&
                        memToReg == 0 &&
                        branchGate == 0)
                    userCorrect = true;
                break;
            case 4: case 5: // beq & bne
                if (regDst == 0 &&
                        regWrite == 0 &&
                        ALUSrc == 0 &&
                        memWrite == 0 &&
                        memRead == 0 &&
                        memToReg == 0 &&
                        branchGate == 1)
                    userCorrect = true;
                break;
        }

        return userCorrect;
    }
}
