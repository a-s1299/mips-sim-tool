package com.example.mips_sim.controller;

import com.example.mips_sim.model.RegisterFile;

public class ALU {

    private Integer controlCode;
    private Integer src1;
    private Integer src2;
    private Integer result;
    private Integer zeroBit;
    private RegisterFile regFileRef;
    private MUX ALUSrcRef;
    private BranchGate branchRef;

    public ALU() {

    }

    public void addDependency(RegisterFile regFile, MUX ALUSrc, BranchGate branch/*, ALUControl aluControl*/) { // RegFile, ALUSrc

        regFileRef = regFile;
        ALUSrcRef = ALUSrc;
        branchRef = branch;
    }

    private Integer operate() {

        switch (controlCode) {
            case 0: // and, rd = rs&rt
                result = src1 & src2;
                break;
            case 1: // or, rd = rs|rt
                result = src1 | src2;
                break;
            case 2: // add, rd = rs+rt
                result = src1 + src2;
                break;
            case 3: // not-equals, zerobit signal high if not-equals
                result = (src1 == src2) ? 1 : 0;
                break;
            case 6: // sub, rd = rs-rt
                result = src1 - src2;
                break;
            case 7: // set-on-less-than, rd = (rs<rt) ? 1 : 0
                result = (src1 < src2) ? 1 : 0;
                break;
            case 12: // NOR, !or
                result = ~(src1 | src2);
                break;
            default: // debugging purposes
                System.out.println("ERROR: ALU operate() case fail");
        }

        zeroBit = (result == 0) ? 1 : 0;
        return result;
    }

    public void setOp(Integer newOp) {

        controlCode = newOp;
        loadVals();
    }

    private void loadVals() {

        src1 = regFileRef.getRead1Data();
        src2 = ALUSrcRef.getCorrectVal();
        result = operate();
        branchRef.setSrc1( zeroBit );
    }

    public Integer getResult() {

        return result;
    }

}
