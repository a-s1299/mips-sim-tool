package com.example.mips_sim.controller;

public class ShiftLeft2 {

    private SignExtender extenderRef;

    public ShiftLeft2 () {

    }

    public void addDependency(SignExtender extender) {

        extenderRef = extender;
    }

    public Integer getShiftedValue() {

        return (extenderRef.getExtended() << 2);
    }
}
