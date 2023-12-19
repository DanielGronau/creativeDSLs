package creativeDSLs.chapter_13.overloads;

public class CallOverloads {
    public static void main(String[] args) {
        DefaultArgsKt.withOverloading(17);
        DefaultArgsKt.withOverloading("two", 17);
        DefaultArgsKt.withOverloading("two", 12, 17);
        DefaultArgsKt.withOverloading("two", 12, 17, true);
    }
}
