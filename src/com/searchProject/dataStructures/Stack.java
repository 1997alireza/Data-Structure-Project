package com.searchProject.dataStructures;

import java.util.*;

public class Stack<E> {
    private E[] theData;
    private int topOfStack = -1;
    private static final int INITIAL_CAPACITY = 10;
    private int size = 0;
    private int capacity = 0;

    public Stack(int initCapacity) {
        capacity = initCapacity;
        theData = (E[]) new Object[capacity];
    }

    public Stack() {
        this(INITIAL_CAPACITY);
    }

    public E push(E e) {
        if (size == capacity) {
            reallocate();
        }
        theData[size] = e;
        size++;
        topOfStack++;

        return e;
    }
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return theData[topOfStack];
    }
    public E pop() {
        if(isEmpty())
            return null;
        E result = theData[topOfStack];
        theData[topOfStack] = null;
        size--;
        topOfStack--;
        if (size <= (capacity / 4) && capacity >= INITIAL_CAPACITY) {
            shrink();
        }
        return result;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    private void reallocate() {
        capacity *= 2;
        theData = Arrays.copyOf(theData, capacity);
    }
    private void shrink() {
        capacity /= 2;
        theData = Arrays.copyOf(theData, capacity);
    }
    public int size() {
        return size;
}}