package comp_main;

public class ColoredNode {
    public boolean isGreen;
    public String id;
    public Type type;
    public int offset;
    public int numParams;

    public ColoredNode content;
    public ColoredNode next;
    public ColoredNode previous;

    public ColoredNode (boolean isG, String i, Type t, int off, int nump, ColoredNode prev) {
        isGreen = isG;
        id = i;
        type = t;
        offset = off;
        numParams = nump;
        previous = prev;
    }

}