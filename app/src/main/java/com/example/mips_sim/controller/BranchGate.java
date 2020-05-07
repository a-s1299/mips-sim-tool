package com.example.mips_sim.controller;

public class BranchGate {

    private Integer signal;
    private Integer src0;
    private Integer src1;
    private MUX PCSrcRef;

    public BranchGate() {

    }

    public void addDependency(MUX PCSrc) {

        PCSrcRef = PCSrc;
    }

    public void setSrc0(Integer zero) {
        src0 = zero; //last
        sendResult();
    }

    public void setSrc1(Integer one) {
        src1 = one;
    }

    public void sendResult() {

        PCSrcRef.receiveSignal( (src0 == 1 && src1 == 1) ? 1 : 0 );
    }

}
