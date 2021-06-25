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
import com.parse.ParseUser
import it.unito.billsplitter.view.activity.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_percentage.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    //ParseUser.logInInBackground("test", "test")

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java,false,false)

    @Test
    fun nullUser(){
        val user = ParseUser.getCurrentUser()

        if(user!=null){
            ParseUser.logOut()
        }

        onView(withId(R.id.layout_landing)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun notNullUser(){

        val user = ParseUser.getCurrentUser()

        if(user==null){
            ParseUser.logIn("test", "test")
            //ParseUser.logInInBackground("test", "test")
        }

        intentsTestRule.launchActivity(null)

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.btnCreate)).check(matches(isDisplayed()))
        onView(withId(R.id.icon_text)).check(matches(withText("T")))

    }

    @Test
    fun createSplitNull(){
        val user = ParseUser.getCurrentUser()

        if(user==null){
            ParseUser.logIn("test", "test")
            //ParseUser.logInInBackground("test", "test")
        }

        intentsTestRule.launchActivity(null)



        onView(withId(R.id.btnCreate)).perform(click())

        onView(withId(R.id.d_txtEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.d_txtPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.d_btnLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun createSplitNotNull(){
        ParseUser.logIn("nicola", "nicola")

        intentsTestRule.launchActivity(null)


        onView(withId(R.id.btnCreate)).perform(click())

        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(CreateSplitActivity::class.java.name)
            )
        )
    }


    @Test
    fun checkProfile(){
        ParseUser.logIn("test", "test")

        intentsTestRule.launchActivity(null)

        onView(withId(R.id.imgAccount)).perform(click())

        onView(withId(R.id.txtUsername)).check(matches(withText("Test")))
        onView(withId(R.id.txtEmail)).check(matches(withText("test@gmail.com")))
        onView(withId(R.id.icon_text)).check(matches(withText("T")))
    }

    @Test
    fun clickSplit(){
        ParseUser.logIn("nicola", "nicola")

        intentsTestRule.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(DetailActivity::class.java.name)
            )
        )
    }

}