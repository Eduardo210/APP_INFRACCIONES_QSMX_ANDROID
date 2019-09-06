package mx.qsistemas.infracciones.modules.create.fr_payer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentPayerBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_IS_CREATION = "is_creation"

class PayerFragment : Fragment() {

    private var isCreation: Boolean = true
    private lateinit var binding: FragmentPayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isCreation = it.getBoolean(ARG_IS_CREATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payer, container, false)
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PayerFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                PayerFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
