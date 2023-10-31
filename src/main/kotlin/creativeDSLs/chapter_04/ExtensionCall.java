package creativeDSLs.chapter_04;

import java.util.List;

public class ExtensionCall {
    public static void main(String[] args) {
       List<Integer> digits = ExtensionSnippetKt.digits(1729,10);
        System.out.println(digits);
    }
}
