package com.example.learn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.TreeSet;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  // 无界通配符
  public void printList(List<?> list) {
    for (Object obj : list) {
      System.out.println(obj);
    }
    // list.add(new Object());  // 编译错误，不知道具体类型
  }

  @Bean
  public CommandLineRunner runner() {
    return args -> {
      // Original logic preserved from Main.java
      System.out.println(String.format("Hello and welcome!"));

      for (int i = 1; i <= 6; i++) {
        System.out.println("i = " + i);
      }
    };
  }

  @Bean
  public CommandLineRunner runnerTree() {
    return args -> {
      TreeSet<Integer> treeSet = new TreeSet<>();
      treeSet.add(5);
      treeSet.add(2);

      treeSet.add(8);

      treeSet.add(1);

      // 自动按升序排序：[1, 2, 5, 8]
      System.out.println(treeSet); // 输出：[1, 2, 5, 8]

      // 获取大于等于3的最小元素
      System.out.println(treeSet.ceiling(3)); // 输出：5

      // 遍历时保持有序
      for (Integer num : treeSet) {
        System.out.println(num); // 按顺序输出：1, 2, 5, 8
      }
    };
  }
}
