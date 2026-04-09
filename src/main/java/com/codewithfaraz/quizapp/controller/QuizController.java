package com.codewithfaraz.quizapp.controller;

import com.codewithfaraz.quizapp.model.Question;
import com.codewithfaraz.quizapp.model.QuestionWrapper;
import com.codewithfaraz.quizapp.model.Response;
import com.codewithfaraz.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
@CrossOrigin(origins = "*")  // Allow all origins for frontend communication
public class QuizController {

    @Autowired
    QuizService quizService;

    /**
     * Creates a new quiz with the specified category, number of questions, and title
     * Returns the quiz ID (Integer) for the frontend to use in subsequent requests
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createQuiz(
            @RequestParam String category,
            @RequestParam int numQ,
            @RequestParam String title){
        return quizService.createQuiz(category, numQ, title);
    }

    /**
     * Fetches all questions for a specific quiz (without correct answers)
     * The ID parameter should be the quiz ID returned by /create endpoint
     */
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);
    }

    /**
     * Submits user responses and calculates the quiz score
     * Returns the number of correct answers
     */
    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(
            @PathVariable Integer id,
            @RequestBody List<Response> responses){
        return quizService.calculateResult(id, responses);
    }
}