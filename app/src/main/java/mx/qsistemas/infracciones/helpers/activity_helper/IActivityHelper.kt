package mx.qsistemas.infracciones.helpers.activity_helper

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

interface IActivityHelper {

    fun showLoader(message: String)

    fun hideLoader()

    fun loadFragment(fragment: Fragment, @IdRes idContainer: Int, direction: Direction,
                     addToBackStack: Boolean)

    fun loadFragment(fragment: Fragment, @IdRes idContainer: Int, direction: Direction,
                     addToBackStack: Boolean, shareElement: View, transitionName: String)

    fun removeLastFragment()

    fun getCurrentFragment(@IdRes idContainer: Int): Fragment

    fun getFragments(): List<Fragment>
}