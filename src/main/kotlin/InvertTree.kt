class InvertTree {

    class TreeNode(var value: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    fun invertTree(root: TreeNode?): TreeNode? {
        if(root == null) return null

        val newRight = root.left
        root.left = root.right
        root.right = newRight

        invertTree(root.left)
        invertTree(root.right)

        return root
    }

}