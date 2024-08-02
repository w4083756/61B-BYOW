package byow.Core;

import java.util.List;
import java.util.Random;

public class BSPTree {
    public int Width;
    public int Height;
    public TreeNode root;
    public List<TreeNode> leafs;
    public Random random = new Random();
    public BSPTree() {
        root = new TreeNode(0, 0, Width, Height);
        CreateBSPTree(root);
    }

    public BSPTree[] CreateBSPTree(TreeNode curr) {
        int r = RandomUtils.uniform(random, 0, 2);
        if (curr.isLeaf(curr)) {
        }
        if (r == 0) {
            curr.verticalSplit(curr);
        }
        if (r == 1) {
            curr.horizontalSplit(curr);
        }
        CreateBSPTree(curr.children[0]);
        CreateBSPTree(curr.children[1]);
        return null;
    }
}
