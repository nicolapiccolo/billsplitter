package it.unito.billsplitter

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import it.unito.billsplitter.view.activity.LoginActivity
import it.unito.billsplitter.view.activity.MainActivity
import it.unito.billsplitter.view.fragment.LoginFragment
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {


        val testActivityRule = IntentsTestRule(LoginActivity::class.java)

        @Rule
        fun rule() = testActivityRule


        @Test
        fun loginTest() {
            val username = "test"
            val password = "test"
            val email = "test@gmail.com"


            Espresso.onView(ViewMatchers.withId(R.id.txtEmail))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard())

            Espresso.onView(ViewMatchers.withId(R.id.txtPassword))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

            Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
                .perform(ViewActions.click())

            Espresso.onView(ViewMatchers.withText(R.string.loginSuccess)).inRoot(ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        @Test
        fun loginTestIntent() {
            val username = "test"
            val password = "test"
            val email = "test@gmail.com"


            Espresso.onView(ViewMatchers.withId(R.id.txtEmail))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard())

            Espresso.onView(ViewMatchers.withId(R.id.txtPassword))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

            Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
                .perform(ViewActions.click())

            intended(
                Matchers.allOf(
                    hasComponent(MainActivity::class.java.name),
                    IntentMatchers.hasExtra("RESULT", LoginActivity.ID.toString())
                )
            )
        }



}