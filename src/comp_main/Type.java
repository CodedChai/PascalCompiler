package comp_main;

public class Type {

    public String type;

    public Type(String t) {
        type = t;
    }

    public boolean isEquivalent(Type t) {
        String t1;
        String t2;
        if (type.indexOf("PP_") == -1) {
            t1 = type;
        } else {
            t1 = type.replaceFirst("PP_", "");
        }
        if (t.type.indexOf("PP_") == -1) {
            t2 = t.type;
        } else {
            t2 = t.type.replaceFirst("PP_", "");
        }
        return t1.equals(t2);
    }

}
