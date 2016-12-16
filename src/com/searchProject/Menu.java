package com.searchProject;

import com.searchProject.dataStructures.Stack;
import com.searchProject.trees.BST;
import com.searchProject.trees.TST;
import com.searchProject.trees.Tree;
import com.searchProject.trees.Trie;
import com.searchProject.dataStructures.LinkedList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Menu extends JFrame{
    private static ArrayList<String> inAppFileList;
    private static TREE treeType;
    private static Tree []stopWordsTrees;
    private static Tree wordsTree;
    private static String directoryAddress = "";
    private static Stack<String> backwardCommands, forwardCommands;
    private static JTextArea resultField;
    public enum TREE{
        BST, TST, Trie
    }
    public Menu(){
        setSize(640,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Inverted Index");
        setLayout(null);

        final JRadioButton TSTButton = new JRadioButton("TST");
        final JRadioButton BSTButton = new JRadioButton("BST");
        final JRadioButton TrieButton = new JRadioButton("Trie");

        TSTButton.setLocation(400,430);
        TSTButton.setSize(60,25);

        BSTButton.setLocation(480,430);
        BSTButton.setSize(60,25);

        TrieButton.setLocation(560,430);
        TrieButton.setSize(60,25);

        final ButtonGroup treeSelector = new ButtonGroup();
        treeSelector.add(TSTButton);
        treeSelector.add(BSTButton);
        treeSelector.add(TrieButton);

        getContentPane().add(TSTButton);
        getContentPane().add(BSTButton);
        getContentPane().add(TrieButton);

        final JLabel text1 = new JLabel("Please enter address.");
        text1.setFont(new Font("Monospaced", text1.getFont().getStyle(), 17));
        text1.setLocation(10,10);
        text1.setSize(500,15);
        getContentPane().add(text1);

        final JTextField directoryTextField = new JTextField();
        directoryTextField.setFont(new Font("Monospaced", text1.getFont().getStyle(), 13));
        directoryTextField.setSize(500,25);
        directoryTextField.setLocation(10, 30);
        getContentPane().add(directoryTextField);


        final JButton browseButton = new JButton("Browse");
        browseButton.setSize(100, 25);
        browseButton.setLocation(520, 30);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setLocation(500, 500);
                fc.setSize(500, 500);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setCurrentDirectory(new File("c:/Users/AliReza/Desktop/"));
                int result = fc.showOpenDialog(browseButton);
                if(result == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null)
                    directoryTextField.setText(fc.getSelectedFile().getPath());
            }
        });
        getContentPane().add(browseButton);

        resultField = new JTextArea();
        int resultSX = 620, resultSY = 360, resultLX = 10, resultLY = 65;
        resultField.setSize(resultSX, resultSY);
        resultField.setLocation(resultLX, resultLY);
        resultField.setEditable(false);
        resultField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        resultField.setFont(new Font("Monospaced", text1.getFont().getStyle(), 14));
        JScrollPane SP = new JScrollPane(resultField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        SP.setBounds(resultLX,resultLY,resultSX,resultSY);
        getContentPane().add(SP);

        final JLabel text2 = new JLabel("Please enter your command :");
        text2.setFont(new Font("Monospaced", text2.getFont().getStyle(), 17));
        text2.setLocation(10,435);
        text2.setSize(300,15);
        getContentPane().add(text2);

        final JTextField commandTextField = new JTextField();
        commandTextField.setFont(new Font("Monospaced", text1.getFont().getStyle(), 13));
        commandTextField.setSize(611,25);
        commandTextField.setLocation(10, 455);
        commandTextField.setEnabled(false);
        commandTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_ENTER:
                        String command = commandTextField.getText();
                        if(command.length() == 0)
                            return;
                        doCommand(command);
                        if(backwardCommands.size() > 0)
                            forwardCommands.push(backwardCommands.pop());
                        while (forwardCommands.size()>1)
                            backwardCommands.push(forwardCommands.pop());
                        backwardCommands.push(commandTextField.getText());
                        backwardCommands.push(forwardCommands.pop());
                        commandTextField.setText("");
                        break;
                    case KeyEvent.VK_UP:
                        if(backwardCommands.size() > 1) {
                            forwardCommands.push(backwardCommands.pop());
                            commandTextField.setText(backwardCommands.peek());
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(forwardCommands.size() > 0) {
                            backwardCommands.push(forwardCommands.pop());
                            commandTextField.setText(backwardCommands.peek());
                        }
                }
            }
        });
        getContentPane().add(commandTextField);

        final JButton buildButton = new JButton("Build");
        buildButton.setSize(100, 25);
        buildButton.setLocation(10, 485);
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(treeSelector.getSelection() == null) {
                    return;
                }
                String directoryAddress = directoryTextField.getText();
                if(directoryAddress.equals("")) {
                    return;
                }
                File directory = new File(directoryAddress);
                if(!directory.exists() || directory.isFile())
                    return;

                Menu.directoryAddress = directory.getPath();

                if(BSTButton.isSelected()) {
                    wordsTree = build(directory, TREE.BST, false);
                    treeType = TREE.BST;
                }
                else if(TSTButton.isSelected()) {
                    wordsTree = build(directory, TREE.TST, false);
                    treeType = TREE.TST;
                }
                else if(TrieButton.isSelected()) {
                    wordsTree = build(directory, TREE.Trie, false);
                    treeType = TREE.Trie;
                }

                showWordsList();

                browseButton.setEnabled(false);
                directoryTextField.setEnabled(false);
                BSTButton.setEnabled(false);
                TSTButton.setEnabled(false);
                TrieButton.setEnabled(false);
                commandTextField.setEnabled(true);

            }
        });
        getContentPane().add(buildButton);

        final JButton resetButton = new JButton("Reset");
        resetButton.setSize(100, 25);
        resetButton.setLocation(180, 485);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButton.setEnabled(true);
                directoryTextField.setEnabled(true);
                BSTButton.setEnabled(true);
                TSTButton.setEnabled(true);
                TrieButton.setEnabled(true);
                resultField.setText("");
                commandTextField.setText("");
                commandTextField.setEnabled(false);

                inAppFileList.clear();
            }
        });
        getContentPane().add(resetButton);

        final JButton helpButton = new JButton("Help");
        helpButton.setSize(100, 25);
        helpButton.setLocation(350, 485);
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"...", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        getContentPane().add(helpButton);

        final JButton exitButton = new JButton("Exit");
        exitButton.setSize(100, 25);
        exitButton.setLocation(520, 485);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        getContentPane().add(exitButton);


        backwardCommands = new Stack<>();
        forwardCommands = new Stack<>();
        backwardCommands.push("");
        inAppFileList = new ArrayList<>();

        setVisible(true);
        buildButton.setEnabled(false);
        final String stopWordsAddress = "src/res/StopWords.txt";
        stopWordsTrees = new Tree[3];
        stopWordsTrees[TREE.BST.ordinal()] = build(new File(stopWordsAddress), TREE.BST, true);
        stopWordsTrees[TREE.TST.ordinal()] = build(new File(stopWordsAddress), TREE.TST, true);
        stopWordsTrees[TREE.Trie.ordinal()] = build(new File(stopWordsAddress), TREE.Trie, true);
        buildButton.setEnabled(true);

    }

    private Tree build(File directory, TREE type, boolean isAStopWordsTree){
        Tree tree = null;
        switch (type){
            case BST:
                tree = new BST();
                break;
            case TST:
                tree = new TST();
                break;
            case Trie:
                tree = new Trie();
                break;
        }

        File[] listOfFiles = {directory};
        if(!isAStopWordsTree)
            listOfFiles = directory.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            addFile(listOfFiles[i], tree, type, isAStopWordsTree);
        }

        return tree;
    }

    private int addFile(File file, Tree tree, TREE type, boolean isAStopWordsTree){
        if (file == null) return 0;
        if(!file.exists() || !file.isFile()) return -1;
        int iOfExtension = file.getName().lastIndexOf('.');
        if (iOfExtension < 0 || !file.getName().substring(iOfExtension+1).equals("txt"))
            return -1;

        String fileName = file.getName().substring(0, iOfExtension);
        if(!isAStopWordsTree && inAppFileList.contains(fileName))
            return -2;

        try {
            if(!isAStopWordsTree/* && !inAppFileList.contains(fileName)*/)
                inAppFileList.add(fileName);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                String s = scanner.next();
                String[] words = s.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (word.length() != 0 && (isAStopWordsTree || !stopWordsTrees[type.ordinal()].search(word))) {
                        Tree.Node node = tree.insert(word);
                        LinkedList <String> fileList = node.getFileList();
                        if(!fileList.contains(fileName))
                            fileList.add(fileName);
                    }
                }

            }

        } catch (FileNotFoundException e) {
        }

        return 1;
    }

    private int deleteFile(String fileName, Tree tree){
        if(!inAppFileList.contains(fileName))
            return -1;
        inAppFileList.remove(fileName);
        for(Tree.Node node : tree.getNodes())
            for(int i=0; i<node.getFileList().size(); i++){
                if(node.getFileList().get(i).equals(fileName)){
                    node.getFileList().remove(i);
                    if(node.getFileList().size() == 0)
                        tree.delete(node.getWord());
                    break;
                }
            }
        return 0;

    }
    private void showWordsList(){
        resultField.setText("");
        resultField.append("\n");
        ArrayList<ResultNode> result = wordsTree.getResult();
        for(ResultNode rNode : result){
            resultField.append("|" + rNode.word + " -> ");
            for(int i=0; i < rNode.fileList.size()-1 ; i++) {
                resultField.append(rNode.fileList.get(i) + ", ");
            }
            if(rNode.fileList.size() != 0)
                resultField.append(rNode.fileList.get(rNode.fileList.size()-1) + "\n");
        }
        resultField.append(" - - - \n Number of words = " + result.size());
    }
    private void showFilesListedInApp(){
        resultField.setText("");
        resultField.append("\n - - - \n");
        for(int i=0; i < inAppFileList.size()-1; i++){
            resultField.append(inAppFileList.get(i) + ", ");
        }
        resultField.append(inAppFileList.get(inAppFileList.size()-1) + "\n");
        resultField.append("Number of listed docs = " + inAppFileList.size());
    }
    private void showFilesListedInDir(){
        resultField.setText("");
        resultField.append("\n - - - \n");
        File[] listOfFiles = (new File(Menu.directoryAddress)).listFiles();
        int num = 0;
        if(listOfFiles != null)
            for(File file : listOfFiles){
                if(file.isFile()) {
                    int iOfExtension = file.getName().lastIndexOf('.');
                    if (iOfExtension >= 0 && file.getName().substring(iOfExtension + 1).equals("txt")) {
                        resultField.append(file.getName().substring(0, iOfExtension) + ", ");
                        num++;
                    }
                }
            }
        resultField.append("\nNumber of all docs = " + num);
    }

    private LinkedList<String> search(String word){
        Tree.Node node = wordsTree.get(word);
        if(node == null)
            return null;
        return node.getFileList();
    }
    private boolean showSearchWordResult(String word){
        resultField.setText("");
        resultField.append("\n - - - \n");
        boolean isFined = false;
        try {
            for (String fileName : search(word)) {
                resultField.append(fileName + ", ");
                isFined = true;
            }
        }catch (java.lang.NullPointerException e){
        }

        return isFined;
    }
    private boolean showSearchExpResult(String exp){
        resultField.setText("");
        resultField.append("\n - - - \n");
        String[] words = exp.split("\\W+");
        ArrayList<LinkedList<String>> filesLists = new ArrayList<>();
        for(String word : words){
            if(!stopWordsTrees[treeType.ordinal()].search(word)){
                filesLists.add(search(word));
            }
        }
        if(filesLists.size() == 0) return false;

        LinkedList<String> interSectionResult;
        if(filesLists.size() == 1)
            interSectionResult = filesLists.get(0);
        else {
            interSectionResult = new LinkedList<>();

            for (String fileName : filesLists.get(0)) {
                boolean isCommon = true;
                for(int i=1; i<filesLists.size(); i++)
                    if(!filesLists.get(i).contains(fileName)) {
                        isCommon = false;
                        break;
                    }
                if(isCommon)
                    interSectionResult.add(fileName);
            }
        }

        if(interSectionResult == null || interSectionResult.size() == 0)
            return false;
        for (String fileName : interSectionResult) {
            resultField.append(fileName + ", ");
        }

        return true;
    }
    private void doCommand(String command){
        String commandName, commandType = "", param = "";
        int lastIndex = command.indexOf(' ');
        if(lastIndex < 0)
            commandName = command;
        else {
            commandName = command.substring(0, lastIndex);

            if (commandName.equals("list") || commandName.equals("search")) {
                try {
                    commandType = command.substring(lastIndex + 1, command.indexOf(' ', lastIndex + 1));
                    lastIndex = command.indexOf(' ', lastIndex + 1);
                } catch (java.lang.StringIndexOutOfBoundsException e) {
                    commandType = command.substring(lastIndex + 1);
                    lastIndex = command.length();
                }
            }

            try {
                param = command.substring(lastIndex + 1);
            } catch (java.lang.StringIndexOutOfBoundsException e) {
                param = "";
            }
        }
        switch (commandName){
            case "exit":
                System.exit(0);
            case "add":
                int addResult = addFile(new File(directoryAddress+"\\"+param+".txt"), wordsTree, treeType, false);
                switch (addResult){
                    case -1:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n err: document not found.");
                        break;
                    case -2:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n err: document already exists.");
                        break;
                    case 1:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n document "+param+" successfully added.");
                }
                break;
            case "del":
                int delResult = deleteFile(param, wordsTree);
                switch (delResult){
                    case -1:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n err: document not found.");
                        break;
                    case 0:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n document "+param+" successfully deleted.");
                        break;


                }
                break;
            case "update":
                if(deleteFile(param, wordsTree) == -1)
                    break;
                int addRes = addFile(new File(directoryAddress+"\\"+param+".txt"), wordsTree, treeType, false);
                switch (addRes){
                    case -1:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n err: document is deleted now.");
                        break;
                    case 1:
                        resultField.setText("");
                        resultField.append("\n - - - - -\n document "+param+" successfully updated.");
                }
                break;
            case "list":
                switch (commandType){
                    case "-w":
                        showWordsList();
                        break;
                    case "-l":
                        showFilesListedInApp();
                        break;
                    case "-f":
                        showFilesListedInDir();
                        break;
                }
                break;
            case "search":
                String mainParam = param;
                param = param.toLowerCase();
                if(param.length() < 2 || param.charAt(0) != '"' || param.charAt(param.length()-1) != '"')
                    break;
                switch (commandType){
                    case "-w":
                        boolean isFindWord = showSearchWordResult(param.substring(1, param.length()-1));
                        if(!isFindWord){
                            resultField.setText("");
                            resultField.append("\n - - - \n Word "+ mainParam + " is not found.");
                        }

                        break;
                    case "-s":
                        boolean isFindExp = showSearchExpResult(param.substring(1, param.length()-1));
                        if(!isFindExp){
                            resultField.setText("");
                            resultField.append("\n - - - \n String "+ mainParam +" is not found");
                        }

                        break;
                }
                break;
        }

    }


}

