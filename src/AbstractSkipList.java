import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class AbstractSkipList {
    final protected Node head;
    final protected Node tail;

    public AbstractSkipList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        increaseHeight();
    }

    public void increaseHeight() {
        head.addLevel(tail, null);
        head.size.set(head.height, getSize(head, head.height));
        tail.addLevel(null, head);
    }

    abstract Node find(int key);

    abstract public int generateHeight();

    public Node search(int key) {
        Node curr = find(key);

        return curr.key() == key ? curr : null;
    }

    public Node insert(int key) {
        int nodeHeight = generateHeight();

        while (nodeHeight > head.height()) {
            increaseHeight();
        }

        Node prevNode = find(key);
        if (prevNode.key() == key) {
            return null;
        }

        Node newNode = new Node(key);

        for (int level = 0; level <= nodeHeight && prevNode != null; ++level) {
            Node nextNode = prevNode.getNext(level);
            
            newNode.addLevel(nextNode, prevNode);
            prevNode.setNext(level, newNode);
            nextNode.setPrev(level, newNode);

            while (prevNode != null && prevNode.height() == level) {
                prevNode = prevNode.getPrev(level);
            }
        }
        Node curNode = newNode;
        int level = 0;
        while(head.height >= level) {
        	if (curNode.height >= level) {
        		curNode.size.set(level, getSize(curNode, level));
        		level++;
        	} else {
        		curNode = curNode.getPrev(level - 1);
        	}
        }
        return newNode;
    }
    
    public int getSize(Node n, int level) {
    	if(level == 0) {
    		if(n == head)
    			return 0;
    		return 1;
    	}
    	Node cur = n;
    	int size = 0;
    	while(cur != null && cur != n.getNext(level)) {
    		size += cur.size.get(level - 1);
    		cur = cur.getNext(level - 1);
    	}
    	return size;
    }

    public boolean delete(Node node) {
        for (int level = 0; level <= node.height(); ++level) {
            Node prev = node.getPrev(level);
            Node next = node.getNext(level);
            prev.setNext(level, next);
            next.setPrev(level, prev);
        }

        return true;
    }

    public int predecessor(Node node) {
        return node.getPrev(0).key();
    }

    public int successor(Node node) {
        return node.getNext(0).key();
    }

    public int minimum() {
        if (head.getNext(0) == tail) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return head.getNext(0).key();
    }

    public int maximum() {
        if (tail.getPrev(0) == head) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return tail.getPrev(0).key();
    }

    private void levelToString(StringBuilder s, int level) {
        s.append("H    ");
        Node curr = head.getNext(level);

        while (curr != tail) {
            s.append(curr.key);
            s.append("    ");
            
            curr = curr.getNext(level);
        }

        s.append("T\n");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int level = head.height(); level >= 0; --level) {
            levelToString(str, level);
        }

        return str.toString();
    }

    public static class Node {
        final private List<Node> next;
        final private List<Node> prev;
        final private List<Integer> size; //added
        private int height;
        final private int key;

        public Node(int key) {
            next = new ArrayList<>();
            prev = new ArrayList<>();
            size = new ArrayList<Integer>(); //added
            this.height = -1;
            this.key = key;
        }

        public Node getPrev(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return prev.get(level);
        }

        public Node getNext(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return next.get(level);
        }

        public void setNext(int level, Node next) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.next.set(level, next);
        }

        public void setPrev(int level, Node prev) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.prev.set(level, prev);
        }

        public void addLevel(Node next, Node prev) {
            ++height;
            this.next.add(next);
            this.prev.add(prev);
            this.size.add(0); //added
        }

        public int height() { return height; }
        public int key() { return key; }
    }
}
