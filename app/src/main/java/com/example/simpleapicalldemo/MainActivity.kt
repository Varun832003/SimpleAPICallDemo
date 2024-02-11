package com.example.simpleapicalldemo

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.loader.content.AsyncTaskLoader
import com.example.simpleapicalldemo.ui.theme.SimpleAPICallDemoTheme
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallAPILoginAsyncTask().execute()
    }
private inner class CallAPILoginAsyncTask(): AsyncTask<Any,Void,String>(){

    private lateinit var customProgressDialog: Dialog

    override fun onPreExecute() {
        super.onPreExecute()

        showProgressDialog()
    }


    override fun doInBackground(vararg p0: Any?): String {

        var result: String
        var connection: HttpURLConnection? = null

        try{
            val url = URL("https://www.mocky.io/v3/7bb80aaa-a7fe-42ed-9626-066ae61cb70a")
            connection = url.openConnection() as HttpURLConnection // for establishing the connection
            connection.doInput = true // for getting the data
            connection.doOutput = true // for sending the data

            val httpResult: Int = connection.responseCode

            if(httpResult == HttpURLConnection.HTTP_OK){
                val inputStream = connection.inputStream

                val reader = BufferedReader(
                    InputStreamReader(inputStream))

                val stringBuilder = StringBuilder()
                var line: String?
                try{
                    while(reader.readLine().also { line = it } != null){
                        stringBuilder.append(line + "\n")
                    }
                }catch(e: IOException){
                    e.printStackTrace()
                }finally {
                    try{
                        inputStream.close()
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
                result = stringBuilder.toString()
            }else{
                result = connection.responseMessage
            }
        }catch(e: SocketTimeoutException){
            result = "Connection Timeout"
        }catch (e: Exception){
            result = "Error : " + e.message
        }finally {
            connection?.disconnect()
        }
        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        cancelProgressDialog()

        if (result != null) {
            Log.i("JSON RESPONSE RESULT",result)
        }
    }

    private fun showProgressDialog(){
        customProgressDialog = Dialog(this@MainActivity)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.show()
    }

    private fun cancelProgressDialog(){
        customProgressDialog.dismiss()
    }
}
}



