public class HelloWorld{
    
    public static Integer[] myHex = { 0x01, 0x10, 0x01, 0x3c};
    public static String[] myStr = { "01", "10", "01", "3c"};
    
    public static String furst = "10000100";
    public static String secund = "10000000";
    public static Integer result;
    
    public static String furst_funct = "0010";
    public static String secund_funct = "0010";
    public static Integer rusult;
    
    public static Integer test = 0x10;
    
    public static Integer completeInt;
    public static String completeStr;
    
    

     public static void main(String []args){
        doIt();
     }
     
     public static void doIt() {
         
         System.out.println( Integer.parseInt(furst, 2) );
         setOpResult( furst, secund );
         //setFunctResult( furst_funct, secund_funct );
         
         System.out.println( compareVals(result) );
         
         checkSignBit(result);
         
         //System.out.println( Integer.toHexString( myArray[0] ) );
         /*
         System.out.println( Integer.toBinaryString(test) );
         
         System.out.println( Integer.parseInt (completeStr.substring(0, 2), 16 ));
         System.out.println( completeStr.substring(2, 4) );
         System.out.println( completeStr.substring(4, 6) );
         System.out.println( completeStr.substring(6, 8) );
         */
         
         //System.out.println( myArray[3] 
         //+  myArray[2] 
         //+  myArray[1] 
         //+  myArray[0] );
     }
     
     public static void setOpResult(String a, String b) {
         
         String temp =  a + b; // 1000 0100 1000 0000
         result = Integer.parseInt( temp.substring(0,16), 2 ); // 10000100100, base 2
         //System.out.println(Integer.toHexString(result));
         //System.out.println(Integer.toBinaryString(result));
         //System.out.println(result);
         
         
     }
     
     public static void setFunctResult(String a, String b) {
         
         String temp = a + b; // 00100010
         rusult = Integer.parseInt( temp.substring(2,8), 2 ); // 100010, base 2
         //System.out.println(Integer.toHexString(rusult));
         System.out.println(Integer.toBinaryString(rusult));
         
     }
     
     public static Boolean compareVals(Integer toCompare) {
         
         Integer binaryValue = Integer.parseInt( "000001001", 2 );
         
         return (binaryValue == toCompare) ? true : false;
     }
     
     public static void checkSignBit(Integer toCheck) {
          
          String bit = Integer.toBinaryString( toCheck );
          
          System.out.println(bit.length());
     }
     
}