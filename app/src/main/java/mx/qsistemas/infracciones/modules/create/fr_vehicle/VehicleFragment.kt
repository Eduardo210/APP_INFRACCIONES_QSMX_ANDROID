package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentVehicleBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity

private const val ARG_IS_CREATION = "is_creation"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [VehicleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [VehicleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VehicleFragment : Fragment(), VehicleContracts.Presenter, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var isCreation: Boolean = true
    private val iterator = lazy { VehicleIterator(this) }
    private lateinit var activity: CreateInfractionActivity
    private lateinit var binding: FragmentVehicleBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as CreateInfractionActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isCreation = it.getBoolean(ARG_IS_CREATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle, container, false)
        initAdapters()
        binding.imgEvidence1.setOnClickListener(this)
        binding.imgEvidence2.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        return binding.root
    }

    override fun initAdapters() {
        /* Set selected item listeners */
        binding.spnBrandVehicle.onItemSelectedListener = this
        binding.spnSubBrandVehicle.onItemSelectedListener = this
        binding.spnTypeDoc.onItemSelectedListener = this
        binding.spnType.onItemSelectedListener = this
        binding.spnIssuedIn.onItemSelectedListener = this
        binding.spnIdentifierDoc.onItemSelectedListener = this
        binding.spnColor.onItemSelectedListener = this
        /* Set spinners adapters */
        binding.spnBrandVehicle.adapter = iterator.value.getBrandAdapter()
        binding.spnType.adapter = iterator.value.getTypeAdapter()
        binding.spnColor.adapter = iterator.value.getColorAdapter()
        binding.spnIdentifierDoc.adapter = iterator.value.getIdentifierDocAdapter()
        iterator.value.getIssuedInAdapter() // Download catalog from Firebase
        binding.spnTypeDoc.adapter = iterator.value.getTypeDocument()
    }

    override fun onIssuedInReady(adapter: ArrayAdapter<String>) {
        binding.spnIssuedIn.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.imgEvidence1.id -> {

            }
            binding.imgEvidence2.id -> {

            }
            binding.btnSave.id -> {
                activity.stepUp()
                activity.router.value.presentInfractionFragment(Direction.NONE)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.spnBrandVehicle.id -> {
                val idBrand = iterator.value.brandList[p2].id
                binding.spnSubBrandVehicle.adapter = iterator.value.getSubBrandAdapter(idBrand)
            }
        }
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isCreation Parameter 1.
         * @return A new instance of fragment VehicleFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                VehicleFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
