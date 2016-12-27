package com.searchProject.dataStructures.trees;

import com.searchProject.dataStructures.ResultEntry;
import com.searchProject.Tools;
import com.searchProject.dataStructures.LinkedList;

import java.util.ArrayList;

public class BST extends Tree {
    private boolean balancedTree;
    private static class NodePointer{
        public Node node;
    }
    public static class Node extends Tree.Node{
        String data;
        Node left,right;
        LinkedList<String> fileList;
        int balanceFactor; // right height - left height
        public Node(String data){
            this.data = data;
            this.left = null;
            this.right = null;
            fileList = new LinkedList<>();
            balanceFactor = 0;
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
    public BST(boolean balancedTree){
        root = null;
        this.balancedTree = balancedTree;
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

        if(balancedTree)
            balance(); // for balanced tree
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

        if(balancedTree)
            balance(); // for balanced tree
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
    public ArrayList<ResultEntry> getResult() {
        ArrayList<ResultEntry> result = new ArrayList<>();
        getResult(root, result);
        return result;
    }

    private void getResult(Node r, ArrayList<ResultEntry> result){
        if(r == null)
            return;
//        if(r.getFileList().size() != 0) {
            ResultEntry rNode = new ResultEntry();
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
    public ArrayList<Member> getMembers() {
        ArrayList<Member> list = new ArrayList<>();
        getNodes(root, list);
        return list;
    }

    private void getNodes(Node r, ArrayList<Member> list){
        if(r == null)
            return;
        if(r.getFileList().size() != 0)
            list.add(r);
        getNodes(r.left, list);
        getNodes(r.right, list);
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node r){
        if(r == null)
            return 0;

        return Tools.max(getHeight(r.left), getHeight(r.right)) + 1;
    }

    private void balance(){
        balance(root, null, false);
    }

    /**
     *
     * @param r
     * @return int This return height of the tree
     */
    private int balance(Node r, Node p, boolean isLeft){
        if(r == null)
            return 0;
        int leftHeight = balance(r.left, r, true);
        int rightHeight = balance(r.right, r, false);
        r.balanceFactor = rightHeight - leftHeight;

        int subTreeHeight = Tools.max(leftHeight, rightHeight) + 1;

        if(r.balanceFactor == +2){
            if(r.right.balanceFactor == +1){ //RR
                Node b = r.right;

                int AlHeight = getHeight(r.left); // a == r
                int BrHeight = getHeight(b.right);
                int BlHeight = getHeight(b.left);


                if(p == null)
                    root = b;
                else {
                    if (isLeft)
                        p.left = b;
                    else
                        p.right = b;
                }

                r.right = b.left;
                b.left = r;

                r.balanceFactor =  BlHeight - AlHeight;
                int AHeight = Tools.max(AlHeight, BlHeight) + 1;
                b.balanceFactor = BrHeight - AHeight;

                subTreeHeight = Tools.max(AHeight, BrHeight) + 1;
            }
            else{ //RL
                Node b = r.right;
                Node c = b.left;

                int AlHeight = getHeight(r.left);
                int ClHeight = getHeight(c.left);
                int CrHeight = getHeight(c.right);
                int BrHeight = getHeight(b.right);
                int AHeight = Tools.max(AlHeight, ClHeight) + 1;
                int BHeight = Tools.max(CrHeight, BrHeight) + 1;


                if(p == null)
                    root = c;
                else {
                    if (isLeft)
                        p.left = c;
                    else
                        p.right = c;
                }

                r.right = c.left;
                b.left = c.right;
                c.left = r;
                c.right = b;

                b.balanceFactor = BrHeight - CrHeight;
                r.balanceFactor = ClHeight - AlHeight;
                c.balanceFactor = BHeight - AHeight;

                subTreeHeight = Tools.max(AHeight, BHeight) + 1;
            }
        }
        else if(r.balanceFactor == -2){
            if(r.left.balanceFactor == -1){ //LL
                Node b = r.left;

                int ArHeight = getHeight(r.right); // a == r
                int BrHeight = getHeight(b.right);
                int BlHeight = getHeight(b.left);


                if(p == null)
                    root = b;
                else {
                    if (isLeft)
                        p.left = b;
                    else
                        p.right = b;
                }

                r.left = b.right;
                b.right = r;

                r.balanceFactor =  ArHeight - BrHeight;
                int AHeight = Tools.max(ArHeight, BrHeight) + 1;
                b.balanceFactor = AHeight - BlHeight;

                subTreeHeight = Tools.max(AHeight, BlHeight) + 1;


            }
            else{ //LR
                Node b = r.left;
                Node c = b.right;

                int ArHeight = getHeight(r.right);
                int ClHeight = getHeight(c.left);
                int CrHeight = getHeight(c.right);
                int BlHeight = getHeight(b.left);
                int AHeight = Tools.max(ArHeight, CrHeight) + 1;
                int BHeight = Tools.max(ClHeight, BlHeight) + 1;


                if(p == null)
                    root = c;
                else {
                    if (isLeft)
                        p.left = c;
                    else
                        p.right = c;
                }

                r.left = c.right;
                b.right = c.left;
                c.right = r;
                c.left = b;


                b.balanceFactor = ClHeight - BlHeight;
                r.balanceFactor = ArHeight - CrHeight;
                c.balanceFactor = AHeight - BHeight;

                subTreeHeight = Tools.max(AHeight, BHeight) + 1;
            }
        }

        return subTreeHeight;

    }
}
