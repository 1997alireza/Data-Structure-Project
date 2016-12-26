package com.searchProject.dataStructures;

import java.util.ArrayList;

public interface DataStructure {
    interface Member {
        LinkedList<String> getFileList();

        String getWord();
    }

    boolean isEmpty();

    void makeEmpty();

    Member insert(String word);

    void delete(String word);

    boolean search(String word);

    Member get(String word);

    ArrayList<ResultEntry> getResult();

    ArrayList<Member> getMembers();
}