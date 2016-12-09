package com.searchProject.trees;

import com.searchProject.ResultNode;
import com.searchProject.dataStructures.LinkedList;


import java.util.ArrayList;

public interface Tree {
    interface Node{
        LinkedList<String> getFileList();
    }
    boolean isEmpty();
    void makeEmpty();
    Node insert(String word);
    void delete(String word) ;
    boolean search(String word);
    Node get(String word);
    ArrayList<ResultNode> getResult();
    ArrayList<Node> getNodes();
}
