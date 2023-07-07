package creativeDSLs.chapter_05;

import org.apache.commons.numbers.complex.Complex;

import static creativeDSLs.chapter_05.ComplexKt.*;

public class Interop {

    public static void main(String[] args) {
        Complex c = plus(4.0, times(3.0, getI()));
        System.out.println(c);
    }
}
