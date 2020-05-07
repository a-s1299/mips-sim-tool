package com.example.mips_sim.controller;

import com.example.mips_sim.model.DataMemory;
import com.example.mips_sim.model.InstructionMemory;
import com.example.mips_sim.model.ProgramCounter;
import com.example.mips_sim.model.RegisterFile;

public class MainTest {

//    private static String[] program_instructions = {"00000101","00000000","00001000","00100001", // addi $8 $8 5
//                                                    "00000100","00000000","00101001","00100001", // addi $9 $9 4
//                                                    "00100010","01010000","00001001","00000001"}; // sub $10 $8 $9

//    private static String[] program_instructions = {"00000101","00000000","00001000","00100001", // addi $8 $8 5
//                                                    "00000000","00000000","00101000","10101101", // sw $8 0($9)
//                                                    "00000000","00000000","00101001","10001101"}; // lw $9 0($9)

    private static String[] program_instructions =
            {"00000001","00000000","00001000","00100001", // addi $8 $8 1
                    "00100010","01001000","00101000","00000001", // sub $9 $9 $8
                    "11111101","11111111","00001001","00010101"}; // bne $8 $9 -3 (2's compliment)

    private static ProgramCounter PC;
    private static InstructionMemory instMem;
    private static RegisterFile regFile;
    private static DataMemory dataMem;

    private static ControlUnit contUnit;
    private static ALU mainALU;
    private static MUX regDst;
    private static MUX ALUSrc;
    private static MUX memToReg;
    private static MUX PCSrc;

    private static ALUControl ALUController;
    private static Adder PCPlusFour;
    private static Adder otherAdder;

    private static DecoderWires wires;
    private static BranchGate branch;
    private static SignExtender extender;
    private static ShiftLeft2 sl2;

    public static void main(String[] args) {

        init();

        regFile.preloadReg(9, "101");

        instMem.loadWords();
        instMem.setNextInstruction();
        contUnit.computeSignals();
        PC.updatePC();
        System.out.println("PASS1: \n\n\n" + "reg: \n" +regFile.getRegisterState());


        instMem.loadWords();
        instMem.setNextInstruction();
        contUnit.computeSignals();
        PC.updatePC();
        System.out.println("PASS2: \n\n\n" + "reg: \n" +regFile.getRegisterState());

        instMem.loadWords();
        instMem.setNextInstruction();
        contUnit.computeSignals();
        System.out.println("PASS3: \n\n\n" + "reg: \n" +regFile.getRegisterState());
    }

    private static void init() {

        createAllObjects();
        addDependencies();
    }

    private static void createAllObjects() {

        PC = new ProgramCounter();
        instMem = new InstructionMemory(program_instructions);
        regFile = new RegisterFile();
        dataMem = new DataMemory();

        contUnit = new ControlUnit();
        mainALU = new ALU();
        regDst = new MUX();
        ALUSrc = new MUX();
        memToReg = new MUX();
        PCSrc = new MUX();

        ALUController = new ALUControl();
        PCPlusFour = new Adder();
        otherAdder = new Adder();

        wires = new DecoderWires();
        branch = new BranchGate();
        extender = new SignExtender();
        sl2 = new ShiftLeft2();
    }

    private static void addDependencies() {

        PC.addDependency(PCSrc);
        instMem.addDependency(PC);
        regFile.addDependency(regDst, wires);
        dataMem.addDependency(mainALU, regFile);

        contUnit.addDependency(wires, regDst, ALUSrc, memToReg, ALUController, regFile, dataMem, branch);
        mainALU.addDependency(regFile, ALUSrc, branch);
        regDst.addDependency(wires);
        ALUSrc.addDependency(regFile, extender);
        memToReg.addDependency(dataMem, mainALU, regFile);
        PCSrc.addDependency(PCPlusFour, otherAdder);

        ALUController.addDependency(wires, mainALU);
        PCPlusFour.addDependency(PC);
        otherAdder.addDependency(PCPlusFour, sl2);

        branch.addDependency(PCSrc);
        extender.addDependency(wires);
        sl2.addDependency(extender);
    }


}
