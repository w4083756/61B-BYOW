package byow.Core;

import java.util.Random;

public class TreeNode {
    private static final int minWidth = 8;
    private static final int maxWidth = 20;
    private static final int minHeight = 8;
    private static final int maxHeight = 20;
    public int startX, startY, Width, Height, childWidth, childHeight;
    private boolean horizontalSplit, verticalSplit;
    public TreeNode[] children = new TreeNode[2];
    public TreeNode left, right;
    public Random random = new Random();
    public TreeNode(int x, int y, int w, int h) {
        this.startX = x;
        this.startY = y;
        this.Width = w;
        this.Height = h;

        this.left = null;
        this.right = null;

    }
    public TreeNode[] verticalSplit(TreeNode t) {
        childWidth = RandomUtils.uniform(random, t.startX, t.startX + t.Width);
        if (childWidth > maxWidth || childWidth < minWidth) {
            return null;
        }
        t.left = new TreeNode(t.startX, t.startY, childWidth, t.Height);
        t.right = new TreeNode(childWidth, t.startY, t.Width - childWidth, t.Height);
        t.children[0] = t.left;
        t.children[1] = t.right;
        return t.children;
    }
    public TreeNode[] horizontalSplit(TreeNode t) {
        childHeight = RandomUtils.uniform(random, t.startY, t.startY + t.Height);
        if (childHeight > maxHeight || childHeight < minHeight) {
            return null;
        }
        t.left = new TreeNode(t.startX, t.startY + childHeight, t.Width, t.Height - childHeight);
        t.right = new TreeNode(t.startX, t.startY, t.Width, t.startY + childHeight);
        t.children[0] = t.left;
        t.children[1] = t.right;
        return t.children;
    }
    public boolean isLeaf(TreeNode t) {
        return (t.left == null) && (t.right == null);
    }
}
