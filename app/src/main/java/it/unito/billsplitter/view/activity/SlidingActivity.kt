package it.unito.billsplitter.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import it.unito.billsplitter.R
import it.unito.billsplitter.controller.adapter.RvAdapterSliding
import kotlinx.android.synthetic.main.activity_sliding.*

class SlidingActivity : AppCompatActivity(){

    private lateinit var myAdapter: RvAdapterSliding
    private lateinit var dotsTv: Array<TextView?>
    private lateinit var layouts: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("SLIDING ACTIVITY")

        setContentView(R.layout.activity_sliding)

        if(!isFirstTimeAppStart()){
            setAppStartStatus(false)
            startActivity(Intent(this, LandingActivity::class.java))
            finish()
        }

        statusBarTransparent()
        btnNextSlide.setOnClickListener{
            val currentPage: Int = viewPager.currentItem + 1
            if(currentPage < layouts.size){
                viewPager.currentItem = currentPage
            }
            else{
                setAppStartStatus(false)
                startActivity(Intent(this, LandingActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                finish()
            }
        }

        btnSkip.setOnClickListener{
            setAppStartStatus(false)
            startActivity(Intent(this, LandingActivity::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
        }

        layouts = intArrayOf(R.layout.slide_1,R.layout.slide_2,R.layout.slide_3,R.layout.slide_4,)
        myAdapter = RvAdapterSliding(layouts,  applicationContext)
        viewPager.adapter = myAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == layouts.size - 1){
                    btnNextSlide.text = "Start"
                    btnSkip.visibility = View.GONE
                }
                else{
                    btnNextSlide.text = "Next"
                    btnSkip.visibility = View.VISIBLE
                }
                setDots(position)
            }

        })
        setDots(0)
    }

    private fun isFirstTimeAppStart() : Boolean {
        val pref = applicationContext.getSharedPreferences("BILLSPLITT", Context.MODE_PRIVATE)
        return pref.getBoolean("APP_START", true)
    }

    private fun setAppStartStatus(status: Boolean){
        val pref = applicationContext.getSharedPreferences("BILLSPLITT", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        editor.putBoolean("APP_START", status)
        editor.apply()
    }

    private fun statusBarTransparent() {
        if(Build.VERSION.SDK_INT >= 21){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setDots(page: Int){
        dotsLayout.removeAllViews()
        dotsTv = arrayOfNulls(layouts.size)

        for(i in dotsTv.indices){
            dotsTv[i] = TextView(this)
            dotsTv[i]!!.text = Html.fromHtml("&#8226;")
            dotsTv[i]!!.textSize = 30f
            dotsTv[i]!!.setTextColor(Color.parseColor("#9e9ed8"))
            dotsLayout.addView(dotsTv[i])
        }

        if(dotsTv.isNotEmpty()){
            dotsTv[page]!!.setTextColor(Color.parseColor("#0505AF"))
        }

    }

}