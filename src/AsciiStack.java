/**
 * Created by mkamleithner on 11/29/14.
 */
public class AsciiStack {

    private AsciiStackNode head;

    public int size() {
        return empty() ? 0 : head.size();
    }

    public boolean empty() {
        return head == null;
    }

    public void push(AsciiImage image) {
        AsciiStackNode node = new AsciiStackNode(image, null);
        if (empty()) {
            head = node;
        } else head.last().next = node;

    }

    public AsciiImage pop() {
        if (empty()) return null;
        if (this.size() == 1) {
            AsciiImage img = head.image;
            head = null;
            return img;
        }
        AsciiStackNode currentElement = head;
        AsciiImage image = null;
        while (image == null) {
            if (currentElement.next.next == null) {
                image = currentElement.next.image;
                currentElement.next = null;

            }
            currentElement = currentElement.next;
        }
        return image;
    }

    public AsciiImage peek() {
        if (empty()) return null;

        AsciiStackNode currentElement = head;
        while (currentElement.next != null) {
            currentElement = currentElement.next;
        }
        return currentElement.image;
    }

    private class AsciiStackNode {

        private AsciiImage image;
        private AsciiStackNode next;

        public AsciiStackNode(AsciiImage image, AsciiStackNode next) {
            this.image = image;
            this.next = next;
        }

        public int size() {
            return 1 + ((next == null) ? 0 : next.size());
        }

        private AsciiStackNode last() {
            if (next == null) {
                return this;
            } else return next.last();
        }

    }

}
