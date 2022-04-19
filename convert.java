// package converter;
import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {
    
    public static void main(String[] args) {
        // write your code here
        Scanner sc = new Scanner(System.in);
        String[] s;
        String s1;
        int a;
        int b;
        BigInteger c;
        do {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            s = sc.nextLine().split(" ");
            if (s[0].toString().equals("/exit")) {
                continue;
            }
            a = Integer.parseInt(s[0]);
            b = Integer.parseInt(s[1]);
            do {
                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ", a, b);
                s1 = sc.nextLine();
                if (s1.equals("/back")) {
                    continue;
                }
                System.out.print("Conversion result: ");
                System.out.println(converFromAToB(a, b, s1));
            } while(!s1.equals("/back"));
            
        } while(!s[0].toString().equals("/exit"));
    }
    
    public static String converFromAToB(int a, int b, String s1) {
        StringBuilder s = new StringBuilder();
        StringBuilder s4 = new StringBuilder();
        BigInteger b1 = BigInteger.ONE;
        BigDecimal b2 = BigDecimal.ONE;
        int ok = 0;
        // start conversion from base "a" to base 10 
        if (s1.indexOf(".") != -1 && a != 10) {
            b1 = convert_10_b(s1.substring(0, s1.indexOf(".")), a);
            if (b != 10) {
                b2 = convert_10_f(s1.substring(s1.indexOf(".") + 1), a, 0);
            } else {
                b2 = convert_10_f(s1.substring(s1.indexOf(".") + 1), a, 1);
            }
            ok = 1;
            if (b == 10) {
                s = new StringBuilder(b2.toString());
                return b1.toString() + s.toString().substring(s.toString().indexOf("."));
            }
        } else if (a != 10) {
            b1 = convert_10_b(s1, a);
        }
        if (a == 10 && s1.indexOf(".") != -1) {
            b1 = new BigInteger(s1.substring(0, s1.indexOf(".")));
            b2 = new BigDecimal(s1);
            b2 = b2.subtract(new BigDecimal(b1.toString()));
            ok = 1;
        } else if (a == 10) {
            b1 = new BigInteger(s1);
        }
        
        if (b == 10) {
            return b1.toString();
        }
        // end of conversion
        // convert from base 10 to any other base
        while(true) {
            int s2 = Integer.parseInt(b1.divideAndRemainder(BigInteger.valueOf(b))[1].toString());
            if (s2 <= 9) {
                s.append(s2);
            } else {
                s.append((char) (s2 - 10 + 'a'));
            }
            b1 = b1.divide(BigInteger.valueOf(b));
            if (b1.toString().equals("0")) {
                break;
            }
        }
        // end conversion
        if (ok == 0) {
            return s.reverse().toString().toLowerCase();
        }
        // start conversion fraction part
        s = s.reverse();
        s.append(".");
        ok = 0;
        while (true) {
            b2 = b2.multiply(BigDecimal.valueOf(b));
            if (b2.toString().equals("0E-10")) {
                b2 = new BigDecimal("0.00000");
            }
            int val = Integer.parseInt(b2.toString().substring(0, b2.toString().indexOf(".")));
            if (val >= 10) {
                s.append((char) (val - 10 + 'a'));
            } else {
                s.append(val);
            }
            b2 = b2.subtract(BigDecimal.valueOf(val));
            // if (b2.toString().equals("0")) {
            //     break;
            // }
            ok++;
            if (ok == 5) {
                break;
            }
        }
        // end conversion
        if (s.toString().substring(s.toString().indexOf(".") + 1).length() != 5) {
            System.out.println("we have an erro den jad bouk");
        }
        return s.toString().toLowerCase();
    } 
    
    public static BigInteger convert_10_b(String a, int b) {
        BigInteger res = BigInteger.ZERO;
        a = a.toLowerCase();
        for (int i = a.length() - 1; i >= 0; i--) {
            if (a.charAt(i) >= 'a' && a.charAt(i) <= 'z') {
                BigInteger val = BigInteger.valueOf((a.charAt(i) - 'a' + 10) * (long)Math.pow(b, a.length() - 1 - i));
                res = res.add(val);
            } else {
                res = res.add(BigInteger.valueOf((a.charAt(i) - '0') * (long)Math.pow(b, a.length() - 1 - i)));
            }
        } 
        return res;
    }
    
    public static BigDecimal convert_10_f(String a, int b, int ok) {
        a = a.toLowerCase();
        BigDecimal res = BigDecimal.ZERO;
        BigDecimal val;
        BigDecimal B = BigDecimal.valueOf(b);
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) >= 'a' && a.charAt(i) <= 'z') {
                val = BigDecimal.valueOf((a.charAt(i) + 10 - 'a'));
                val = val.divide(B.pow(i + 1), 10, RoundingMode.HALF_DOWN);
                res = res.add(val);
            } else {
                val = BigDecimal.valueOf((a.charAt(i) - '0'));
                val = val.divide(B.pow(i + 1), 10, RoundingMode.HALF_DOWN);
                res = res.add(val);
            }
        }
        if (ok == 0) {
            return res;
        }
        return res.setScale(5, RoundingMode.HALF_DOWN);
    }
}
