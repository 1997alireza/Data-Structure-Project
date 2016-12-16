package com.searchProject.trees;

import com.searchProject.ResultNode;
import com.searchProject.Tools;
import com.searchProject.dataStructures.LinkedList;

import java.util.ArrayList;

public class BST implements Tree {
    private static class NodePointer{
        public Node node;
    }
    public static class Node implements Tree.Node{
        String data;
        Node left,right;
        LinkedList<String> fileList;
        public Node(String data){
            this.data = data;
            this.left = null;
            this.right = null;
            fileList = new LinkedList<>();
        }

        @Override
        public LinkedList<String> getFileList() {
            return fileList;
        }

        @Override
        public String getWord() {
            return data;
        }
    }
    private Node root;
    public BST(){
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }
    public void makeEmpty() {
        root = null;
    }
    public Node insert(String word) {
        NodePointer finalNodePtr = new NodePointer();
        root = insert(root, word, finalNodePtr);
        return finalNodePtr.node;
    }

    private Node insert(Node r, String word, final NodePointer finalNodePtr){
        if(r == null) {
            finalNodePtr.node = new Node(word);
            return finalNodePtr.node;
        }

        if(word.equals(r.data)) {
            finalNodePtr.node = r;
            return r;
        }

        if(Tools.isBigger(word, r.data))
            r.right = insert(r.right, word, finalNodePtr);

        else
            r.left = insert(r.left, word, finalNodePtr);

        return r;

    }

    public void delete(String word) {
        Node extraNode = new Node("");
        extraNode.right = root;
        Node p = extraNode;
        Node r = root;
        boolean isRightChild = true;
        while(r != null){
            if(r.data.equals(word)){
                if(r.right == null && r.left == null){
                    if(isRightChild)
                        p.right = null;
                    else
                        p.left = null;
                }
                else if(r.right == null){
                    if(isRightChild)
                        p.right = r.left;
                    else
                        p.left = r.left;
                }
                else if(r.left == null){
                    if(isRightChild)
                        p.right = r.right;
                    else
                        p.left = r.right;
                }
                else {
                    Node pOfMinInRight = r;
                    Node minInRight = r.right;
                    while (minInRight.left != null){
                        pOfMinInRight = minInRight;
                        minInRight = minInRight.left;
                    }
                    r.data = minInRight.data;
                    r.fileList = minInRight.fileList;
                    if(r == pOfMinInRight)
                        pOfMinInRight.right = minInRight.right;
                    else
                        pOfMinInRight.left = minInRight.right;
                }


                break;
            }
            else if(Tools.isBigger(word, r.data)){
                p = r;
                r = r.right;
                isRightChild = true;
            }
            else {
                p = r;
                r = r.left;
                isRightChild = false;
            }


        }

        root = extraNode.right;
    }

    public boolean search(String word){
        return search(root, word);
    }

    private boolean search(Node r, String word){
        if(r == null)
            return false;

        if(r.data.equals(word))
            return true;

        if(Tools.isBigger(word,r.data))
            return search(r.right, word);

        return search(r.left, word);
    }

    public Node get(String word){
        return get(root, word);
    }

    private Node get(Node r, String word){
        if(r == null || r.data.equals(word))
            return r;

        if(Tools.isBigger(word,r.data))
            return get(r.right, word);

        return get(r.left, word);
    }

    @Override
    public ArrayList<ResultNode> getResult() {
        ArrayList<ResultNode> result = new ArrayList<>();
        getResult(root, result);
        return result;
    }

    private void getResult(Node r, ArrayList<ResultNode> result){
        if(r == null)
            return;
//        if(r.getFileList().size() != 0) {
            ResultNode rNode = new ResultNode();
            rNode.word = r.data;
            for (String fileName : r.getFileList()) {
                rNode.fileList.add(fileName);
            }
            result.add(rNode);
//        }

        getResult(r.left, result);
        getResult(r.right, result);
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
        if(r.getFileList().size() != 0)
            list.add(r);
        getNodes(r.left, list);
        getNodes(r.right, list);
    }



}
