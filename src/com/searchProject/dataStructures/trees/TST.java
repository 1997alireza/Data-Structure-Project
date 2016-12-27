package com.searchProject.dataStructures.trees;

import com.searchProject.dataStructures.ResultEntry;
import com.searchProject.Tools;
import com.searchProject.dataStructures.LinkedList;

import java.util.ArrayList;

public class TST extends Tree {
    private boolean balancedTree;
    private static class NodePointer{
        public Node node;
    }
    public static class Node extends Tree.Node{
        char data;
        String word;
        boolean isEnd;
        Node left, middle, right;
        LinkedList<String> fileList;
        int balanceFactor; // right height - left height

        public Node(char data) {
            this.data = data;
            this.word = "";
            this.isEnd = false;
            this.left = null;
            this.middle = null;
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
            return word;
        }
    }

    private Node root;

    public TST(boolean balancedTree) {
        root = null;
        this.balancedTree = balancedTree;
    }
    public boolean isEmpty() {
        return isEmpty(root);
    }
    private boolean isEmpty(Node r){
        return r == null || !r.isEnd && isEmpty(r.left) && isEmpty(r.middle) && isEmpty(r.right);
    }
    public void makeEmpty() {
        root = null;
    }
    public Node insert(String word) {
        NodePointer finalNodePtr = new NodePointer();
        root = insert(root, word.toCharArray(), 0, finalNodePtr);
        if(balancedTree)
            balance();  // for balanced tree
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
        if(balancedTree)
            balance();  // for balanced tree
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

        return r.left == null && r.middle == null && r.right == null && !r.isEnd;

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
            return r.isEnd && ptr == word.length - 1 || ptr != word.length - 1 && search(r.middle, word, ptr + 1);
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
    public ArrayList<ResultEntry> getResult() {
        ArrayList<ResultEntry> result = new ArrayList<>();
        getResult(root, "", result);
        return result;
    }

    private void getResult(Node r, String word, ArrayList<ResultEntry> result){
        if(r == null)
            return;
        if(r.isEnd/* && r.getFileList().size() != 0*/){
            ResultEntry rNode = new ResultEntry();
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
    public ArrayList<Member> getMembers() {
        ArrayList<Member> list = new ArrayList<>();
        getNodes(root, list);
        return list;
    }

    private void getNodes(Node r, ArrayList<Member> list){
        if(r == null)
            return;
        if(r.isEnd && r.getFileList().size() != 0) {
            list.add(r);
        }
        getNodes(r.left, list);
        getNodes(r.middle, list);
        getNodes(r.right, list);
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node r){
        if(r == null)
            return 0;

        return Tools.max(getHeight(r.left), getHeight(r.middle), getHeight(r.right)) + 1;
    }

    private void balance(){
        balance(root, null, 0);
    }

    private int getHeightWithoutMiddleChild(Node r){
        if(r == null)
            return 0;

        return Tools.max(getHeightWithoutMiddleChild(r.left), getHeightWithoutMiddleChild(r.right)) + 1;
    }

    /**
     *
     * @param r
     * @return int This return height of the tree
     */
    private int balance(Node r, Node p, int direction){ // direction: -1 0 1 : left middle right
        if(r == null)
            return 0;
        int leftHeight = balance(r.left, r, -1);
        int rightHeight = balance(r.right, r, 1);
        balance(r.middle, r, 0);
        r.balanceFactor = rightHeight - leftHeight;

        int subTreeHeight = Tools.max(leftHeight, rightHeight) + 1;

        if(r.balanceFactor == +2){
            if(r.right.balanceFactor == +1){ //RR
                Node b = r.right;

                int AlHeight = getHeightWithoutMiddleChild(r.left); // a == r
                int BrHeight = getHeightWithoutMiddleChild(b.right);
                int BlHeight = getHeightWithoutMiddleChild(b.left);


                if(p == null)
                    root = b;
                else {
                    if (direction == -1)
                        p.left = b;
                    else if(direction == 1)
                        p.right = b;
                    else
                        p.middle = b;
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

                int AlHeight = getHeightWithoutMiddleChild(r.left);
                int ClHeight = getHeightWithoutMiddleChild(c.left);
                int CrHeight = getHeightWithoutMiddleChild(c.right);
                int BrHeight = getHeightWithoutMiddleChild(b.right);
                int AHeight = Tools.max(AlHeight, ClHeight) + 1;
                int BHeight = Tools.max(CrHeight, BrHeight) + 1;


                if(p == null)
                    root = c;
                else {
                    if (direction == -1)
                        p.left = c;
                    else if(direction == 1)
                        p.right = c;
                    else
                        p.middle = c;
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

                int ArHeight = getHeightWithoutMiddleChild(r.right); // a == r
                int BrHeight = getHeightWithoutMiddleChild(b.right);
                int BlHeight = getHeightWithoutMiddleChild(b.left);


                if(p == null)
                    root = b;
                else {
                    if (direction == -1)
                        p.left = b;
                    else if(direction == 1)
                        p.right = b;
                    else
                        p.middle = b;
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

                int ArHeight = getHeightWithoutMiddleChild(r.right);
                int ClHeight = getHeightWithoutMiddleChild(c.left);
                int CrHeight = getHeightWithoutMiddleChild(c.right);
                int BlHeight = getHeightWithoutMiddleChild(b.left);
                int AHeight = Tools.max(ArHeight, CrHeight) + 1;
                int BHeight = Tools.max(ClHeight, BlHeight) + 1;


                if(p == null)
                    root = c;
                else {
                    if (direction == -1)
                        p.left = c;
                    else if(direction == 1)
                        p.right = c;
                    else
                        p.middle = c;
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