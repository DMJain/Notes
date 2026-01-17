package org.example.p3_Java_Advance_Concept.c3_streams.examples;

import java.util.*;
import java.util.stream.*;

import org.example.p3_Java_Advance_Concept.c3_streams.model.*;

/**
 * Demonstrates the Collectors utility class.
 * Collectors provide reduction operations for collect().
 */
public class CollectorsDemo {

    /**
     * Demonstrates groupingBy() - group elements by a classifier.
     */
    public static void demoGroupingBy() {
        List<Person> people = SampleData.getPeople();

        System.out.println("   🔹 groupingBy(Department):");
        Map<String, List<Person>> byDept = people.stream()
                .collect(Collectors.groupingBy(Person::getDepartment));
        byDept.forEach((dept, persons) -> System.out.println("      " + dept + ": " +
                persons.stream().map(Person::getName).collect(Collectors.toList())));
    }

    /**
     * Demonstrates partitioningBy() - split into true/false groups.
     */
    public static void demoPartitioningBy() {
        List<Person> people = SampleData.getPeople();

        System.out.println("\n   🔹 partitioningBy(age >= 30):");
        Map<Boolean, List<String>> byAge = people.stream()
                .collect(Collectors.partitioningBy(
                        p -> p.getAge() >= 30,
                        Collectors.mapping(Person::getName, Collectors.toList())));
        System.out.println("      30+ years: " + byAge.get(true));
        System.out.println("      Under 30: " + byAge.get(false));
    }

    /**
     * Demonstrates downstream collectors with groupingBy.
     */
    public static void demoDownstreamCollectors() {
        List<Person> people = SampleData.getPeople();

        System.out.println("\n   🔹 counting() - People per department:");
        Map<String, Long> countByDept = people.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.counting()));
        System.out.println("      " + countByDept);

        System.out.println("\n   🔹 averagingDouble() - Avg salary by department:");
        Map<String, Double> avgByDept = people.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.averagingDouble(Person::getSalary)));
        avgByDept.forEach((dept, avg) -> System.out.printf("      %s: $%.2f%n", dept, avg));
    }

    /**
     * Demonstrates joining strings.
     */
    public static void demoJoining() {
        List<Person> people = SampleData.getPeople();

        System.out.println("\n   🔹 joining() - String concatenation:");
        String names = people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("      Names: " + names);
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 COLLECTORS\n");
        demoGroupingBy();
        demoPartitioningBy();
        demoDownstreamCollectors();
        demoJoining();
    }
}
