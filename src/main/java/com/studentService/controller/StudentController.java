package com.studentService.controller;

import com.studentService.model.Student;
import com.studentService.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Upload a CSV file", description = "Uploads a CSV file and processes it.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "multipart/form-data")),
            responses = {
                    @ApiResponse(responseCode = "200", description = "File uploaded and processed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid file format",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            studentService.processStudents(file);
            return ResponseEntity.ok("File uploaded and processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing file: " + e.getMessage());
        }
    }


    @GetMapping("/search/{rollNumber}")
    public ResponseEntity<Object> searchByRollNumber(@PathVariable String rollNumber) {
        try {
            Student student = studentService.findByRollNumber(rollNumber);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/criteria")
    public ResponseEntity<String> updateCriteria(@RequestParam int science, @RequestParam int maths,
                                                 @RequestParam int english, @RequestParam int computer) {
        studentService.updateCriteria(science, maths, english, computer);
        return ResponseEntity.ok("Criteria updated successfully.");
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(){;
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.ACCEPTED);
    }
}
