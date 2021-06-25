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
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.rule.ActivityTestRule
import it.unito.billsplitter.view.activity.ContactActivity
import it.unito.billsplitter.view.activity.SplittingActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(ContactActivity::class.java,false,false)


    @Before
    fun start (){

        val intent = Intent().apply {
            putExtra("title", "test")
            putExtra("total", "10")
        }

        intentsTestRule.launchActivity(intent)
    }

    @Test
    fun emptyFriend(){
        onView(withId(R.id.btnAdd)).perform(click())
        onView(withText(R.string.emptyFriend)).inRoot(ToastMatcher())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun selectItem(){
        onView(withId(R.id.recyclerViewContacts)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.checkBox_select)))
        onView(withId(R.id.btnAdd)).perform(click())

        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(SplittingActivity::class.java.name),
                IntentMatchers.hasExtra("title", "test"),
                IntentMatchers.hasExtra("total", "10")
            )
        )
    }



}

class MyViewAction {

    companion object{
        fun clickChildViewWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController, view: View) {
                    val v = view.findViewById<View>(id)
                    v.performClick()
                }
            }
        }
    }

}