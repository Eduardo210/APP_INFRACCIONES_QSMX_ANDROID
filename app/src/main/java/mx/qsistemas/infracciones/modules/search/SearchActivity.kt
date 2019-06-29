package mx.qsistemas.infracciones.modules.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search.*
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch

class SearchActivity : ActivityHelper(), SearchContracts.Presenter, View.OnClickListener {

    val router = lazy { SearchRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initListeners()
        //TODO: Colocar en el when cuando se tenga el histórico del día
        router.value.presentSearchFragment(Direction.BACK)
        try{
            tab_layout_search?.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    Log.d("TAB_SEARCH-------->", "TablReselected: $tab.toString()")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    Log.d("TAB_SEARCH-------->", "TablUnselected: $tab.toString()")
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
        img_back.setOnClickListener(this)
    }

    override fun onResultInfractionById(infraction: InfractionSearch) {

    }

    override fun onResultSearch(listInfractions: MutableList<InfractionList.Results>) {

    }

    override fun onError(msg: String) {

    }

    override fun onClick(id: View?) {
        when(id?.id){
            R.id.img_back -> onBackPressed()
        }
    }
}
