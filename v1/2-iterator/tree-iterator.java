import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

class Driver {
    public static void main(String[] args) {
        BinaryNode<Integer> tree = tree();
        List<Integer> list = list();
        System.out.println(sum(tree));
        System.out.println(sum(list));
    }

    private static Integer sum(Iterable<Integer> iterable) {
        Integer sum = 0;
        for (Integer elem : iterable) {
            sum += elem;
        }

        return sum;
    }

    private static Integer sumIterator(Iterator<Integer> iterator) {
        Integer sum = 0;
        while (iterator.hasNext()) {
            sum += iterator.next();
        }

        return sum;
    }

    private static List<Integer> list() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);

        return list;
    }

    private static BinaryNode<Integer> tree() {
        return new BinaryNode<Integer>(5,
                new BinaryNode<Integer>(3, new BinaryNode<Integer>(2, new BinaryNode<Integer>(1, null, null), null),
                        new BinaryNode<Integer>(4, null, null)),
                new BinaryNode<Integer>(7, new BinaryNode<Integer>(6, null, null), new BinaryNode<Integer>(9,
                        new BinaryNode<Integer>(10, null, null), new BinaryNode<Integer>(8, null, null))));
    }
}

class BinaryNode<T> implements Iterable<T> {

    public T Val;
    public BinaryNode<T> Left;
    public BinaryNode<T> Right;

    public BinaryNode(T val, BinaryNode<T> left, BinaryNode<T> right) {
        this.Val = val;
        this.Left = left;
        this.Right = right;
    }

    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator<T>(this);
    }
}

class BinaryTreeIterator<T> implements Iterator<T> {
    private Stack<BinaryNode<T>> _stack;

    public BinaryTreeIterator(BinaryNode<T> root) {
        this._stack = new Stack<>();

        if (root != null) {
            _pushLeftNodes(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !this._stack.empty();
    }

    @Override
    public T next() {
        if (_stack.empty()) {
            throw new IllegalAccessError("Cannot access empty iterator");
        }

        BinaryNode<T> node = _stack.pop();

        if (node.Right != null) {
            _pushLeftNodes(node.Right);
        }

        return node.Val;
    }

    private void _pushLeftNodes(BinaryNode<T> root) {
        _stack.push(root);
        while (root.Left != null) {
            root = root.Left;
            _stack.push(root);
        }
    }
}