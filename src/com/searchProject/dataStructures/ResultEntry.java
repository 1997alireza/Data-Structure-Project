package com.searchProject.dataStructures;

import java.util.ArrayList;

public class ResultEntry {
    public String word;
    public ArrayList<String> fileList;
    public ResultEntry(){
        fileList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return word;
    }

    public static class ResultException extends Exception{
        String message;
        public ResultException() {
            this.message = "Some Wrong Result Happened!";
        }

        public ResultException(String message) {
            super(message);
            this.message = message;
        }
        public ResultException(String message, Throwable cause) {
            super(message, cause);
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
