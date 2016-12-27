package com.searchProject;

import com.searchProject.dataStructures.*;
import com.searchProject.dataStructures.hashMaps.CustomHashMap;
import com.searchProject.dataStructures.hashMaps.JavaHashMap;
import com.searchProject.dataStructures.trees.BST;
import com.searchProject.dataStructures.trees.TST;
import com.searchProject.dataStructures.trees.Tree;
import com.searchProject.dataStructures.trees.Trie;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private static DATA_STRUCTURE_TYPE dataStructureType;
    private static DataStructure [] stopWordsStructures;
    private static DataStructure wordsStructure;
    private static String directoryAddress = "";
    private static Stack<String> backwardCommands, forwardCommands;
    private static JTextArea resultField;
    public enum DATA_STRUCTURE_TYPE {
        BST, BalancedBST, TST, BalancedTST, Trie, CustomHashMap, JavaHashMap
    }
    public Menu(){
        setSize(640,615);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Inverted Index");
        setLayout(null);

        final JRadioButton TSTButton = new JRadioButton("TST");
        final JRadioButton BSTButton = new JRadioButton("BST");
        final JRadioButton TrieButton = new JRadioButton("Trie");
        final JRadioButton CustomHashMapButton = new JRadioButton("HashMap");
        final JRadioButton JavaHashMapButton = new JRadioButton("Java HashMap");


        TSTButton.setLocation(30,430);
        TSTButton.setSize(80,25);

        BSTButton.setLocation(120,430);
        BSTButton.setSize(80,25);

        TrieButton.setLocation(210,430);
        TrieButton.setSize(80,25);

        CustomHashMapButton.setLocation(380,430);
        CustomHashMapButton.setSize(80, 25);

        JavaHashMapButton.setLocation(490, 430);
        JavaHashMapButton.setSize(120, 25);

        final ButtonGroup dataStructureSelector = new ButtonGroup();
        dataStructureSelector.add(TSTButton);
        dataStructureSelector.add(BSTButton);
        dataStructureSelector.add(TrieButton);
        dataStructureSelector.add(CustomHashMapButton);
        dataStructureSelector.add(JavaHashMapButton);

        getContentPane().add(TSTButton);
        getContentPane().add(BSTButton);
        getContentPane().add(TrieButton);
        getContentPane().add(CustomHashMapButton);
        getContentPane().add(JavaHashMapButton);

        JCheckBox balancedTreeCheckOption = new JCheckBox("Balanced Tree");
        balancedTreeCheckOption.setSize(120, 25);
        balancedTreeCheckOption.setLocation(30, 460);
        balancedTreeCheckOption.setEnabled(false);
        getContentPane().add(balancedTreeCheckOption);

        TSTButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(TSTButton.isSelected())
                    balancedTreeCheckOption.setEnabled(true);
            }
        });
        BSTButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(BSTButton.isSelected())
                    balancedTreeCheckOption.setEnabled(true);
            }
        });
        TrieButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(TrieButton.isSelected())
                    balancedTreeCheckOption.setEnabled(false);
            }
        });
        CustomHashMapButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(CustomHashMapButton.isSelected())
                    balancedTreeCheckOption.setEnabled(false);
            }
        });
        JavaHashMapButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(JavaHashMapButton.isSelected())
                    balancedTreeCheckOption.setEnabled(false);
            }
        });


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
        text2.setFont(new Font("Monospaced", text2.getFont().getStyle(), 14));
        text2.setLocation(10,495);
        text2.setSize(300,15);
        getContentPane().add(text2);

        final JTextField commandTextField = new JTextField();
        commandTextField.setFont(new Font("Monospaced", text1.getFont().getStyle(), 13));
        commandTextField.setSize(611,25);
        commandTextField.setLocation(10, 515);
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
        buildButton.setLocation(10, 545);
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dataStructureSelector.getSelection() == null) {
                    resultField.setText("Error : Select a data structure");
                    return;
                }
                String directoryAddress = directoryTextField.getText();
                if(directoryAddress.equals("")) {
                    resultField.setText("Error : Choose a directory");
                    return;
                }
                File directory = new File(directoryAddress);
                if(!directory.exists()) {
                    resultField.setText("Error : Chosen directory is not exist");
                    return;
                }
                if(directory.isFile()){
                    resultField.setText("Error : Please choose a directory");
                    return;
                }

                Menu.directoryAddress = directory.getPath();

                if(BSTButton.isSelected()) {
                    if(balancedTreeCheckOption.isSelected())
                        dataStructureType = DATA_STRUCTURE_TYPE.BalancedBST;
                    else
                        dataStructureType = DATA_STRUCTURE_TYPE.BST;
                }
                else if(TSTButton.isSelected()) {
                    if(balancedTreeCheckOption.isSelected())
                        dataStructureType = DATA_STRUCTURE_TYPE.BalancedTST;
                    else
                        dataStructureType = DATA_STRUCTURE_TYPE.TST;
                }
                else if(TrieButton.isSelected()) {
                    dataStructureType = DATA_STRUCTURE_TYPE.Trie;
                }
                else if(CustomHashMapButton.isSelected()) {
                    dataStructureType = DATA_STRUCTURE_TYPE.CustomHashMap;
                }
                else if(JavaHashMapButton.isSelected()) {
                    dataStructureType = DATA_STRUCTURE_TYPE.JavaHashMap;
                }

                long cTime = System.nanoTime();
                wordsStructure = build(directory, dataStructureType, false);
                System.out.printf("Build Time : " + String.valueOf(System.nanoTime() - cTime) + "\n");
                if(wordsStructure instanceof Tree)
                    System.out.printf("Height of The Tree : " + ((Tree)wordsStructure).getHeight() + "\n") ;
                try {
                    testSearchTime();
                } catch (ResultEntry.ResultException e1) {
                    e1.printStackTrace();
                }

                showWordsList();

                balancedTreeCheckOption.setEnabled(false);
                buildButton.setEnabled(false);
                browseButton.setEnabled(false);
                directoryTextField.setEnabled(false);
                BSTButton.setEnabled(false);
                TSTButton.setEnabled(false);
                TrieButton.setEnabled(false);
                CustomHashMapButton.setEnabled(false);
                JavaHashMapButton.setEnabled(false);
                commandTextField.setEnabled(true);

            }
        });
        getContentPane().add(buildButton);

        final JButton resetButton = new JButton("Reset");
        resetButton.setSize(100, 25);
        resetButton.setLocation(180, 545);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wordsStructure != null)
                    wordsStructure.makeEmpty();
                wordsStructure = null;
                System.gc();
                browseButton.setEnabled(true);
                buildButton.setEnabled(true);
                directoryTextField.setEnabled(true);
                BSTButton.setEnabled(true);
                TSTButton.setEnabled(true);
                TrieButton.setEnabled(true);
                CustomHashMapButton.setEnabled(true);
                JavaHashMapButton.setEnabled(true);
                if(BSTButton.isSelected() || TSTButton.isSelected())
                    balancedTreeCheckOption.setEnabled(true);
                resultField.setText("");
                commandTextField.setText("");
                commandTextField.setEnabled(false);

                inAppFileList.clear();
            }
        });
        getContentPane().add(resetButton);

        final JButton helpButton = new JButton("Help");
        helpButton.setSize(100, 25);
        helpButton.setLocation(350, 545);
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"...", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        getContentPane().add(helpButton);

        final JButton exitButton = new JButton("Exit");
        exitButton.setSize(100, 25);
        exitButton.setLocation(520, 545);
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
        stopWordsStructures = new DataStructure[7];
        stopWordsStructures[DATA_STRUCTURE_TYPE.BST.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.BST, true);
        stopWordsStructures[DATA_STRUCTURE_TYPE.BalancedBST.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.BalancedBST, true);
        stopWordsStructures[DATA_STRUCTURE_TYPE.TST.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.TST, true);
        stopWordsStructures[DATA_STRUCTURE_TYPE.BalancedTST.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.BalancedTST, true);
        stopWordsStructures[DATA_STRUCTURE_TYPE.Trie.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.Trie, true);
        stopWordsStructures[DATA_STRUCTURE_TYPE.CustomHashMap.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.CustomHashMap, true);
        stopWordsStructures[DATA_STRUCTURE_TYPE.JavaHashMap.ordinal()] = build(new File(stopWordsAddress), DATA_STRUCTURE_TYPE.JavaHashMap, true);
        buildButton.setEnabled(true);

    }

    private DataStructure build(File directory, DATA_STRUCTURE_TYPE type, boolean isAStopWordsStructure){
        DataStructure dataStructure = null;
        switch (type){
            case BST:
                dataStructure = new BST(false);
                break;
            case BalancedBST:
                dataStructure = new BST(true);
                break;
            case TST:
                dataStructure = new TST(false);
                break;
            case BalancedTST:
                dataStructure = new TST(true);
                break;
            case Trie:
                dataStructure = new Trie();
                break;
            case CustomHashMap:
                dataStructure = new CustomHashMap();
                break;
            case JavaHashMap:
                dataStructure = new JavaHashMap();
                break;
        }

        File[] listOfFiles = {directory};
        if(!isAStopWordsStructure)
            listOfFiles = directory.listFiles();

        for(File file : listOfFiles)
            addFile(file, dataStructure, type, isAStopWordsStructure);

        return dataStructure;
    }

    private int addFile(File file, DataStructure dataStructure, DATA_STRUCTURE_TYPE type, boolean isAStopWordsStructure){
        if (file == null) return 0;
        if(!file.exists() || !file.isFile()) return -1;
        int iOfExtension = file.getName().lastIndexOf('.');
        if (iOfExtension < 0 || !file.getName().substring(iOfExtension+1).equals("txt"))
            return -1;

        String fileName = file.getName().substring(0, iOfExtension);
        if(!isAStopWordsStructure && inAppFileList.contains(fileName))
            return -2;

        try {
            if(!isAStopWordsStructure/* && !inAppFileList.contains(fileName)*/)
                inAppFileList.add(fileName);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                String s = scanner.next();
                String[] words = s.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (word.length() != 0 && (isAStopWordsStructure || !stopWordsStructures[type.ordinal()].search(word))) {
                        DataStructure.Member member = dataStructure.insert(word);
                        LinkedList <String> fileList = member.getFileList();
                        if(!fileList.contains(fileName))
                            fileList.add(fileName);
                    }
                }

            }

        } catch (FileNotFoundException e) {
        }

        return 1;
    }

    private int deleteFile(String fileName, DataStructure dataStructure){
        if(!inAppFileList.contains(fileName))
            return -1;
        inAppFileList.remove(fileName);
        for(DataStructure.Member node : dataStructure.getMembers())
            for(int i=0; i<node.getFileList().size(); i++){
                if(node.getFileList().get(i).equals(fileName)){
                    node.getFileList().remove(i);
                    if(node.getFileList().size() == 0)
                        dataStructure.delete(node.getWord());
                    break;
                }
            }
        return 0;

    }
    private void showWordsList(){
        resultField.setText("");
        resultField.append("\n");
        ArrayList<ResultEntry> result = wordsStructure.getResult();
        for(ResultEntry rNode : result){
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
        DataStructure.Member member = wordsStructure.get(word);
        if(member == null)
            return null;
        return member.getFileList();
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
            if(!stopWordsStructures[dataStructureType.ordinal()].search(word)){
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
                int addResult = addFile(new File(directoryAddress+"\\"+param+".txt"), wordsStructure, dataStructureType, false);
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
                int delResult = deleteFile(param, wordsStructure);
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
                if(deleteFile(param, wordsStructure) == -1)
                    break;
                int addRes = addFile(new File(directoryAddress+"\\"+param+".txt"), wordsStructure, dataStructureType, false);
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
                if(commandType.toCharArray()[0] != '-')
                    resultField.setText("Error : command \"" + commandName + "\" requires an argument");
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
                    default:
                        resultField.setText("Error : command \"" + commandName + "\" hasn't an argument \'" + commandType + "\'");
                }
                break;
            case "search":
                String mainParam = param;
                param = param.toLowerCase();
                if(param.length() < 2 || param.charAt(0) != '"' || param.charAt(param.length()-1) != '"') {
                    resultField.setText("Error : command \"" + commandName + "\" requires a parameter between \" \"");
                    break;
                }
                if(commandType.toCharArray()[0] != '-')
                    resultField.setText("Error : command \"" + commandName + "\" requires an argument");
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
                    default:
                        resultField.setText("Error : command \"" + commandName + "\" hasn't an argument \'" + commandType.substring(1) + "\'");

                }
                break;

            default:
                resultField.setText("Error : command \""+ commandName +"\" not found");

        }

    }

    private void testSearchTime() throws ResultEntry.ResultException {
        ArrayList<ResultEntry> result = wordsStructure.getResult();

        long startTime = System.nanoTime();
        for(ResultEntry rEntry : result)
            if (!wordsStructure.search(rEntry.word)) {
                throw new ResultEntry.ResultException("The word should be in the structure");
            }

        System.out.println("Mean Search Time : " + ((System.nanoTime() - startTime) / result.size()) + " ---> For " + result.size() + " Words\n");

    }


}

