package it.unito.billsplitter

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.parse.ParseObject
import com.parse.ParseUser
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.view.activity.MainActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ModelUnitTest {

    private var split: ParseObject? = null
    private var user: ParseUser? = null
    private var pp_account: ParseObject? = null

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java,false,false)

    @Before
    fun createSplitTest(){
        intentsTestRule.launchActivity(null)
        user = ParseUser.getCurrentUser()
        split = Model.instance.getSplitByName("Split_test")
        getPayPalAccount()
    }

    @Test
    fun getSplit(){
        val result = Model.instance.getSplit(split?.objectId.toString())
        Assert.assertNotNull(result)
    }

    @Test
    fun getTotalMySplit(){
        val result = Model.instance.getTotalofMySplit(split!!)
        Assert.assertEquals(
            0.0F,
            result
        )
    }

    @Test
    fun getOthersSplit(){
        val list: ArrayList<String> = ArrayList()
        val result = Model.instance.getOtherSplit(list)
        Assert.assertNotNull(result)
    }

    @Test
    fun getMySplits(){
        val result = Model.instance.getMySplit()
        Assert.assertNotNull(result)
    }

    @Test
    fun getTotalPaidMySplit(){
        val result = Model.instance.getTotalPaidMySplit(split!!)
        Assert.assertEquals(
            25.0F,
            result
        )
    }

    @Test
    fun getTotalHaveSplit(){
        val result = Model.instance.getTotalHaveMySplit(split!!)
        Assert.assertEquals(
            25.0F,
            result
        )
    }

    @Test
    fun getTotalSplit(){
        val result = Model.instance.getTotalSplit(split!!)
        Assert.assertEquals(
            "€50.00",
            result
        )
    }

    @Test
    fun getNameSplit(){
        val result = Model.instance.getNameSplit(split!!)
        Assert.assertEquals(
            "Split_test",
            result
        )
    }

    @Test
    fun getDateSplit(){
        val result = Model.instance.getDateSplit(split!!)
        Assert.assertEquals(
            "Tue, 22 Jun 2021",
            result
        )
    }

    @Test
    fun getOwnerSplit(){
        val result = Model.instance.getOwnerSplit(split!!)
        Assert.assertEquals(
            user!!.objectId,
            result!!.objectId
        )
    }

    @Test
    fun isMySplit(){
        val result = Model.instance.isMySplit(split!!)
        Assert.assertEquals(
            true,
            result
        )
    }

    @Test
    fun getMyHistory(){
        val result = Model.instance.getMyHistory()
        Assert.assertNotNull(result)
    }

    @Test
    fun getListMember(){
        val result = Model.instance.getListMember(split!!)
        Assert.assertNotNull(result)
    }

    @Test
    fun setUserPaid(){
        val result = Model.instance.setPaid(split!!,user!!,false)
        Assert.assertEquals(
            true,
            result
        )
    }

    @Test
    fun getMyShare(){
        val result = Model.instance.getMyShare(split!!)
        Assert.assertEquals("€25.00",
        result)
    }

    @Test
    fun getPayPalAccount(){
        pp_account = Model.instance.getPayPalAccount("frank.sca98@gmail.com", "franco")
        Assert.assertNotNull(pp_account)
    }

    @Test
    fun checkBalancePayPal(){
        val result = Model.instance.checkBalancePayPal(25.0F, pp_account!!)
        assert(result)
    }

    @Test
    fun payWithPayPalTo(){
        val sender:ParseUser? = Model.instance.findUser("stef.tria@gmail.com")
        val result = Model.instance.payWithPayPalTo(25.0F, user!!,sender!!, split!!)
        Assert.assertTrue(result)
    }
}