package mx.qsistemas.infracciones.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.CustomTablayoutItemBinding

class StepCounter : TabLayout {

    private var attrsSteps: Int = 1

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun nextStep() {
        val position = selectedTabPosition
        if (position < attrsSteps) {
            (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(position + 1)?.customView!!))?.indCustomTablayout?.visibility = View.VISIBLE
            changeColorPositions(position + 1)
            getTabAt(position + 1)?.select()
        }
    }

    fun backStep() {
        val position = selectedTabPosition
        if (position in 1..attrsSteps) {
            (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(position)?.customView!!))?.indCustomTablayout?.visibility = View.GONE
            changeColorPositions(position - 1)
            getTabAt(position - 1)?.select()
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.StepCounter, defStyle, 0)
        attrsSteps = a.getInt(R.styleable.StepCounter_steps, 1)
        for (i in 1..attrsSteps) {
            /* Custom tab layout set up */
            val customLayout = DataBindingUtil.inflate<CustomTablayoutItemBinding>(LayoutInflater.from(context), R.layout.custom_tablayout_item, null, false)
            customLayout.txtCustomTablayout.text = "Paso $i"
            customLayout.indCustomTablayout.visibility = View.GONE
            /* Add custom view to tab */
            val tab = newTab()
            tab.customView = customLayout.root
            tab.view.setPadding(-16)
            addTab(tab)
        }
        (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(0)?.customView!!))?.txtCustomTablayout?.setTextColor(ContextCompat.getColor(context, R.color.colorLightBlack))
        (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(0)?.customView!!))?.indCustomTablayout?.visibility = View.VISIBLE
        /* Disable default indicator for Tab Layout */
        setSelectedTabIndicatorHeight(0)
        /* Disable Tab Click Listener */
        val layout: LinearLayout = getChildAt(0) as LinearLayout
        for (i in 0 until layout.childCount) {
            layout.getChildAt(i).setOnTouchListener { _, _ -> true }
        }
        a.recycle()
    }

    private fun changeColorPositions(position: Int) {
        for (i in 0 until attrsSteps) {
            if (i == position) {
                (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(position)?.customView!!))?.txtCustomTablayout?.setTextColor(ContextCompat.getColor(context, R.color.colorLightBlack))
                (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(position)?.customView!!))?.indCustomTablayout?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            } else {
                (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(i)?.customView!!))?.txtCustomTablayout?.setTextColor(ContextCompat.getColor(context, R.color.colorGray4))
                (DataBindingUtil.getBinding<CustomTablayoutItemBinding>(getTabAt(i)?.customView!!))?.indCustomTablayout?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryTransparent))
            }
        }
    }
}
