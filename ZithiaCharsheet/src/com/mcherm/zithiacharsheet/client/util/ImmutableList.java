package com.mcherm.zithiacharsheet.client.util;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Your basic immutable linked list class.
 */
public class ImmutableList<T> implements Iterable<T> {

    /**
     * Returns true if the list is empty.
     */
    public boolean isEmpty() {
        return true;
    }

    /**
     * Returns the first item in the list. An error if you
     * invoke it on the empty list.
     */
    public T head() {
        // Cannot pop an empty list!
        throw new IllegalStateException();
    }

    /**
     * Returns the rest of list after the first element.
     * If the list has only 1 item then this returns an
     * empty list.
     */
    public ImmutableList<? extends T> tail() {
        // Cannot get tail of an empty list
        throw new IllegalStateException();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            public boolean hasNext() {
                return false;
            }
            public T next() {
                throw new NoSuchElementException();
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        boolean firstItem = true;
        for (T item : this) {
            if (firstItem) {
                firstItem = false;
            } else {
                result.append(", ");
            }
            result.append(item);
        }
        result.append("]");
        return result.toString();
    }
    
    /**
     * Adds an item to the list.
     * @param item the new item to add
     * @return a new list where item is the first element and the
     *   rest is this list.
     */
    public ImmutableList<T> add(T item) {
        return new ListNode<T>(item, this);
    }
    
    /**
     * Removes the first occurrence of an item from the list if it is in the list.
     * @param item an item to be removed. Must not be null. The equals() method
     *   will be used to test for a match.
     * @return a new list which is the same except that it is missing
     *   the first instance of item. If item did not exist in the original
     *   list then it returns the original list.
     */
    @SuppressWarnings("unchecked") // because I can't get the type of the lists right while iterating
    public ImmutableList<T> remove(T item) {
        ImmutableList<T> leadingItems = new ImmutableList<T>();
        ImmutableList<T> remainingList = this;
        while (! remainingList.isEmpty()) {
            T currentItem = remainingList.head();
            remainingList = (ImmutableList<T>) remainingList.tail();
            if (item.equals(currentItem)) {
                ImmutableList<T> result = remainingList;
                while (! leadingItems.isEmpty()) {
                    T x = leadingItems.head();
                    leadingItems = (ImmutableList<T>) leadingItems.tail();
                    result = result.add(x);
                }
                return result;
            } else {
                leadingItems = leadingItems.add(currentItem);
            }
        }
        return this; // no match was found
    }

    /**
     * A static version of the add() method which can be used to
     * create a list where the type of newer items is broader than
     * the type of the tail of the list.
     * @param <T>
     * @param tail the rest of the list
     * @param item the new head of the list
     * @return a new list where item is the head and the rest is rest.
     */
    public static <T> ImmutableList<T> add(final ImmutableList<? extends T> tail, final T item) {
      return new ListNode<T>(item, tail);
    }

    private static class ListNode<T> extends ImmutableList<T> {
        private final T head;
        private final ImmutableList<? extends T> tail;

        /** Constructor. */
        private ListNode( final T head, final ImmutableList<? extends T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
      
        @Override
        public T head() {
            return head;
        }

        @Override
        public ImmutableList<? extends T> tail() {
            return tail;
        }
      
        @Override
        public Iterator<T> iterator() {
            final ImmutableList<? extends T> thisList = this;
            return new Iterator<T>() {
                private ImmutableList<? extends T> currentPosition = thisList;
                public boolean hasNext() {
                    return ! currentPosition.isEmpty();
                }
                public T next() {
                    if (currentPosition.isEmpty()) {
                        throw new NoSuchElementException();
                    }
                    T result = currentPosition.head();
                    currentPosition = currentPosition.tail();
                    return result;
                }
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

}
