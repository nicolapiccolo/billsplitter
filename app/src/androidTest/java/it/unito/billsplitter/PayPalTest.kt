package it.unito.billsplitter


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.parse.ParseObject
import com.parse.ParseRelation
import com.parse.ParseUser
import it.unito.billsplitter.view.activity.*
import org.junit.Test
import kotlin.reflect.typeOf


class PayPalTest {

    @Test
    fun dialogLogin(){
        ParseUser.logIn("test", "test")
        val currentUser = ParseUser.getCurrentUser()
        /*val relation: ParseRelation<ParseObject> = currentUser.getRelation("id_paypal")

        if(relation != null) {
            //currentUser.put("id_paypal",null)
            currentUser.save()
            println("GG " + relation)
        }*/

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.s_btnSend))
            .perform(click())

        onView(withId(R.id.d_centerLayout)).check(matches(isDisplayed()))

    }

    @Test
    fun dialogLoginError(){
        ParseUser.logIn("test", "test")
        val currentUser = ParseUser.getCurrentUser()
        /*val relation: ParseRelation<ParseObject> = currentUser.getRelation("id_paypal")

        if(relation != null) {
            //currentUser.put("id_paypal",null)
            currentUser.save()
            println("GG " + relation)
        }*/

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.s_btnSend))
            .perform(click())

        onView(withId(R.id.d_txtEmail)).perform(typeText("testo@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.d_txtPassword)).perform(typeText("test"), closeSoftKeyboard())

        onView(withId(R.id.d_btnLogin)).perform(click())

        onView(withText(R.string.payPaError)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

        onView(withId(R.id.txtMessage)).check(matches(withText(R.string.incorrectCredential)))
    }

    @Test
    fun sendMoney(){
        ParseUser.logIn("test", "test")
        val currentUser = ParseUser.getCurrentUser()
        /*val relation: ParseRelation<ParseObject> = currentUser.getRelation("id_paypal")

        if(relation != null) {
            //currentUser.put("id_paypal",null)
            currentUser.save()
            println("GG " + relation)
        }*/

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.s_btnSend))
            .perform(click())

        onView(withId(R.id.d_txtEmail)).perform(typeText("test@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.d_txtPassword)).perform(typeText("test"), closeSoftKeyboard())

        onView(withId(R.id.d_btnLogin)).perform(click())

        onView(withId(R.id.d_txtBalance)).check(matches(withText("€100,00")))
        onView(withId(R.id.txtShare)).check(matches(withText("€10,00")))
        onView(withId(R.id.txtSendTo)).check(matches(withText("Nicola")))

    }

    @Test
    fun sendMoneySuccess(){
        ParseUser.logIn("test", "test")

        val intentsTestRule1 = IntentsTestRule(MainActivity::class.java,false,false)
        intentsTestRule1.launchActivity(null)

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.s_btnSend))
            .perform(click())

        onView(withId(R.id.d_btnSend))
            .perform(click())

        onView(withId(R.id.txtSucces))
            .check(matches(withText("Money Sent Successfully")))

        onView(withId(R.id.d_btnBackHome))
            .check(matches(isDisplayed()))
    }








}