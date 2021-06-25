package it.unito.billsplitter

import android.content.Intent
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
import com.parse.ParseUser
import it.unito.billsplitter.view.activity.*
import org.hamcrest.core.StringContains.containsString

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_percentage.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(DetailActivity::class.java,false,false)

    @Test
    fun testMySplit(){

        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

       // onView(withId(R.id.s_txtTitle)).check(matches(withText(containsString("test my split"))))
        onView(withId(R.id.s_txtName)).check(matches(withText("You")))
        onView(withId(R.id.s_txtTotal)).check(matches(withText("€20,00")))
        onView(withId(R.id.s_txtDate)).check(matches(withText("ven, 25 giu 2021")))

        onView(withId(R.id.s_txtGet)).check(matches(withText("€0,00")))

        onView(withId(R.id.s_btnSend)).check(matches(isDisplayed()))
    }


}