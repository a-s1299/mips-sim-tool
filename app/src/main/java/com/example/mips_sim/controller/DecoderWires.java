package com.example.mips_sim.controller;

public class DecoderWires {

    private static String buffer;

    private static Integer opCode;
    private static Integer rs;
    private static Integer rt;
    private static Integer rd;
    private static Integer shamt;
    private static Integer funct;
    private static Integer immediate;
    private static Integer address;

    public static Integer getOpCode() {
        return opCode;
    }

    public static void setOpCode(String byte3) {

        buffer = byte3;
        opCode = Integer.parseInt( buffer.substring(0, 6), 2 );
    }

    public static Integer getRs() {
        return rs;
    }

    public static void setRs(String byte3, String byte2) {

        buffer = byte3 + byte2;
        rs = Integer.parseInt( buffer.substring(6, 11), 2 );
    }

    public static Integer getRt() {
        return rt;
    }

    public static void setRt(String byte2) {
        buffer = byte2;
        rt = Integer.parseInt( buffer.substring(3, 8), 2 );
    }

    public static Integer getRd() {
        return rd;
    }

    public static void setRd(String byte1) {
        buffer = byte1;
        rd = Integer.parseInt( buffer.substring(0,5), 2 );
    }

    public static Integer getShamt() {
        return shamt;
    }

    public static void setShamt(String byte1, String byte0) {
        buffer = byte1 + byte0;
        shamt = Integer.parseInt( buffer.substring(5,10), 2 );
    }

    public static Integer getFunct() {
        return funct;
    }

    public static void setFunct(String byte0) {
        buffer = byte0;
        funct = Integer.parseInt( buffer.substring(2,8), 2 );
    }

    public static Integer getImmediate() {
        return immediate;
    }

    public static void setImmediate(String byte1, String byte0) {
        buffer = byte1 + byte0;
        immediate = Integer.parseInt(buffer, 2 );
    }

    public static Integer getAddress() {
        return address;
    }

    public static void setAddress(String byte3, String byte2, String byte1, String byte0) {
        buffer = byte3 + byte2 + byte1 + byte0;
        address = Integer.parseInt( buffer.substring(6,32), 2 );
    }

}