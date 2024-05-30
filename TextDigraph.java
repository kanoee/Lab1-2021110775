package lab1;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TextDigraph {
    // 主函数
    public static void main(String[] args) {
        String fileName = "C:\\Users\\64126\\Desktop\\软件工程\\p6input.txt";
        String newfile = "C:\\Users\\64126\\Desktop\\软件工程\\newinput.txt";
        String fileContent = readFileToString(newfile);
        Map<String, Map<String, Integer>> directedGraph = buildDirectedGraph(fileName);
        //System.out.println(queryBridgeWords("a","n", directedGraph));
        //System.out.println(calcShortestPath("a","v",directedGraph));
        //System.out.println(calcShortestPath("","",directedGraph));
        //showDirectedGraph(calcShortestPathOneWord("",directedGraph);
        //generateNewText(newfile,directedGraph);
        //System.out.println(generateNewText(fileContent,directedGraph));
        System.out.println(randomWalk(directedGraph));
    }

    // 读取文件内容到字符串
    public static String readFileToString(String fileName) {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append(System.lineSeparator()); // 添加换行符
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
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

    // 展示有向图 p2
    public static void showDirectedGraph(Map<String, Map<String, Integer>> directedGraph) {
        int totalInnerMapCount = 0;
        for (Map<String, Integer> innerMap : directedGraph.values()) {
            totalInnerMapCount += innerMap.size();
        }
        int i;
        ArrayList<String> dg = new ArrayList<>();
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        // 遍历外层Map的每个值
        for (Map.Entry<String, Map<String, Integer>> entry : directedGraph.entrySet()) {
            String outerKey = entry.getKey();
            Map<String, Integer> innerMap = entry.getValue();
            // 遍历内层Map的每个键值对
            for (Map.Entry<String, Integer> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                Integer innerValue = innerEntry.getValue();
                dg.add(outerKey + "->" + innerKey + " " + "[" + "label" + "=" + innerValue + "]");
            }
        }
        String stringdg = String.join("\n", dg);
        gv.add(stringdg);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File("DirectedGraph" + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }

    // 查询桥接词
    public static String queryBridgeWords(String word1, String word2, Map<String, Map<String, Integer>> directedGraph) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // 输入的word1或word2如果不在图中出现，则输出“No word1 or word2 in the graph!”
        if (!directedGraph.containsKey(word1) && !directedGraph.containsKey(word2)) {
            String result = String.format("No \"%s\" and \"%s\" in the graph!", word1, word2);
            return result;
        } else if (!directedGraph.containsKey(word1)) {
            String result = String.format("No \"%s\" in the graph!", word1);
            return result;
        } else if (!directedGraph.containsKey(word2)) {
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
    public static int flagqueryBridgeWords(String word1, String word2, Map<String, Map<String, Integer>> directedGraph) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // 输入的word1或word2如果不在图中出现，则输出“No word1 or word2 in the graph!”
        if (!directedGraph.containsKey(word1) && !directedGraph.containsKey(word2)) {
            String result = String.format("No \"%s\" and \"%s\" in the graph!", word1, word2);
            return 0;
        } else if (!directedGraph.containsKey(word1)) {
            String result = String.format("No \"%s\" in the graph!", word1);
            return 0;
        } else if (!directedGraph.containsKey(word2)) {
            String result = String.format("No \"%s\" in the graph!", word2);
            return 0;
        }

        // 开始查询桥接词
        List<String> bridgeWords = new ArrayList<>();
        for (String bridgeWord : directedGraph.get(word1).keySet()) {
            if (directedGraph.containsKey(bridgeWord) && directedGraph.get(bridgeWord).containsKey(word2)) {
                bridgeWords.add(bridgeWord);
            }
        }

        if (bridgeWords.isEmpty()) {
            return 0;
        } else if (bridgeWords.size() == 1) {
            return 1;
        } else {
            return 1;
        }
    }
    public static String wordqueryBridgeWords(String word1, String word2, Map<String, Map<String, Integer>> directedGraph) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // 开始查询桥接词
        List<String> bridgeWords = new ArrayList<>();
        for (String bridgeWord : directedGraph.get(word1).keySet()) {
            if (directedGraph.containsKey(bridgeWord) && directedGraph.get(bridgeWord).containsKey(word2)) {
                bridgeWords.add(bridgeWord);
            }
        }

        if (bridgeWords.isEmpty()) {
            return " ";
        } else if (bridgeWords.size() == 1) {
            return bridgeWords.get(0);
        } else {
            return bridgeWords.get(0);
        }
    }
    public static String generateNewText(String inputText,Map<String, Map<String, Integer>> directedGraph){
        int i;
        int j=0;
        String newString = inputText;
        String[] parts=inputText.split(" ");
        String[] partsnew = new String[100];
        int length = parts.length;
        //System.out.println(length);
        for(i=0;i<length-1;i++)
        {
            if(flagqueryBridgeWords(parts[i],parts[i+1],directedGraph)==1)
            {
                //System.out.println(flagqueryBridgeWords(parts[i],parts[i+1],directedGraph));
                String temp = wordqueryBridgeWords(parts[i],parts[i+1],directedGraph);
                //System.out.println(temp);
                //System.out.println(parts[i]);
                partsnew[j] = parts[i];
                //System.out.println(partsnew[j]);
                partsnew[j+1] =  temp;
                //System.out.println(partsnew[j+1]);
                j = j+2;
            }
            else
            {
                partsnew[j] = parts[i];
                //System.out.println(partsnew[j]);
                j++;
            }
        }
        partsnew[j] = parts[i];
        //System.out.println(partsnew[j]);
        StringBuilder sb = new StringBuilder();
        for (i = 0; i <= Math.min(j, partsnew.length); i++) {
            sb.append(partsnew[i]).append(" "); // 在元素之间添加空格分隔符
        }
        // 将 StringBuilder 转换为字符串
        String newtext = sb.toString().trim(); // 去掉最后一个元素后面的空格
        return newtext;
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

    public static String randomWalk(Map<String, Map<String, Integer>> directedGraph)
    {
        int i;
        String newtext = new String();
        int totalInnerMapCount = directedGraph.size();; //Size of graphe
        System.out.println(totalInnerMapCount);
        Random random = new Random();
        int select = random.nextInt(totalInnerMapCount);
        if(select == 0) select++;
        //test
        //select = 8;
        //System.out.println(select);
        String node = new String();
        int count = 0;
        //get randkey
        for (Map.Entry<String, Map<String, Integer>> entry : directedGraph.entrySet()) {
            if (count == select) {
                node = entry.getKey();
                break;
            }
            count++;
        }
        String[] path = new String[100];
        String[] connect = new String[100]; //与node有连接的点
        path[0] = node;
        //System.out.println(path[0]);
        int k=0;
        // 遍历外层Map的每个值,将与node有连接的点存储到connect[]中
        for (Map.Entry<String, Map<String, Integer>> entry : directedGraph.entrySet()) {
            String outerKey = entry.getKey();
            if(outerKey == node){
                Map<String, Integer> innerMap = entry.getValue();
                // 遍历内层Map的每个键值对
                for (Map.Entry<String, Integer> innerEntry : innerMap.entrySet())
                {
                    String innerKey = innerEntry.getKey();
                    connect[k] =  innerKey;
                    k++;
                }
            }
        }
        //System.out.println(k);
        if(k==0) k++;   //避免随机数从0-0取报错
        select = random.nextInt(k);
        path[1]=connect[select];
        //System.out.println(path[1]);
        int j = 1;
        //search words connect from part[1]
        while(true){
            k=0;
            for (Map.Entry<String, Map<String, Integer>> entry : directedGraph.entrySet())
            {
                String outerKey = entry.getKey();
                if (outerKey.equals(path[j]))
                {
                    Map<String, Integer> innerMap = entry.getValue();
                    // 遍历内层Map的每个键值对
                    for (Map.Entry<String, Integer> innerEntry : innerMap.entrySet())
                    {
                        String innerKey = innerEntry.getKey();
                        connect[k] = innerKey;
                        k++;
                        //System.out.println(k);
                    }
                }
            }
                j++;
                //System.out.println(k);
                if(k==0)
                {
                    StringBuilder sb = new StringBuilder();
                    int m;
                    for (m = 0; m < j; m++) {
                        sb.append(path[m]).append(" "); // 在元素之间添加空格分隔符
                    }
                    // 将 StringBuilder 转换为字符串
                    newtext = sb.toString().trim(); // 去掉最后一个元素后面的空格
                    String filename = "p6output.txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                        writer.write(newtext); // 将字符串写入文件
                        System.out.println("写入成功！");
                    } catch (IOException e) {
                        System.out.println("写入文件时出错：" + e.getMessage());
                    }
                    return newtext;
                }
                select = random.nextInt(k);
                path[j] = connect[select];
                //System.out.println(path[j]);
                //check repeat
                for (i = 1; i < j; i++) {
                    if (path[j] == path[i] && path[j - 1] == path[i - 1]) {
                        StringBuilder sb = new StringBuilder();
                        int m;
                        for (m = 0; m <= j; m++) {
                            sb.append(path[m]).append(" "); // 在元素之间添加空格分隔符
                        }
                        // 将 StringBuilder 转换为字符串
                        newtext = sb.toString().trim(); // 去掉最后一个元素后面的空格
                        String filename = "p6output.txt";
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                            writer.write(newtext); // 将字符串写入文件
                            System.out.println("写入成功！");
                        } catch (IOException e) {
                            System.out.println("写入文件时出错：" + e.getMessage());
                        }
                        return newtext;
                    }
                }
            }
    }
}

