package it.unito.billsplitter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.parse.ParseUser
import it.unito.billsplitter.view.activity.*
import org.hamcrest.core.StringContains.containsString


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

        onView(withId(R.id.s_txtTitle)).check(matches(withText(containsString("Test\nmy\nsplit"))))
        onView(withId(R.id.s_txtName)).check(matches(withText("You")))
        onView(withId(R.id.s_txtTotal)).check(matches(withText("€20,00")))
        onView(withId(R.id.s_txtDate)).check(matches(withText("ven, 25 giu 2021")))

        onView(withId(R.id.s_txtGet)).check(matches(withText("€0,00")))

        onView(withId(R.id.s_btnSend)).check(matches(isDisplayed()))
        onView(withId(R.id.s_btnSend)).check(matches(withText("Send new Request")))

    }

    @Test
    fun testOtherSplit(){
        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.s_txtTitle)).check(matches(withText(containsString("Test\nother\nsplit"))))
        onView(withId(R.id.s_txtName)).check(matches(withText("Nicola")))
        onView(withId(R.id.s_txtTotal)).check(matches(withText("€20,00")))
        onView(withId(R.id.s_txtDate)).check(matches(withText("sab, 26 giu 2021")))

        onView(withId(R.id.txtYouPay)).check(matches(withText("You pay for:")))
        onView(withId(R.id.s_txtShare)).check(matches(withText("€10,00")))

        onView(withId(R.id.s_btnSend)).check(matches(isDisplayed()))
        onView(withId(R.id.s_btnSend)).check(matches(withText("Send with PayPal")))
    }

    @Test
    fun notificationMySplit(){

        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))


        onView(withId(R.id.s_btnSend)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText((R.string.notificationSend))))

        onView(withId(R.id.s_btnSend)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText((R.string.notificationAlreadySend))))
    }

    @Test
    fun dialogPaidMySplit(){
        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.s_recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, longClick()))

        onView(withText(R.string.titlePaid))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(R.string.yes))
            .perform(click())

        onView(withText("Confirming payment to: Nicola")).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }


    @Test
    fun openMenuItem(){
        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())

        onView(withText("Close the Split")).check(matches(isDisplayed()))
        onView(withText("Delete the Split")).check(matches(isDisplayed()))
        onView(withText("Modify Percentages")).check(matches(isDisplayed()))
    }

    @Test
    fun closeSplitDialog(){
        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())

        onView(withText("Close the Split")).perform(click())

        //"Are you sure to close this split? "
        onView(withId(R.id.txtConfirmTitle)).check(matches(withText(R.string.closeTitle)))
        onView(withId(R.id.txtDialogMessage)).check(matches(withText(R.string.closeMessage)))
    }

    @Test
    fun deleteSplitDialog(){
        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())

        onView(withText("Delete the Split")).perform(click())

        //"Are you sure to close this split? "
        onView(withId(R.id.txtConfirmTitle)).check(matches(withText(R.string.deleteTitle)))
        onView(withId(R.id.txtDialogMessage)).check(matches(withText(R.string.deleteMessage)))
    }






}