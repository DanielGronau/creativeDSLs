package creativeDSLs.chapter_13.reifiedTypes;

import java.util.List;

public class CallReified {

    public static void main(String[] args) {
        ReifiedTypeSnippetKt.tellTypeJava(List.of(1,2,3), Integer.class);

        ReifiedTypeSnippetKt.tellTypeJava(List.of(List.of(1,2,3)), List.class);

        //ReifiedTypeSnippetKt.class.getMethod("tellYourType", List.class).invoke(Integer.class, List.of(1,2,3));
    }
}
