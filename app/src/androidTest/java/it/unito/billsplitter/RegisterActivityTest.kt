package it.unito.billsplitter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import it.unito.billsplitter.view.activity.MainActivity
import it.unito.billsplitter.view.activity.RegisterActivity
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class RegisterActivityTest {


    @get:Rule
    val intentsTestRule = IntentsTestRule(RegisterActivity::class.java)


    /**
     * Having not entered data in the TextViews (username and password), the relative error views will be shown
     */
    @Test
    fun showErrorsViewOnEmptyCredentials(){

        onView(withId(R.id.btnRegister)).perform(ViewActions.click())

        onView(withText(R.string.emptyField)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }




    /**
     * Check that the login is successful and that the MainActivity intent starts correctly
     */
    @Test
    fun successfulLoginShouldOpenMainScreen() {
        val username = "test"
        val password = "test"
        val email = "test@gmail.com"

        onView(withId(R.id.txtName))
            .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.txtEmail))
            .perform(ViewActions.typeText(email), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.txtPassword))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.btnRegister))
            .perform(ViewActions.click())

        /*
           - Validate intent -
           Espresso-Intents records all intents that attempt to launch activities from the application under test.
           Using the intended() method you can assert that a given intent has been seen.

           hasComponent(targetActivity) -> verify if exist an intent with targetActivity
           hasExtra(key, value) -> verify if exist an intent with extra (key, value)
           allOf -> to verify both matchers
         */

        intended(
            Matchers.allOf(
                hasComponent(MainActivity::class.java.name))
        )

    }




}