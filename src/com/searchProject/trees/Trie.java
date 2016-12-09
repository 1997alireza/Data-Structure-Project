package com.searchProject.trees;

import com.searchProject.ResultNode;
import com.searchProject.dataStructures.Set;
import com.searchProject.dataStructures.LinkedList;

import java.util.ArrayList;


public class Trie implements Tree {
    public static class Node implements Tree.Node{
        char data;
        boolean isEnd;
        Set<Node> childs;
        LinkedList<String> fileList;

        public Node(char data){
            this.data = data;
            this.childs = new Set<>();
            this.isEnd = false;
            fileList = new LinkedList<>();
        }

        @Override
        public LinkedList<String> getFileList() {
            return fileList;
        }
    }

    private Node root;

    public Trie(){
        root = new Node(' ');
    }
    public boolean isEmpty() {
        return isEmpty(root);
    }
    private boolean isEmpty(Node r){
        if(r == null)
            return true;
        if(r.isEnd)
            return false;
        boolean isEmpty = true;
        for(Node n : r.childs){
            isEmpty = isEmpty & isEmpty(n);
        }
        return isEmpty;
    }
    public void makeEmpty() {
        root.childs = new Set<>();
    }
    public Node insert(String word) {
        return insert(root, word.toCharArray(), 0);
    }
    private Node insert(Node r, char[] word, int ptr){
        Node n = new Node(word[ptr]);
        n = r.childs.put(word[ptr], n);
        if(word.length == ptr + 1) {
            n.isEnd = true;
            return n;
        }
        else
            return insert(n, word, ptr+1);
    }

    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }
    private void delete(Node r, char[] word, int ptr){
        if(r == null)
            return;
        if(word.length == ptr)
            r.isEnd = false;
        else
            delete(r.childs.get(word[ptr]), word, ptr+1);
    }

    public boolean search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    private boolean search(Node r, char[] word, int ptr){
        if(r == null)
            return false;
        if(word.length == ptr)
            return true;
        else
            return search(r.childs.get(word[ptr]), word, ptr+1);

    }

    public Node get(String word) {
        return get(root, word.toCharArray(), 0);
    }

    private Node get(Node r, char[] word, int ptr){
        if(r == null)
            return null;
        if(word.length == ptr)
            return r;
        else
            return get(r.childs.get(word[ptr]), word, ptr+1);

    }

    @Override
    public ArrayList<ResultNode> getResult() {
        ArrayList<ResultNode> result = new ArrayList<>();
        getResult(root, "", result);
        return result;
    }
    private void getResult(Node r, String word, ArrayList<ResultNode> result){
        word += r.data;
        if(r.isEnd &&  r.getFileList().size() != 0){
            ResultNode rNode = new ResultNode();
            rNode.word = word;
            for(String fileName : r.getFileList()){
                rNode.fileList.add(fileName);
            }
            result.add(rNode);
        }
        for(Node child : r.childs){
            getResult(child, word, result);
        }
    }

    @Override
    public ArrayList<Tree.Node> getNodes() {
        ArrayList<Tree.Node> list = new ArrayList<>();
        getNodes(root, list);
        return list;
    }

    private void getNodes(Node r, ArrayList<Tree.Node> list){
        if(r.isEnd &&  r.getFileList().size() != 0){
            list.add(r);
        }

        for(Node child : r.childs){
            getNodes(child, list);
        }
    }
}

