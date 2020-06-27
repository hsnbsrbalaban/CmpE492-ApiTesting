package com.example.cmpe492_apitesting

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.*
import com.android.volley.toolbox.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var requestQueue: RequestQueue? = null
    private var urlPrefix = "https://demo.apitesting.info"
    private var urlPathForGreeting = "/greeting"
    private var urlPathForWait = "/greetingWithWait"

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun showPopUp(header: String, body: String, success: Boolean) {
        // Initialize a new layout inflater instance
        val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.another_view,null)
        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )
        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }
        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.TOP
            popupWindow.exitTransition = slideOut
        }
        // Get the widgets reference from custom view
        val tvHeader = view.findViewById<TextView>(R.id.tv_header)
        val tvBody = view.findViewById<TextView>(R.id.tv_body)
        val buttonPopup = view.findViewById<Button>(R.id.btn_popup)

        tvHeader.text = header
        tvBody.text = body

        // Set a click listener for popup's button widget
        buttonPopup.setOnClickListener{
            // Dismiss the popup window
            popupWindow.dismiss()
        }
        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(root_layout)
        popupWindow.showAtLocation(
            root_layout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )
    }

    private fun makeGetRequest(text: String, wait: Boolean) {
        var url = if(wait) {
            "$urlPrefix$urlPathForWait?name=$text"
        } else {
            "$urlPrefix$urlPathForGreeting?name=$text"
        }

        val getRequest: StringRequest = @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : StringRequest(Request.Method.GET, url,
            Response.Listener {
                showPopUp(header = "GET: ${url}:", body = it.toString(), success = true)
            },
            Response.ErrorListener {
                showPopUp(header = "GET: ${url}:", body = it.toString(), success = false)
            }) {}
        requestQueue?.add(getRequest);
    }

    private fun makePostRequestThatReturnsString(text: String, wait: Boolean) {
        var url = if (wait) {
            "$urlPrefix$urlPathForWait"
        } else {
            "$urlPrefix$urlPathForGreeting"
        }

        val jsonObject = JSONObject()
        jsonObject.put("name", text)

        val postRequest: StringRequest = @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : StringRequest(Request.Method.POST, url,
            Response.Listener {
                showPopUp(header = "POST: ${url}:", body = it.toString(), success = true)
            },
            Response.ErrorListener {
                showPopUp(header = "POST: ${url}:", body = it.toString(), success = false)
            }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonObject.toString().toByteArray(Charsets.UTF_8)
            }
        }
        requestQueue?.add(postRequest);
    }

    private fun makePostRequestThatReturnsJson(text: String, wait: Boolean) {
        var url = if (wait) {
            "$urlPrefix$urlPathForWait"
        } else {
            "$urlPrefix$urlPathForGreeting"
        }

        val jsonObject = JSONObject()
        jsonObject.put("name", text)

        val postRequest: JsonObjectRequest = @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : JsonObjectRequest(url, jsonObject,
            Response.Listener {
                showPopUp(header = "POST: ${url}:", body = it.toString(), success = true)
            },
            Response.ErrorListener {
                showPopUp(header = "POST: ${url}:", body = it.toString(), success = false)
            }) {}
        requestQueue?.add(postRequest);
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        val etInputBoxString: EditText = findViewById(R.id.et_textInput_String)
        val btnSendGetGreeting: Button = findViewById(R.id.btn_send_get_greeting)
        val btnSendGetWait: Button = findViewById(R.id.btn_send_get_wait)
        val btnSendPostGreeting: Button = findViewById(R.id.btn_send_post_greeting)
        val btnSendPostWait: Button = findViewById(R.id.btn_send_post_wait)

        val etInputBoxJSON: EditText = findViewById(R.id.et_textInput_Json)
        val btnSendGetGreetingJson: Button = findViewById(R.id.btn_send_get_greeting_json)
        val btnSendGetWaitJson: Button = findViewById(R.id.btn_send_get_wait_json)
        val btnSendPostGreetingJson: Button = findViewById(R.id.btn_send_post_greeting_json)
        val btnSendPostWaitJson: Button = findViewById(R.id.btn_send_post_wait_json)

        btnSendGetGreeting.setOnClickListener {
            makeGetRequest(etInputBoxString.text.toString(), wait = false)
        }

        btnSendGetWait.setOnClickListener {
            makeGetRequest(etInputBoxString.text.toString(), wait = true)
        }

        btnSendPostGreeting.setOnClickListener {
            makePostRequestThatReturnsString(etInputBoxString.text.toString(), wait = false)
        }

        btnSendPostWait.setOnClickListener {
            makePostRequestThatReturnsString(etInputBoxString.text.toString(), wait = true)
        }

        btnSendGetGreetingJson.setOnClickListener {
            makeGetRequest(etInputBoxJSON.text.toString(), wait = false)
        }

        btnSendGetWaitJson.setOnClickListener {
            makeGetRequest(etInputBoxJSON.text.toString(), wait = true)
        }

        btnSendPostGreetingJson.setOnClickListener {
            makePostRequestThatReturnsJson(etInputBoxJSON.text.toString(), wait = false)
        }

        btnSendPostWaitJson.setOnClickListener {
            makePostRequestThatReturnsJson(etInputBoxJSON.text.toString(), wait = true)
        }
    }
}