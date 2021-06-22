package it.unito.billsplitter.view.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.unito.billsplitter.controller.task.ChangePasswordAsyncTask
import it.unito.billsplitter.R
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.change_password_dialog.*
import kotlinx.android.synthetic.main.change_password_dialog.view.*
import kotlinx.android.synthetic.main.profile_bottomsheet_fragment.*

class ProfileBottomSheetActivity: BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = User.getCurrentUser()?.get("username").toString().capitalize()
        txtUsername.text = username
        txtEmail.text = User.getCurrentUser()?.get("email").toString()
        txtPhone.text = User.getCurrentUser()?.get("phone").toString()
        icon_text.text = username[0].toString()
        btnChangePassword.setOnClickListener {
            changePasswordDialog(requireContext())
        }

        btnLogOut.setOnClickListener {
            User.logOut()
            activity?.finish()
            val intent = Intent(context, SlidingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    public fun updateContext(){
        showProgressBar(false)
        val username = User.getCurrentUser()?.get("username").toString().capitalize()
        txtUsername.text = username
        txtEmail.text = User.getCurrentUser()?.get("email").toString()
        txtPhone.text = User.getCurrentUser()?.get("phone").toString()
        icon_text.text = username[0].toString()
    }

    private fun changePasswordDialog(ctx: Context){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.change_password_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
                .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mAlertDialog.d_btnSend.setOnClickListener {
            if(!mDialogView.d_newPassword.text.equals("") && !mDialogView.d_newPasswordConfirm.text.equals("")){
                if(mDialogView.d_newPassword.text.toString().equals(mDialogView.d_newPasswordConfirm.text.toString())){
                    showProgressBar(true)
                    ChangePasswordAsyncTask(ctx).execute(mDialogView.d_newPassword.text.toString())
                    mAlertDialog.dismiss()
                }
                else
                    Toast.makeText(ctx, "Password missmatch!", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(ctx, "Fields can't be empty!", Toast.LENGTH_SHORT).show()

        }
        mDialogView.d_btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun showProgressBar(b: Boolean){
        if(b){
            container.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        else{
            container.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}
