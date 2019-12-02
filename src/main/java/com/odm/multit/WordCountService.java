package com.odm.multit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

public class WordCountService {

  final static String INPUTFILE = "bible.txt";
  final static String OUTPUTFILE = "wordcount.txt";

  private static String removePunctuation(String input) {
    String rv = input.replaceAll("\\p{P}", "");
    rv = rv.toLowerCase();
    return rv;
  }

  private static List<String> ingestLines() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(INPUTFILE));
    List<String> lines = new ArrayList<String>();
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }
    reader.close();
    return lines;
  }

  private static Map<String, Integer> countWordsForLines(String input) {
    String inputWithoutPunctuation = removePunctuation(input);
    var words = inputWithoutPunctuation.split("\\s+");
    Map<String, Integer> rv = new HashMap<String, Integer>();
    for (String word : words) {
      rv.put(word, rv.getOrDefault(word, 0) + 1);
    }
    return rv;
  }

  private static Map<String, Integer> countWords(List<String> lines) {
    Map<String, Integer> rv = new HashMap<String, Integer>();
    lines.stream().forEach((line) -> {
      var lineDictionary = countWordsForLines(line);
      for (Map.Entry<String, Integer> entry : lineDictionary.entrySet()) {
        rv.put(entry.getKey(), rv.getOrDefault(entry.getKey(), 0) + entry.getValue());
      }      
    });
    return rv;
  }

  static void writeOutput(Map<String, Integer> map) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUTFILE));
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      writer.append(entry.getKey() + ": " + entry.getValue());
      writer.newLine();
    }
    writer.close();
  }

  public static void GenerateWordCount() {
    try {
      var lines = ingestLines();
      var map = countWords(lines);
      writeOutput(map);
    } catch (IOException e) {
      System.out.println("ODM io error");
      e.printStackTrace();
    }
  }

}