package com.searchProject.dataStructures;

import java.util.ArrayList;

/**
 * Its a HashMap with String keys
 */
public class CustomHashMap implements DataStructure{

    private LinkedList<Entry>[] hashTable;
    private static final int HASH_TABLE_SIZE = 113;

    public static class Entry implements Member {
        String key;
        LinkedList<String> fileList;

        public Entry(String key) {
            this.key = key;
            fileList = new LinkedList<>();
        }

        public LinkedList<String> getFileList() {
            return fileList;
        }

        public String getWord() {
            return key;
        }
    }

    public CustomHashMap() {
        hashTable = new LinkedList[HASH_TABLE_SIZE];
    }

    public boolean isEmpty() {
        for (LinkedList<Entry> entryList : hashTable)
            if (entryList != null && entryList.size() != 0)
                return false;
        return true;
    }

    public void makeEmpty() {
        for (int i = 0; i < hashTable.length; i++)
            hashTable[i] = null;
    }

    public Entry insert(String key) {
        int hashCode = hashFunction(key);
        LinkedList<Entry> entryList = hashTable[hashCode];

        if (entryList == null) {
            entryList = new LinkedList<>();
            hashTable[hashCode] = entryList;
        }

        for (Entry entry : hashTable[hashCode])
            if (entry.key.equals(key))
                return entry;

        hashTable[hashCode].add(new Entry(key));
        return hashTable[hashCode].getLast();
    }

    public void delete(String key) {
        LinkedList<Entry> entryList = hashTable[hashFunction(key)];
        if (entryList == null)
            return;
        for (int i = 0; i < entryList.size(); i++)
            if (entryList.get(i).key.equals(key)) {
                entryList.remove(i);
                return;
            }
    }

    public boolean search(String key) {
        LinkedList<Entry> entryList = hashTable[hashFunction(key)];
        if (entryList == null)
            return false;
        for (Entry entry : entryList)
            if (entry.key.equals(key))
                return true;
        return false;
    }

    public Entry get(String key) {
        LinkedList<Entry> entryList = hashTable[hashFunction(key)];
        if (entryList == null)
            return null;
        for (Entry entry : entryList)
            if (entry.key.equals(key))
                return entry;
        return null;
    }

    public ArrayList<ResultEntry> getResult() {
        ArrayList<ResultEntry> result = new ArrayList<>();
        for (LinkedList<Entry> entryList : hashTable) {
            if (entryList != null)
                for (Entry entry : entryList) {
                    ResultEntry rNode = new ResultEntry();
                    rNode.word = entry.key;
                    for (String fileName : entry.getFileList()) {
                        rNode.fileList.add(fileName);
                    }
                    result.add(rNode);
                }
        }
        return result;
    }

    public ArrayList<Member> getMembers() {
        ArrayList<Member> list = new ArrayList<>();
        for (LinkedList<Entry> entryList : hashTable) {
            if (entryList != null)
                for (Entry entry : entryList)
                    list.add(entry);
        }
        return list;
    }

    private static int hashFunction(String key) {
        int hashCode = 1;
        for (char c : key.toCharArray())
            hashCode = hashCode * 17 + c;
        if(hashCode < 0)
            hashCode *= -1;
        return hashCode % HASH_TABLE_SIZE;
    }
}
