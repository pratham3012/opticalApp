package com.mibtech.optical.helper

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection
public class  imageupload {

    companion object{
        public  var realurl ="null"
        fun  getValue(): String{

            return  realurl

        }
    }
    private val CLIENT_ID = "62865de1937fd8c"
  var urll:String="vv";
    public fun uploadImageToImgur(imagea: Bitmap,uuid: String,activity: Context) :String{

     var   image=resizeBitmap(imagea);
        getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                var url = URL("https://api.imgur.com/3/image")

                val boundary = "Boundary-${System.currentTimeMillis()}"

                val httpsURLConnection =
                        withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.setRequestProperty("Authorization", "Client-ID $CLIENT_ID")
                httpsURLConnection.setRequestProperty(
                        "Content-Type",
                        "multipart/form-data; boundary=$boundary"
                )

                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                var body = ""
                body += "--$boundary\r\n"
                body += "Content-Disposition:form-data; name=\"image\""
                body += "\r\n\r\n$base64Image\r\n"
                body += "--$boundary--\r\n"


                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(body)
                    Log.i("edddd", "uploadImageToImgur: " + body)
                    outputStreamWriter.flush()
                    val response = httpsURLConnection.inputStream.bufferedReader()
                            .use { it.readText() }  // defaults to UTF-8
                    val jsonObject = JSONTokener(response).nextValue() as JSONObject
                    val data = jsonObject.getJSONObject("data")
                    Log.d("TAG", "Link is : ${data.getString("link")}")

                    urll = data.getString("link");
                    try {
                        //  var  doc = Jsoup.connect("https://mibtechnologies.in/hupariapp/uploadCategory.php?uid=${uuid}&catname=${catName}&catimage=${data.getString("link")}").get()  // <2>
                        println(data.getString("link"))
                        Toast.makeText(activity, "upload done", Toast.LENGTH_LONG).show();
                    } catch (e: Exception) {

                    }

                }

            }

        })
        return urll


    }
    public fun uploadImageToImgur(image: Bitmap,catname_item:String,itemname: String,phonenumber:String,stars:String,rank:String,address:String,status:String,activity:Context) :String{
        getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                var url = URL("https://api.imgur.com/3/image")

                val boundary = "Boundary-${System.currentTimeMillis()}"

                val httpsURLConnection =
                        withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.setRequestProperty("Authorization", "Client-ID $CLIENT_ID")
                httpsURLConnection.setRequestProperty(
                        "Content-Type",
                        "multipart/form-data; boundary=$boundary"
                )

                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                var body = ""
                body += "--$boundary\r\n"
                body += "Content-Disposition:form-data; name=\"image\""
                body += "\r\n\r\n$base64Image\r\n"
                body += "--$boundary--\r\n"


                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(body)
                    Log.i("edddd", "uploadImageToImgur: "+body)
                    outputStreamWriter.flush()
                    val response = httpsURLConnection.inputStream.bufferedReader()
                            .use { it.readText() }  // defaults to UTF-8
                    val jsonObject = JSONTokener(response).nextValue() as JSONObject
                    val data = jsonObject.getJSONObject("data")
                    Log.d("TAG", "Link is : ${data.getString("link")}")
                    urll=data.getString("link");
                    realurl=urll
                    try {
                 //       var  doc = Jsoup.connect("https://mibtechnologies.in/hupariapp/uploadItem.php?catname=${catname_item}&name=${itemname}&stars=${stars}&ratings=55&ranks=${rank}&address=${address}&phone=${phonenumber}&status=${status}&image=${data.getString("link")}").get()  // <2>
                     Toast.makeText(activity,"upload done",Toast.LENGTH_LONG).show()



                    }catch (e: Exception){

                    }

                }

            }

        })
        return urll


    }
    fun resizeBitmap(source: Bitmap): Bitmap {
        val maxResolution = 1000    //edit 'maxResolution' to fit your need
        val width = source.width
        val height = source.height
        var newWidth = width
        var newHeight = height
        val rate: Float

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / width.toFloat()
                newHeight = (height * rate).toInt()
                newWidth = maxResolution
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / height.toFloat()
                newWidth = (width * rate).toInt()
                newHeight = maxResolution
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true)
    }
    private fun getBase64Image(image: Bitmap, complete: (String) -> Unit) {
        GlobalScope.launch {
            val outputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val b = outputStream.toByteArray()
            complete(Base64.encodeToString(b, Base64.DEFAULT))
        }
    }
}