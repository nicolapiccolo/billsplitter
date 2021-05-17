package it.unito.billsplitter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.unito.billsplitter.R
import org.json.JSONException
import org.json.JSONObject

import kotlinx.android.synthetic.main.activity_payment_details.*




class PaymentDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        val intent = intent

        try{
            val jsonObject : JSONObject = JSONObject(intent.getStringExtra("PaymentDetails"))
            showDetails(jsonObject.getJSONObject("response"),
                intent.getStringExtra("PaymentAmount").toString()
            )
        }catch(e : JSONException){
            println(e.message)
        }
    }

    private fun showDetails(response: JSONObject, paymentAmount: String){
        try{
            txtId.text  = response.getString("id")
            txtStatus.text = response.getString("state")
            txtAmount.text = ("€" + paymentAmount)
        }catch(e : JSONException){
            println(e.message)
        }

    }

}