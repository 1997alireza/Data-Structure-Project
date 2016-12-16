package com.searchProject.trees;

import com.searchProject.ResultNode;
import com.searchProject.Tools;
import com.searchProject.dataStructures.LinkedList;

import java.util.ArrayList;

public class TST implements Tree {
    private static class NodePointer{
        public Node node;
    }
    public static class Node implements Tree.Node{
        char data;
        String word;
        boolean isEnd;
        Node left, middle, right;
        LinkedList<String> fileList;

        public Node(char data) {
            this.data = data;
            this.word = "";
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

        @Override
        public String getWord() {
            return word;
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
                r.word = new String(word);
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
                if(r.middle == null)
                    deleteNode(word);
            }

            else if (ptr + 1 < word.length)
                delete(r.middle, word, ptr + 1);
        }
    }

    private void deleteNode(char[] word){
        Node extraNode = new Node(' ');
        extraNode.middle = root;
        deleteNode(root, extraNode, word, 0, 0);
        root = extraNode.middle;
    }

    /**
     *
     * @param r
     * @param p
     * @param word
     * @param ptr
     * @param direction : -1 0 1 = left middle right
     * @return isEmpty
     */
    private boolean deleteNode(Node r, Node p,  char[] word, int ptr, int direction){
        if(r == null)
            return true;

        if(word[ptr] < r.data)
            deleteNode(r.left, r, word, ptr, -1);
        else if(word[ptr] > r.data)
            deleteNode(r.right, r, word, ptr, +1);
        else{
            boolean mustBeDelete = false;
            if(ptr == word.length -1 && r.middle == null){
                mustBeDelete = true;
            }
            else if(ptr + 1 < word.length){
                if(deleteNode(r.middle, r, word, ptr+1, 0) && !r.isEnd){
                    mustBeDelete = true;
                }
            }

            if(mustBeDelete){
                if(r.left == null && r.right == null){
                    switch (direction){
                        case -1:
                            p.left = null;
                            break;
                        case 0 :
                            p.middle = null;
                            break;
                        case +1:
                            p.right = null;
                    }
                }
                else if(r.left == null){
                    switch (direction){
                        case -1:
                            p.left = r.right;
                            break;
                        case 0 :
                            p.middle = r.right;
                            break;
                        case +1:
                            p.right = r.right;
                    }
                }
                else if(r.right == null){
                    switch (direction){
                        case -1:
                            p.left = r.left;
                            break;
                        case 0 :
                            p.middle = r.left;
                            break;
                        case +1:
                            p.right = r.left;
                    }
                }
                else{
                    Node pOfMinInRight = r;
                    Node minInRight = r.right;
                    while (minInRight.left != null){
                        pOfMinInRight = minInRight;
                        minInRight = minInRight.left;
                    }

                    r.data = minInRight.data;
                    r.word = minInRight.word;
                    r.isEnd = minInRight.isEnd;
                    r.middle = minInRight.middle;
                    r.fileList = minInRight.fileList;
                    if(r == pOfMinInRight)
                        pOfMinInRight.right = minInRight.right;
                    else
                        pOfMinInRight.left = minInRight.right;
                }
            }

        }

        if(r.left == null && r.middle == null && r.right == null && !r.isEnd)
            return true;

        return false;

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
        if(r.isEnd/* && r.getFileList().size() != 0*/){
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