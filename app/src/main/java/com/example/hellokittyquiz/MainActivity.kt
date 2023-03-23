package com.example.hellokittyquiz

//import android.content.ContentValues.TAG
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.example.hellokittyquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private var numCorrect = 0
    private var numIncorrect = 0


    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate(Bundle?) called")

        Log.d(TAG, "Got a com.example.hellokittyquiz.QuizViewModel: $quizViewModel")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            updateCheatCount()
        }

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            updateCheatCount()
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            updateCheatCount()
        }

        binding.nextButton.setOnClickListener {
            isAnswered()
            quizViewModel.moveToNext()
            updateQuestion()
            updateCheatCount()
            quizViewModel.isCheater = false
        }

        binding.prevButton.setOnClickListener {
            isAnswered()
            quizViewModel.moveToPrevious()
            updateQuestion()
            updateCheatCount()
            quizViewModel.isCheater = false
        }

        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()
        updateCheatCount()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        isAnswered()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        quizViewModel.currentQuestionAnswered()

        if (userAnswer == correctAnswer){
            numCorrect+=1
        }
        else{
            numIncorrect+=1
        }

        if(quizViewModel.isCheater){
            quizViewModel.numCheated+=1
        }

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Snackbar.make(
            binding.questionTextView,
            messageResId,
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()

        isAnswered()

        checkGrade()
    }

    private fun isAnswered(){
        val isQA = false
        if(quizViewModel.isAnswered == false) {
            binding.trueButton.isEnabled = !isQA
            binding.falseButton.isEnabled = !isQA
        }
        else {
            binding.trueButton.isEnabled = isQA
            binding.falseButton.isEnabled = isQA
        }

    }

    private fun checkGrade() {
        if(numCorrect + numIncorrect == quizViewModel.questionBankSize){
            val grade = (numCorrect*100)/quizViewModel.questionBankSize

            Toast.makeText(
                this,
                //grade.toString().plus("% is your quiz grade."),
                "Your quiz grade is ".plus(grade.toString()).plus("%"),
                Toast.LENGTH_LONG
            ).show()

            //Snackbar.make(
            //    binding.questionTextView,
            //    R.string.grade_snackbar,
            //    BaseTransientBottomBar.LENGTH_;LONG
            //).show()
        }
    }

    private fun updateCheatCount(){

        binding.cheatTextView.setText("Questions Cheated: "+quizViewModel.numCheated)
    }


}