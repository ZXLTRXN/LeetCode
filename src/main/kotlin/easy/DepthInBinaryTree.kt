package easy

import java.util.LinkedList
import java.util.Stack
import kotlin.math.max

/**
 * Example:
 * var ti = TreeNode(5)
 * var v = ti.`val`
 * Definition for a binary tree node.

 */


class DepthInBinaryTree {

    // bfs в ширину
    fun minDepth(root: TreeNode?): Int {
        if (root == null) return 0
        val queue = LinkedList<Pair<TreeNode, Int>>()

        queue.add(Pair(root, 1))

        while (queue.isNotEmpty()) {
            val (currentTree, depth) = queue.poll()
            val isLeaf = currentTree.left == null && currentTree.right == null
            if (isLeaf) {
                return depth
            }
            currentTree.left?.let { queue.add(Pair(it, depth + 1)) }
            currentTree.right?.let { queue.add(Pair(it, depth + 1)) }
        }
        throw IllegalArgumentException()
    }

    // dfs в глубину
    fun maxDepth(root: TreeNode?): Int {
        if (root == null) return 0
        return dfs(root, 0)
    }

    fun dfs(root: TreeNode?, depth: Int): Int {
        if (root == null) return depth
        return max(dfs(root.left, depth), dfs(root.right, depth)) + 1
    }

    fun dfsIterative(root: TreeNode?): Int {
        if (root == null) return 0

        var maxDepth = 0

        val stack = ArrayDeque<Pair<TreeNode, Int>>()
        stack.add(Pair(root, 1))

        while (stack.isNotEmpty()) {
            val (node, depth) = stack.removeLast()
            if (depth > maxDepth) maxDepth = depth

            // Порядок важен: сначала кладём правый, потом левый
            // чтобы левый обработался первым
            node.right?.let { stack.add(Pair(it, depth)) }
            node.left?.let { stack.add(Pair(it, depth)) }
        }
        return maxDepth
    }
}