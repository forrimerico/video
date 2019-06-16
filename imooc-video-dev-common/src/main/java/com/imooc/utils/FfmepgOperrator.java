package com.imooc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FfmepgOperrator {

    private String ffmepgExePath;

    public FfmepgOperrator(String ffmepgExePath) {
        this.ffmepgExePath = ffmepgExePath;
    }

    public void addBgm(String mp4Path, String mp3Path, Double sec, String outputPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add(this.ffmepgExePath);
        command.add("-i");
        command.add(mp4Path);
        command.add("-i");
        command.add(mp3Path);
        command.add("-t");
        command.add(String.valueOf(sec));
        command.add("-y");
        command.add(outputPath);
        processBuilder.command(command);
        Process process = processBuilder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {}
        if (errorStream != null) {
            errorStream.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }

    public static void main(String[] args) {
//        FfmepgOperrator ffmepgOperrator = new FfmepgOperrator("E:\\dev\\ffmpeg-20190614-dd357d7-win64-static\\bin\\ffmpeg.exe");
//        try {
//            ffmepgOperrator.convert("E:\\dev\\shortvideo\\c467870fecea33b69d9b82a209637319.mp4", "E:\\dev\\shortvideo\\test.avi");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
