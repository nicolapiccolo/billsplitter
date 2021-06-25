package it.unito.billsplitter


import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import it.unito.billsplitter.view.activity.ContactActivity
import it.unito.billsplitter.view.activity.CreateSplitActivity
import it.unito.billsplitter.view.activity.MainActivity
import it.unito.billsplitter.view.activity.RegisterActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateSplitActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(CreateSplitActivity::class.java)

    @Test
    fun startWithTitle(){
        onView(withId(R.id.txt)).check(ViewAssertions.matches(withText("Enter the Bill Title")))
    }

    @Test
    fun checkNullTitle(){

        onView(withId(R.id.btnNextTitle)).perform(ViewActions.click())
        onView(withText(R.string.emptyTitle)).inRoot(ToastMatcher())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun checkNullTotal(){
        onView(withId(R.id.edtInsertTitle)).perform(ViewActions.typeText("title"), closeSoftKeyboard())
        onView(withId(R.id.btnNextTitle)).perform(click())

        onView(withId(R.id.btnNextTitle)).perform(click())
        onView(withText(R.string.emptyTotal)).inRoot(ToastMatcher())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkIntent(){
        onView(withId(R.id.edtInsertTitle)).perform(ViewActions.typeText("title"), closeSoftKeyboard())
        onView(withId(R.id.btnNextTitle)).perform(click())
        onView(withId(R.id.edtInsertTotal)).perform(typeText("10"), closeSoftKeyboard())
        onView(withId(R.id.btnNextTitle)).perform(click())

        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(ContactActivity::class.java.name)
            )
        )
    }

    private fun forceTypeText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "force type text"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isEnabled())
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as? EditText)?.append(text)
                uiController?.loopMainThreadUntilIdle()
            }
        }
    }



}