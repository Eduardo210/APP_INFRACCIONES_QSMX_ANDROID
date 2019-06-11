package mx.qsistemas.infracciones.helpers.activity_helper

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class ActivityHelper : AppCompatActivity(), IActivityHelper {

    protected var fragmentManager: FragmentManager? = null
    private var lastFragment: Fragment? = null

    init {
        fragmentManager = supportFragmentManager
    }

    override fun showLoader(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFragment(fragment: Fragment, idContainer: Int, direction: Direction, addToBackStack: Boolean) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        when (direction) {
            Direction.FORDWARD -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.BACK.enterAnimation, Direction.BACK.exitAnimation)
            }
            Direction.BACK -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.FORDWARD.enterAnimation, Direction.FORDWARD.enterAnimation)
            }
            Direction.TOP -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.BOTTOM.enterAnimation, Direction.BOTTOM.enterAnimation)
            }
            Direction.BOTTOM -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.TOP.enterAnimation, Direction.TOP.enterAnimation)
            }
        }
        if (addToBackStack) {
            fragmentTransaction?.addToBackStack(null)
        }
        lastFragment = fragment
        fragmentTransaction?.replace(idContainer, fragment, fragment.javaClass.simpleName)!!.commitAllowingStateLoss()
    }

    override fun loadFragment(fragment: Fragment, idContainer: Int, direction: Direction, addToBackStack: Boolean, shareElement: View, transitionName: String) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        when (direction) {
            Direction.FORDWARD -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.BACK.enterAnimation, Direction.BACK.exitAnimation)
            }
            Direction.BACK -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.FORDWARD.enterAnimation, Direction.FORDWARD.enterAnimation)
            }
            Direction.TOP -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.BOTTOM.enterAnimation, Direction.BOTTOM.enterAnimation)
            }
            Direction.BOTTOM -> {
                fragmentTransaction?.setCustomAnimations(direction.enterAnimation, direction.exitAnimation,
                        Direction.TOP.enterAnimation, Direction.TOP.enterAnimation)
            }
        }
        if (addToBackStack) {
            fragmentTransaction?.addToBackStack(null)
        }
        lastFragment = fragment
        fragmentTransaction?.addSharedElement(shareElement, transitionName)
        fragmentTransaction?.replace(idContainer, fragment, fragment.javaClass.simpleName)?.commitAllowingStateLoss()
    }

    override fun removeLastFragment() {
        if (lastFragment != null) {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.remove(lastFragment as Fragment)!!.commit()
        }
    }

    override fun getCurrentFragment(idContainer: Int): Fragment {
        return fragmentManager?.findFragmentById(idContainer)!!
    }

    override fun getFragments(): List<Fragment> {
        return fragmentManager?.fragments!!
    }
}