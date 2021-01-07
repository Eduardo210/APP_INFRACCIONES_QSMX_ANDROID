package mx.qsistemas.infracciones.modules.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import mx.qsistemas.infracciones.Application.Companion.TAG
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivitySearchBinding
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction

class SearchActivity : ActivityHelper(), View.OnClickListener {

    private lateinit var binding: ActivitySearchBinding
    val router = lazy { SearchRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        initListeners()
        //TODO: Colocar en el when cuando se tenga el histórico del día
        router.value.presentSearchFragment(Direction.BACK)
        try{
            binding.tabLayoutSearch.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    Log.d(TAG, "TablReselected: $tab.toString()")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    Log.d(TAG, "TablUnselected: $tab.toString()")
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    //TODO: Descomentar cuando se tenga el histórico
                  /*  when(tab?.position){
                        0-> router.value.presentHistoricalFragment(Direction.FORDWARD)
                        1-> router.value.presentSearchFragment(Direction.BACK)
                    }*/

                }

            })
        }catch (ex: Exception){
            ex.printStackTrace()
        }


    }

    private fun initListeners() {
        binding.imgBack.setOnClickListener(this)
    }

    override fun onClick(id: View?) {
        when(id?.id){
            R.id.img_back -> onBackPressed()
        }
    }
}
