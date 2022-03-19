package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btCompose: Button
    lateinit var client: TwitterClient
    lateinit var etCount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btCompose = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        //handling the user's click on the tweet button
        btCompose.setOnClickListener {
            //grab the content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            //1. make sure the tweet isnt empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweet not allowed", Toast.LENGTH_SHORT).show()
                //look into displaying snackbar message

            }else


            //2. make sure the tweet is under character count
            if (tweetContent.length > 280) {
                Toast.makeText(this, "Tweet is too long, limit is 140 characters", Toast.LENGTH_SHORT).show()
            }else{
                //make an api call to twitter to publish tweet
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "successfully to public the tweet")
                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "failed to post the tweet", throwable)
                    }
                })
            }
        }

        etCount = findViewById(R.id.etCount)
        etCompose.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                //Log.i("cc", s.toString().length.toString())
                var count = s.toString().length
                etCount.setText("$count / 280")
                if(count == 280) {
                    etCount.setTextColor(Color.RED)
                }else {
                    etCount.setTextColor(Color.BLACK)
                }
            }

        })
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}