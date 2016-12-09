package com.searchProject.dataStructures;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @param <V>
 * with char key
 */
public class Set<V> implements Iterable<V> {

    @Override
    public Iterator<V> iterator() {
        Iterator<V> it = new Iterator<V>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < members.size();
            }

            @Override
            public V next() {
                return members.get(currentIndex++).value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    class Member<V>{
        char key;
        V value;
        public Member(char key, V value){
            this.key = key;
            this.value = value;
        }
    }
    private ArrayList<Member<V>> members;
    public Set(){
        members = new ArrayList<Member<V>>();
    }

    public V put(char key, V value) {
        V existingMember = get(key);
        if (existingMember != null)
            return existingMember;

        members.add(new Member<V>(key, value));
        return value;

    }
    public V remove(char key){
        if(isEmpty())
            return null;

        for(int i=0; i<members.size(); i++)
            if (members.get(i).key == key){
                V v = members.get(i).value;
                members.remove(i);
                return v;
            }

        return null;
    }
    public V get(char key){
        if(isEmpty())
            return null;

        for(int i=0; i<members.size(); i++)
            if (members.get(i).key == key){
                return members.get(i).value;
            }

        return null; // not found
    }
    public V get(int index){
        if(index >= size())
            return null;
        return members.get(index).value;
    }
    public boolean containsKey(char key){
        if(get(key) == null)
            return false;
        return true;
    }
    public int size(){
        return members.size();
    }
    public boolean isEmpty(){
        return members.isEmpty();
    }
}
