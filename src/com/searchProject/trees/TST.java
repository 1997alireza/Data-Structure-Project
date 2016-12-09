package com.searchProject.trees;

import com.searchProject.ResultNode;
import com.searchProject.dataStructures.LinkedList;

import java.util.ArrayList;

public class TST implements Tree {
    private static class NodePointer{
        public Node node;
    }
    public static class Node implements Tree.Node{
        char data;
        boolean isEnd;
        Node left, middle, right;
        LinkedList<String> fileList;

        public Node(char data) {
            this.data = data;
            this.isEnd = false;
            this.left = null;
            this.middle = null;
            this.right = null;
            fileList = new LinkedList<>();
        }

        @Override
        public LinkedList<String> getFileList() {
            return fileList;
        }
    }

    private Node root;

    public TST() {
        root = null;
    }
    public boolean isEmpty() {
        return isEmpty(root);
    }
    private boolean isEmpty(Node r){
        if(r == null)
            return true;
        if(r.isEnd)
            return false;
        return isEmpty(r.left) & isEmpty(r.middle) & isEmpty(r.right);
    }
    public void makeEmpty() {
        root = null;
    }
    public Node insert(String word) {
        NodePointer finalNodePtr = new NodePointer();
        root = insert(root, word.toCharArray(), 0, finalNodePtr);
        return finalNodePtr.node;
    }
    private Node insert(Node r, char[] word, int ptr, final NodePointer finalNodePtr) {
        if (r == null)
            r = new Node(word[ptr]);

        if (word[ptr] < r.data)
            r.left = insert(r.left, word, ptr, finalNodePtr);
        else if (word[ptr] > r.data)
            r.right = insert(r.right, word, ptr, finalNodePtr);
        else {
            if (ptr + 1 < word.length)
                r.middle = insert(r.middle, word, ptr + 1, finalNodePtr);
            else {
                r.isEnd = true;
                finalNodePtr.node = r;
            }
        }
        return r;
    }

    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }
    private void delete(Node r, char[] word, int ptr) {
        if (r == null)
            return;

        if (word[ptr] < r.data)
            delete(r.left, word, ptr);
        else if (word[ptr] > r.data)
            delete(r.right, word, ptr);
        else {
            if (r.isEnd && ptr == word.length - 1) {
                r.isEnd = false;
            }

            else if (ptr + 1 < word.length)
                delete(r.middle, word, ptr + 1);
        }
    }

    public boolean search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    private boolean search(Node r, char[] word, int ptr) {
        if (r == null)
            return false;

        if (word[ptr] < r.data)
            return search(r.left, word, ptr);
        else if (word[ptr] > r.data)
            return search(r.right, word, ptr);
        else {
            if (r.isEnd && ptr == word.length - 1)
                return true;
            else if (ptr == word.length - 1)
                return false;
            else
                return search(r.middle, word, ptr + 1);
        }
    }

    public Node get(String word) {
        return get(root, word.toCharArray(), 0);
    }

    private Node get(Node r, char[] word, int ptr) {
        if (r == null)
            return null;

        if (word[ptr] < r.data)
            return get(r.left, word, ptr);
        else if (word[ptr] > r.data)
            return get(r.right, word, ptr);
        else {
            if (r.isEnd && ptr == word.length - 1)
                return r;
            else if (ptr == word.length - 1)
                return null;
            else
                return get(r.middle, word, ptr + 1);
        }
    }

    @Override
    public ArrayList<ResultNode> getResult() {
        ArrayList<ResultNode> result = new ArrayList<>();
        getResult(root, "", result);
        return result;
    }

    private void getResult(Node r, String word, ArrayList<ResultNode> result){
        if(r == null)
            return;
        if(r.isEnd && r.getFileList().size() != 0){
            ResultNode rNode = new ResultNode();
            rNode.word = word + r.data;
            for(String fileName : r.getFileList()){
                rNode.fileList.add(fileName);
            }
            result.add(rNode);
        }

        getResult(r.left, word, result);
        getResult(r.middle, word+r.data, result);
        getResult(r.right, word, result);
    }

    @Override
    public ArrayList<Tree.Node> getNodes() {
        ArrayList<Tree.Node> list = new ArrayList<>();
        getNodes(root, list);
        return list;
    }

    private void getNodes(Node r, ArrayList<Tree.Node> list){
        if(r == null)
            return;
        if(r.isEnd && r.getFileList().size() != 0) {
            list.add(r);
        }
        getNodes(r.left, list);
        getNodes(r.middle, list);
        getNodes(r.right, list);
    }

}