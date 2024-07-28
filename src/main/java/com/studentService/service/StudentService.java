package com.studentService.service;

import com.opencsv.CSVReader;
import com.studentService.model.Criteria;
import com.studentService.model.Student;
import com.studentService.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final Criteria criteria;

    @Autowired
    public StudentService(StudentRepository studentRepository, Criteria criteria) {
        this.studentRepository = studentRepository;
        this.criteria = criteria;
    }

    public void processStudents(MultipartFile file) {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = reader.readAll();
            if (records.isEmpty()) {
                throw new RuntimeException("CSV file is empty");
            }

            // Skip the header row
            records.removeFirst();

            List<Student> students = new ArrayList<>();
            for (String[] record : records) {
                // Check for empty lines or lines with the wrong number of columns
                if (record == null || record.length == 0) {
                    System.err.println("Skipping empty line");
                    continue;
                }
                try {
                    Student student = new Student();
                    student.setRollNumber(record[0]);
                    student.setStudentName(record[1]);
                    student.setScience(Integer.parseInt(record[2]));
                    student.setMaths(Integer.parseInt(record[3]));
                    student.setEnglish(Integer.parseInt(record[4]));
                    student.setComputer(Integer.parseInt(record[5]));
                    student.setEligible(checkEligibility(student) ? "YES" : "NO");
                    students.add(student);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in record: " + String.join(",", record));
                }
            }

            List<Future<?>> futures = students.stream()
                    .map(student -> executor.submit(() -> {
                        try {
                            studentRepository.save(student);
                        } catch (Exception e) {
                            System.err.println("Error saving student: " + student.getRollNumber());
                        }
                    }))
                    .collect(Collectors.toList());

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.err.println("Error waiting for future to complete");
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing file with virtual threads");
            throw new RuntimeException("Error processing file with virtual threads", e);
        } finally {
            executor.shutdown();
        }
    }

    private boolean checkEligibility(Student student) {
        return student.getScience() >= criteria.getScienceCriteria() &&
                student.getMaths() >= criteria.getMathsCriteria() &&
                student.getEnglish() >= criteria.getEnglishCriteria() &&
                student.getComputer() >= criteria.getComputerCriteria();
    }

    public Student findByRollNumber(String rollNumber) {
        return studentRepository.findById(rollNumber)
                .orElseThrow(() -> new RuntimeException("No student found with roll number: " + rollNumber));
    }



    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void updateCriteria(int science, int maths, int english, int computer) {
        criteria.setScienceCriteria(science);
        criteria.setMathsCriteria(maths);
        criteria.setEnglishCriteria(english);
        criteria.setComputerCriteria(computer);
    }
}
