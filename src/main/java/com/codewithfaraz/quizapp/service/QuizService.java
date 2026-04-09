package com.codewithfaraz.quizapp.service;

import com.codewithfaraz.quizapp.doa.QuestionDao;
import com.codewithfaraz.quizapp.doa.QuizDao;
import com.codewithfaraz.quizapp.model.Question;
import com.codewithfaraz.quizapp.model.QuestionWrapper;
import com.codewithfaraz.quizapp.model.Quiz;
import com.codewithfaraz.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    /**
     * Creates a new quiz with random questions from the specified category
     * Returns the quiz ID (not a success message) so the frontend can fetch questions
     */
    public ResponseEntity<Integer> createQuiz(String category, int numQ, String title) {
        // Fetch random questions from the specified category
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);

        // Create and save the quiz
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        Quiz savedQuiz = quizDao.save(quiz);  // Save and capture the returned quiz with ID

        // Return the quiz ID so frontend can use it for subsequent requests
        return new ResponseEntity<>(savedQuiz.getId(), HttpStatus.CREATED);
    }

    /**
     * Retrieves all questions for a specific quiz (without correct answers)
     * Questions are wrapped to hide the correct answer from the client
     */
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUsers = new ArrayList<>();

        for(Question q : questionsFromDB){
            QuestionWrapper qw = new QuestionWrapper(
                    q.getId(),
                    q.getQuestionTitle(),
                    q.getOption1(),
                    q.getOption2(),
                    q.getOption3(),
                    q.getOption4()
            );
            questionsForUsers.add(qw);
        }

        return new ResponseEntity<>(questionsForUsers, HttpStatus.OK);
    }

    /**
     * Calculates the quiz result by comparing user responses with correct answers
     */
    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();

        int right = 0;
        int i = 0;

        for(Response response : responses){
            if(response.getResponse().equals(questions.get(i).getRightAnswer()))
                right++;
            i++;
        }

        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}