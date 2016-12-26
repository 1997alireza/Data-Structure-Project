package com.searchProject.dataStructures;


import java.util.Iterator;

public class LinkedList<E> implements Iterable<E>{

    private Node<E> root, last;
    private int size;
    public LinkedList(){
        root = null;
        last = null;
        size = 0;
    }

    public void add(E object){
        if(object == null)
            return;

        size++;
        if(size == 1){
            root = new Node<>(object);
            last = root;
            return;
        }

        last.forwardChild = new Node<>(object);
        last.forwardChild.backwardChild = last;
        last = last.forwardChild;
    }

    public boolean contains(E object){
        return contains(object, root);
    }

    private boolean contains(E object, Node r){
        if(r == null)
            return false;
        return r.data.equals(object) || contains(object, r.forwardChild);
    }

    public int size(){
        return size;
    }
    public E get(int index){
        if(index >= size)
            return null;
        Node<E> r = root;
        for(int i=0; i < index; i++)
            r = r.forwardChild;

        return r.data;
    }

    public E getLast(){
        return last.data;
    }

    public E remove(int index){
        if(index >= size || size == 0)
            return null;
        Node<E> r = root;
        for(int i=0; i < index; i++)
            r = r.forwardChild;

        if(index == 0){
            if(size == 1)
                root = null;
            else {
                r.forwardChild.backwardChild = null;
                root = r.forwardChild;
            }
        }
        else if(index == size()-1){
            r.backwardChild.forwardChild = null;
            last = r.backwardChild;
        }
        else {
            r.backwardChild.forwardChild = r.forwardChild;
            r.forwardChild.backwardChild = r.backwardChild;
        }

        size--;
        return r.data;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it = new Iterator<E>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    private static class Node<E>{
        public Node<E> backwardChild, forwardChild;
        public E data;
        public Node(E data){
            backwardChild = null;
            forwardChild = null;
            this.data = data;
        }
    }
}
