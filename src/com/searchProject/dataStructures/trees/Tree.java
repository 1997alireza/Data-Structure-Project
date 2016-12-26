package com.searchProject.dataStructures.trees;

import com.searchProject.dataStructures.DataStructure;
import com.searchProject.dataStructures.LinkedList;
import com.searchProject.dataStructures.ResultEntry;
import java.util.ArrayList;

public abstract class Tree implements DataStructure {
    public static abstract class Node implements DataStructure.Member {
        public abstract LinkedList<String> getFileList();

        public abstract String getWord();
    }

    public abstract boolean isEmpty();

    public abstract void makeEmpty();

    public abstract Node insert(String word);

    public abstract void delete(String word);

    public abstract boolean search(String word);

    public abstract Node get(String word);

    public abstract ArrayList<ResultEntry> getResult();

    public abstract ArrayList<Member> getMembers();

    public abstract int getHeight();
}
