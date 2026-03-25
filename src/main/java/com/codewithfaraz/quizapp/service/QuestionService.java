package com.codewithfaraz.quizapp.service;

import com.codewithfaraz.quizapp.model.Question;
import com.codewithfaraz.quizapp.doa.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try{
            return new ResponseEntity<>(questionDao.findByActiveTrue(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try{
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateQuestion(int id, Question question) {
        Optional<Question> optionalQuestion = questionDao.findById(id);

        if(optionalQuestion.isPresent()) {

            Question existingQuestion = optionalQuestion.get();

            existingQuestion.setQuestionTitle(question.getQuestionTitle());
            existingQuestion.setOption1(question.getOption1());
            existingQuestion.setOption2(question.getOption2());
            existingQuestion.setOption3(question.getOption3());
            existingQuestion.setOption4(question.getOption4());
            existingQuestion.setRightAnswer(question.getRightAnswer());
            existingQuestion.setDifficultyLevel(question.getDifficultyLevel());
            existingQuestion.setCategory(question.getCategory());

            questionDao.save(existingQuestion);

            return new ResponseEntity<>("Question Updated Successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Question Not Found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteQuestion(int id) {
        Optional<Question> optionalQuestion = questionDao.findById(id);

        if(optionalQuestion.isPresent()){
            Question question = optionalQuestion.get();
            question.setActive(false);
            questionDao.save(question);

            return new ResponseEntity<>("Question Deactivated", HttpStatus.OK);
        }

        return new ResponseEntity<>("Question Not Found", HttpStatus.NOT_FOUND);
    }
}
