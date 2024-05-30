package lab1;
// B2
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TextDigraph {
    // 主函数
    public static void main(String[] args) {
        String fileName = "lab1/p6input.txt";
        Map<String, Map<String, Integer>> directedGraph = buildDirectedGraph(fileName);
        System.out.println(directedGraph);
    }

    // 处理文本文件，并返回处理后的文本
    public static String processTextFile(String fileName) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append(" "); // 将换行符替换为空格
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 使用正则表达式替换非字母字符为空格或直接忽略
        String processedText = text.toString().replaceAll("[\\p{Punct}&&[^']]", " ").replaceAll("[^a-zA-Z\n]&&[^\\s]", "");
        return processedText;
    }

    // 读入文本并且构建有向图
    public static Map<String, Map<String, Integer>> buildDirectedGraph(String fileName) {
        // 声明Map变量
        Map<String, Map<String, Integer>> directedGraph = new HashMap<>();
        // 处理文本文件
        String line = processTextFile(fileName);
        String[] words = line.split("\\s+"); // 使用一个或多个空格分割单词
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i].toLowerCase(); // 将单词转换为小写
            String nextWord = words[i + 1].toLowerCase();    
            // 更新有向图的边及权重
            directedGraph.putIfAbsent(currentWord, new HashMap<>());
            directedGraph.get(currentWord).merge(nextWord, 1, Integer::sum);
        }
        return directedGraph;
    }

    // 展示有向图
    public static void showDirectedGraph(Map<String, Map<String, Integer>> directedGraph) {
        for (String startWord : directedGraph.keySet()) {
            System.out.println(startWord + " -> " + directedGraph.get(startWord));
        }
    }

    // 查询桥接词
    public static String queryBridgeWords(String word1, String word2, Map<String, Map<String, Integer>> directedGraph) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // 输入的word1或word2如果不在图中出现，则输出“No word1 or word2 in the graph!”
        if(!directedGraph.containsKey(word1) && !directedGraph.containsKey(word2)){
            String result = String.format("No \"%s\" and \"%s\" in the graph!", word1, word2);
            return result;
        }
        else if(!directedGraph.containsKey(word1)){
            String result = String.format("No \"%s\" in the graph!", word1);
            return result;
        }
        else if(!directedGraph.containsKey(word2)){
            String result = String.format("No \"%s\" in the graph!", word2);
            return result;
        }
        
        // 开始查询桥接词
        List<String> bridgeWords = new ArrayList<>();
        for (String bridgeWord : directedGraph.get(word1).keySet()) {
            if (directedGraph.containsKey(bridgeWord) && directedGraph.get(bridgeWord).containsKey(word2)) {
                bridgeWords.add(bridgeWord);
            }
        }
    
        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else if (bridgeWords.size() == 1) {
            return "The bridge word from " + word1 + " to " + word2 + " is: " + bridgeWords.get(0) + ".";
        } else {
            return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
        }        
    }

    // 最短路径计算（两个单词的版本）使用String...不满足定义的要求，使用两个方法分别实现
    public static String calcShortestPath (String word1, String word2, Map<String, Map<String, Integer>> directedGraph){
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // 输入的word1或word2如果不在图中出现，则输出“No word1 or word2 in the graph!”
        if(!directedGraph.containsKey(word1) && !directedGraph.containsKey(word2)){
            String result = String.format("No \"%s\" and \"%s\" in the graph!", word1, word2);
            return result;
        }
        else if(!directedGraph.containsKey(word1)){
            String result = String.format("No \"%s\" in the graph!", word1);
            return result;
        }
        else if(!directedGraph.containsKey(word2)){
            String result = String.format("No \"%s\" in the graph!", word2);
            return result;
        }
        
        // 开始计算最短路径（使用Dijkstra算法）
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        dist.put(word1, 0);
        for (String v : directedGraph.keySet()) {
            dist.putIfAbsent(v, Integer.MAX_VALUE);
            prev.putIfAbsent(v, null);
            for (String w : directedGraph.get(v).keySet()) {
                dist.putIfAbsent(w, Integer.MAX_VALUE);
                prev.putIfAbsent(w, null);
            }
        }
        while (!visited.contains(word2)) {
            String u = null;
            int minDist = Integer.MAX_VALUE;
            // 找到当前未访问节点中距离源点最近的节点
            for (String v : directedGraph.keySet()) {
                if (!visited.contains(v) && dist.get(v) < minDist) {
                    u = v;
                    minDist = dist.get(v);
                }
            }
            if (u == null) {
                break;
            }
            visited.add(u);
            // 更新距离
            for (String v : directedGraph.get(u).keySet()) {
                int alt = dist.get(u) + directedGraph.get(u).get(v);
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                }
            }
        }
        // 回溯路径
        List<String> path = new ArrayList<>();
        String current = word2;
        while (current!= null) {
            path.add(0, current);
            current = prev.get(current);
        }
        if (path.isEmpty()) {
            return "No path from " + word1 + " to " + word2 + "!";
        } else if (path.size() == 1) {
            return "The shortest path from " + word1 + " to " + word2 + " is: " + path.get(0) + "." + " Distance: " + dist.get(word2) + ".";
        } else {
            return "The shortest path from " + word1 + " to " + word2 + " is: " + String.join(" -> ", path) + "." + " Distance: " + dist.get(word2) + ".";
        }
    }

    // 最短路径计算（一个单词的版本）计算从word1到图中任意节点的最短路径
    public static String calcShortestPathOneWord (String word1,  Map<String, Map<String, Integer>> directedGraph){
        word1 = word1.toLowerCase();

        // 输入的word1如果不在图中出现，则输出“No word1 in the graph!”
        if(!directedGraph.containsKey(word1)){
            String result = String.format("No \"%s\" in the graph!", word1);
            return result;
        }
        
        // 开始计算最短路径（使用Dijkstra算法）
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        dist.put(word1, 0);
        for (String v : directedGraph.keySet()) {
            dist.putIfAbsent(v, Integer.MAX_VALUE);
            prev.putIfAbsent(v, null);
            for (String w : directedGraph.get(v).keySet()) {
                dist.putIfAbsent(w, Integer.MAX_VALUE);
                prev.putIfAbsent(w, null);
            }
        }
        while (true) {
            String u = null;
            int minDist = Integer.MAX_VALUE;
            // 找到当前未访问节点中距离源点最近的节点
            for (String v : directedGraph.keySet()) {
                if (!visited.contains(v) && dist.get(v) < minDist) {
                    u = v;
                    minDist = dist.get(v);
                }
            }
            if (u == null) {
                break;
            }
            visited.add(u);
            // 更新距离
            for (String v : directedGraph.get(u).keySet()) {
                int alt = dist.get(u) + directedGraph.get(u).get(v);
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                }
            }
        }
        // 逐项展示每一条路径
        StringBuilder text = new StringBuilder();
        for (String v: visited){
            if (v.equals(word1)) {
                continue;
            }
            List<String> path = new ArrayList<>();
            String current = v;
            while (current!= null) {
                path.add(0, current);
                current = prev.get(current);
            }
            if (path.isEmpty()) {
                text.append("No path from " + word1 + " to " + v + "!\n"); 
            } else if (path.size() == 1) {
                text.append("The shortest path from " + word1 + " to " + v + " is: " + path.get(0) + "." + " Distance: " + dist.get(v) + ".\n");
            } else {
                text.append("The shortest path from " + word1 + " to " + v + " is: " + String.join(" -> ", path) + "." + " Distance: " + dist.get(v) + ".\n");
            }
        }
        return text.toString();
    }
}

