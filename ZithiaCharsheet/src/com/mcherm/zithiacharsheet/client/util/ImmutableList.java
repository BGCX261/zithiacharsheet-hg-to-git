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

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public T next() {
                throw new NoSuchElementException();
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
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
     * A static version of the add() method which can be used to
     * create a list where the type of newer items is broader than
     * the type of the tail of the list.
     * @param <T>
     * @param item the new head of the list
     * @param rest the rest of the list
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
                @Override
                public boolean hasNext() {
                    return ! currentPosition.tail().isEmpty();
                }
                @Override
                public T next() {
                    T result = currentPosition.head();
                    currentPosition = currentPosition.tail();
                    return result;
                }
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

}
