package it.unito.billsplitter


import android.content.Intent
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import it.unito.billsplitter.view.activity.ContactActivity
import it.unito.billsplitter.view.activity.SplittingActivity
import kotlinx.android.synthetic.main.activity_setting_percentage.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SplittingActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(SplittingActivity::class.java,false,false)

    @Before
    fun setMember(){

        val intentsTestRule1 = IntentsTestRule(ContactActivity::class.java,false,false)

        val intent = Intent().apply {
            putExtra("title", "test")
            putExtra("total", "10")
        }

        intentsTestRule1.launchActivity(intent)

        onView(withId(R.id.recyclerViewContacts)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.checkBox_select)))
        onView(withId(R.id.btnAdd)).perform(click())
    }

    @Test
    fun checkVisibility(){
        onView(withId(R.id.d_btnSend)).perform(click())
        onView(withId(R.id.layout_money_sent)).check(matches(isDisplayed()))
        onView(withId(R.id.btnBackHome)).check(matches(isDisplayed()))
    }

    @Test
    fun checkLocking(){
        onView(withId(R.id.recyclerViewSplitting)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.btnLocker)))


        onView(RecyclerViewMatcher(R.id.recyclerViewSplitting)
            .atPositionOnView(0, R.id.seekBar))
            .check(matches(not(isEnabled())))

        onView(RecyclerViewMatcher(R.id.recyclerViewSplitting)
            .atPositionOnView(0, R.id.txtPrice))
            .check(matches(not(isEnabled())))

        onView(RecyclerViewMatcher(R.id.recyclerViewSplitting)
            .atPositionOnView(1, R.id.seekBar))
            .check(matches(isEnabled()))

        onView(RecyclerViewMatcher(R.id.recyclerViewSplitting)
            .atPositionOnView(1, R.id.txtPrice))
            .check(matches(isEnabled()))
    }


}

