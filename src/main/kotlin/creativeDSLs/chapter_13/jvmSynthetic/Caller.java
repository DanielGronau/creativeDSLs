package creativeDSLs.chapter_13.jvmSynthetic;

public class Caller {

    public static void main(String[] args) {
        String data = JvmSyntheticKt.getStuffForJava().join();
    }
}
