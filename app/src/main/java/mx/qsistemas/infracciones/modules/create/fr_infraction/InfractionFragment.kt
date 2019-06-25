package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentInfractionBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity

private const val ARG_IS_CREATION = "is_creation"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InfractionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InfractionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InfractionFragment : Fragment(), InfractionContracts.Presenter, AdapterView.OnItemSelectedListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private var isCreation: Boolean = true
    private val iterator = lazy { InfractionIterator(this) }
    private lateinit var activity: CreateInfractionActivity
    private lateinit var binding: FragmentInfractionBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_infraction, container, false)
        initAdapters()
        return binding.root
    }

    override fun initAdapters() {
        binding.rcvArticles.layoutManager = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        /* Init listener of components*/
        binding.spnArticle.onItemSelectedListener = this
        binding.spnFraction.onItemSelectedListener = this
        binding.spnRetainedDoc.onItemSelectedListener = this
        binding.rdbReferralYes.setOnCheckedChangeListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        /* Init adapters */
        binding.spnArticle.adapter = iterator.value.getArticlesAdapter()
        binding.spnRetainedDoc.adapter = iterator.value.getRetainedDocAdapter()
        binding.spnDisposition.adapter = iterator.value.getDispositionAdapter()
        binding.rcvArticles.adapter = MotivationAdapter()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.spnArticle.id -> {
                binding.spnFraction.adapter = iterator.value.getFractionAdapter(p2)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAdd.id -> {
                if (iterator.value.articlesList[binding.spnArticle.selectedItemPosition].id.toInt() == 0) {
                    onError(getString(R.string.e_invalid_article))
                } else {
                    iterator.value.saveNewArticle(binding.spnArticle.selectedItemPosition,
                            binding.spnFraction.selectedItemPosition)
                    binding.rcvArticles.adapter?.notifyDataSetChanged()
                    binding.spnFraction.setSelection(0)
                    binding.spnArticle.setSelection(0)
                }
            }
            binding.btnNext.id -> {
                activity.stepUp()
                activity.router.value.presentOffenderFragment(Direction.NONE)
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if (p1) {
            binding.txtDispositionTitle.visibility = VISIBLE
            binding.spnDisposition.visibility = VISIBLE
        } else {
            binding.txtDispositionTitle.visibility = GONE
            binding.spnDisposition.visibility = GONE
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
         * @return A new instance of fragment InfractionFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                InfractionFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
