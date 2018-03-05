import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private int size = 0;

    private Node<Item> first = null;
    private Node<Item> last = first;

    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkAddItem(item);

        Node<Item> node = new Node<>(null, item, first);
        if (first != null) {
            first.prev = node;
        } else {
            last = node;
        }
        first = node;
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkAddItem(item);

        Node<Item> node = new Node<>(last, item, null);
        if (last != null) {
            last.next = node;
        } else {
            first = node;
        }
        last = node;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkRemoveItem();
        Item item = first.item;
        if (size > 1) {
            first.next.prev = null;
        } else {
            last = null;
        }
        first = first.next;
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkRemoveItem();
        Item item = last.item;
        if (size > 1) {
            last.prev.next = null;
        } else {
            first = null;
        }
        last = last.prev;
        size--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Itr();
    }


    private void checkAddItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkRemoveItem() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
    }

    private static class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;

        Node(Node<Item> prev, Item element, Node<Item> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }

    private class Itr implements Iterator<Item> {
        private Node<Item> node = first;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Item next() {
            Item tmp = node.item;
            node = node.next;
            return tmp;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

    }

    // unit testing (optional)
    public static void main(String[] args) {
        int[] items = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Deque<Integer> deque = new Deque<>();
        for (int item : items) {
            deque.addFirst(item);
        }
        for (int item : items) {
            System.out.println(deque.removeFirst());
        }
        for (int item : items) {
            deque.addFirst(item);
        }
        for (int item : items) {
            System.out.println(deque.removeLast());
        }

        for (int item : items) {
            deque.addFirst(item);
            deque.addLast(item);
        }


        for (int item : items) {
            System.out.println(deque.removeLast());
            System.out.println(deque.removeFirst());
        }
        for (int item : items) {
            deque.addFirst(item);
            deque.addLast(item);
        }
        Iterator<Integer> iter = deque.iterator();
        for (; iter.hasNext(); ) {
            System.out.println(iter.next());
        }
    }
}