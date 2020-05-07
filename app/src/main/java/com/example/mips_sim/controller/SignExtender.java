package com.example.mips_sim.controller;

public class SignExtender {

    private DecoderWires incomingWires;

    public SignExtender() {

    }

    public void addDependency(DecoderWires wiresRef) {

        incomingWires = wiresRef;
    }

    public short getExtended() {

        String not_extended = Integer.toBinaryString( incomingWires.getImmediate() );
        String sign_extended;

        if ( not_extended.length() == 16 ) // will only be length 16 if 1 is the most significant digit
            sign_extended = "111111111111111" + not_extended; // 1-less, reserved for sign bit
        else
            sign_extended = "000000000000000" + not_extended;

        short res = (short)Integer.parseInt(sign_extended, 2);

        return res;
    }

}
