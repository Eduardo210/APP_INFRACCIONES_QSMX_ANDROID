package mx.qsistemas.infracciones.modules.main.fr_my_preferences

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentMyPreferencesBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.main.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [MyPreferencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPreferencesFragment : Fragment(), MyPreferencesContracts.Presenter, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val iterator by lazy { MyPreferencesIterator(this) }
    private lateinit var activity: MainActivity
    private lateinit var binding: FragmentMyPreferencesBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_preferences, container, false)
        binding.imgBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.spnZipCode.onItemSelectedListener = this
        binding.spnArticle.onItemSelectedListener = this
        binding.spnZipCode.adapter = iterator.getPostalCodesAdapter()
        //binding.spnArticle.adapter = iterator.getArticlesAdapter()
        //binding.spnArticle.setSelection(iterator.getPositionArticle())
        binding.edtStreet.setText(Application.prefs.loadData(R.string.sp_default_street, ""))
        binding.edtBetweenStreet1.setText(Application.prefs.loadData(R.string.sp_default_street1, ""))
        binding.edtBetweenStreet2.setText(Application.prefs.loadData(R.string.sp_default_street2, ""))
        binding.edtMotivation.setText(Application.prefs.loadData(R.string.sp_default_motivation, ""))
        return binding.root
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.imgBack.id -> {
                activity.onBackPressed()
            }
            binding.btnSave.id -> {
                val postalCode = iterator.postalCodesList[binding.spnZipCode.selectedItemPosition].key
                val colony = iterator.coloniesList[binding.spnColony.selectedItemPosition].key
                val street = binding.edtStreet.text.toString().toUpperCase()
                val betweenStreet = binding.edtBetweenStreet1.text.toString().toUpperCase()
                val andStreet = binding.edtBetweenStreet2.text.toString().toUpperCase()
                val idArticle = iterator.articlesList[binding.spnArticle.selectedItemPosition].id
                val idFraction = iterator.fractionList[binding.spnFraction.selectedItemPosition].id
                val motivation = binding.edtMotivation.text.toString().toUpperCase()
                //iterator.saveDefaultMotivation(idArticle.toInt(), idFraction.toInt(), motivation)
                iterator.saveDefaultDirection(postalCode, colony, street, betweenStreet, andStreet)
            }
        }
    }

    override fun onPreferencesSaved() {
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_preferences_saved), Snackbar.LENGTH_SHORT)
        Handler().postDelayed({ activity.onBackPressed() }, 1500)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.spnZipCode.id -> {
                binding.spnColony.adapter = iterator.getColoniesAdapter(iterator.postalCodesList[p2].key)
            }
            binding.spnArticle.id -> {
                /*binding.spnFraction.adapter = iterator.getFractionAdapter(p2)
                binding.spnFraction.setSelection(iterator.getPositionFraction())*/
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MyPreferencesFragment.
         */
        @JvmStatic
        fun newInstance() =
                MyPreferencesFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
