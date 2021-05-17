package it.unito.billsplitter.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseObject
import com.paypal.android.sdk.payments.*
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import it.unito.billsplitter.Config
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapterDetail
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.activity.CellClickListenerDetail
import it.unito.billsplitter.activity.MainActivity
import it.unito.billsplitter.activity.PaymentDetails
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import kotlinx.android.synthetic.main.fragment_other_split.*
import kotlinx.android.synthetic.main.fragment_other_split.icon_text
import kotlinx.android.synthetic.main.fragment_other_split.s_recyclerView
import kotlinx.android.synthetic.main.fragment_other_split.s_txtDate
import kotlinx.android.synthetic.main.fragment_other_split.s_txtName
import kotlinx.android.synthetic.main.fragment_other_split.s_txtTitle
import kotlinx.android.synthetic.main.fragment_other_split.s_txtTotal
import org.json.JSONException
import java.math.BigDecimal

class DetailOtherSplitFragment : Fragment(), CellClickListenerDetail {

    companion object {
        val PAYPAL_REQUEST_CODE = 7171

        fun newIstance(): Fragment{
            return DetailOtherSplitFragment()
        }
    }

    val config: PayPalConfiguration = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId(Config.PAYPAL_CLIENT_ID)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mySplit: MySplit = (arguments?.getParcelable("split") as MySplit?)!!

        println("MS: " + mySplit.name)
        println("MS: " + mySplit.memberList)

        s_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = RvAdapterDetail(this, mySplit.memberList)
        s_recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        displaySplit(mySplit)

        s_btnSend.setup(
            createOrder = CreateOrder { createOrderActions ->
                val order = Order(
                    intent = OrderIntent.CAPTURE,
                    appContext = AppContext(
                        userAction = UserAction.PAY_NOW
                    ),
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            amount = Amount(
                                currencyCode = CurrencyCode.EUR,
                                value = "0.10"
                            )
                        )
                    )
                )

                createOrderActions.create(order)
            },
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                }
            },
            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }


        )
    }

    override fun onDestroy() {
        activity?.stopService(Intent(context,PayPalService::class.java))
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_other_split, container, false)
        val intent: Intent = Intent(context,PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        activity?.startService(intent)
    }


    private fun displaySplit(split: MySplit){

        val owner = split.owner.getString("username")?.capitalize()
        s_txtName.text = owner
        icon_text.text = owner!![0].toString()
        s_txtTitle.text = split.name
        s_txtTotal.text = split.total
        s_txtDate.text = split.date
        s_txtShare.text = split.share
    }

    private fun processPayment(amount: String){
        val payPalPayment: PayPalPayment = PayPalPayment(BigDecimal(0.1),"EUR","Donate",PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                val confirmation: PaymentConfirmation? = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if(confirmation != null){
                    try{
                        val paymentDetails: String = confirmation.toJSONObject().toString(4)
                        startActivity(Intent(context, PaymentDetails::class.java)
                            .putExtra("PaymentDetails",paymentDetails)
                            .putExtra("PaymentAmount",0.1))


                    }catch(e: JSONException){
                        println(e.message)
                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED)
                println("CANCEL")
        }
        else if(resultCode ==   PaymentActivity.RESULT_EXTRAS_INVALID)
            println("INVALID")
    }


    override fun onCellClickListener(data: SplitMember?) {
        println("Not yet implemented")
    }
}