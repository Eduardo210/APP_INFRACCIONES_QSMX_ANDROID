package mx.qsistemas.infracciones.modules.create.fr_offender

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentOffenderBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity

private const val ARG_IS_CREATION = "is_creation"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OffenderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OffenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OffenderFragment : Fragment(), OffenderContracts.Presenter, CompoundButton.OnCheckedChangeListener {
    private var isCreation: Boolean = true
    private lateinit var binding: FragmentOffenderBinding
    private lateinit var activity: CreateInfractionActivity

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offender, container, false)
        initAdapters()
        return binding.root
    }

    override fun initAdapters() {
        /* Init listeners */
        binding.rdbAbsentYes.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if (p1) {
            binding.lytOffender.root.visibility = GONE
        } else {
            binding.lytOffender.root.visibility = VISIBLE
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
         * @return A new instance of fragment OffenderFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                OffenderFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
