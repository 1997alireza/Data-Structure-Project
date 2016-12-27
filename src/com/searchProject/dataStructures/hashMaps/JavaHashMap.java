package com.searchProject.dataStructures.hashMaps;

import com.searchProject.dataStructures.DataStructure;
import com.searchProject.dataStructures.ResultEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JavaHashMap implements DataStructure{
    HashMap<String, CustomHashMap.Member> hashMap;

    public JavaHashMap(){
        hashMap = new HashMap<>();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public void makeEmpty() {
        hashMap.clear();
    }

    @Override
    public Member insert(String key) {
        Member existMember = hashMap.get(key);
        if(existMember != null)
            return existMember;

        Member member = new CustomHashMap.Entry(key);
        hashMap.put(key, member);
        return member;
    }

    @Override
    public void delete(String key) {
        hashMap.remove(key);
    }

    @Override
    public boolean search(String key) {
        return  hashMap.containsKey(key);
    }

    @Override
    public Member get(String key) {
        return hashMap.get(key);
    }

    @Override
    public ArrayList<ResultEntry> getResult() {
        ArrayList<ResultEntry> result = new ArrayList<>();
        for(Map.Entry mapEntry : this.hashMap.entrySet()){
            CustomHashMap.Entry entry = (CustomHashMap.Entry)mapEntry.getValue();
            ResultEntry rNode = new ResultEntry();
            rNode.word = entry.getWord();
            for (String fileName : entry.getFileList()) {
                rNode.fileList.add(fileName);
            }

            result.add(rNode);
        }
        return result;
    }

    @Override
    public ArrayList<Member> getMembers() {
        ArrayList<Member> list = new ArrayList<>();
        for(Map.Entry mapEntry : this.hashMap.entrySet())
            list.add((CustomHashMap.Entry)mapEntry.getValue());

        return list;
    }
}
