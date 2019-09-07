package mx.qsistemas.infracciones.modules.create.fr_offender

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.databinding.FragmentOffenderBinding
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.singletons.SingletonInfraction

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OffenderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OffenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OffenderFragment : Fragment(), OffenderContracts.Presenter, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener,
        OnClickListener{

    private var isTicketCopy: Boolean = false
    private val iterator = lazy { OffenderIterator(this) }
    private lateinit var binding: FragmentOffenderBinding
    private lateinit var activity: CreateInfractionActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as CreateInfractionActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offender, container, false)
        initAdapters()
        fillFields()
        return binding.root
    }

    override fun initAdapters() {
        /* Init listeners */
        binding.rdbAbsentYes.setOnCheckedChangeListener(this)
        binding.lytOffender.spnState.onItemSelectedListener = this
        binding.lytOffender.spnTownship.onItemSelectedListener = this
        binding.lytOffender.spnZipCode.onItemSelectedListener = this
        binding.lytOffender.spnColony.onItemSelectedListener = this
        binding.lytOffender.spnLicenseType.onItemSelectedListener = this
        binding.lytOffender.spnLicenseIssuedIn.onItemSelectedListener = this
        binding.btnPay.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        if (SingletonInfraction.nameOffender == "Quien") {
            binding.lytOffender.edtOffenderName.doOnTextChanged { text, start, count, after -> SingletonInfraction.nameOffender = text?.trim().toString() }
            binding.lytOffender.edtOffenderFln.doOnTextChanged { text, start, count, after -> SingletonInfraction.lastFatherName = text?.trim().toString() }
            binding.lytOffender.edtOffenderMln.doOnTextChanged { text, start, count, after -> SingletonInfraction.lastMotherName = text?.trim().toString() }
        }

        binding.lytOffender.edtOffenderRfc.doOnTextChanged { text, start, count, after -> SingletonInfraction.rfcOffenfer = text?.trim().toString() }
        binding.lytOffender.edtStreet.doOnTextChanged { text, start, count, after -> SingletonInfraction.streetOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderNoExt.doOnTextChanged { text, start, count, after -> SingletonInfraction.noExtOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderNoInt.doOnTextChanged { text, start, count, after -> SingletonInfraction.noIntOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderLicenseNo.doOnTextChanged { text, start, count, after -> SingletonInfraction.noLicenseOffender = text?.trim().toString() }
        /* Init adapters */
        iterator.value.getStatesList()
        iterator.value.getStatesIssuedList()
        iterator.value.getTypeLicenseAdapter()
        iterator.value.getHolidays()
    }

    override fun fillFields() {
        binding.rdbAbsentNo.isChecked = !SingletonInfraction.isPersonAbstent
        binding.lytOffender.edtOffenderName.setText(SingletonInfraction.nameOffender)
        binding.lytOffender.edtOffenderFln.setText(SingletonInfraction.lastFatherName)
        binding.lytOffender.edtOffenderMln.setText(SingletonInfraction.lastMotherName)
        binding.lytOffender.edtOffenderRfc.setText(SingletonInfraction.rfcOffenfer)
        binding.lytOffender.edtStreet.setText(SingletonInfraction.streetOffender)
        binding.lytOffender.edtOffenderNoExt.setText(SingletonInfraction.noExtOffender)
        binding.lytOffender.edtOffenderNoInt.setText(SingletonInfraction.noIntOffender)
        binding.lytOffender.edtOffenderLicenseNo.setText(SingletonInfraction.noLicenseOffender)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        SingletonInfraction.isPersonAbstent = p1
        if (p1) {
            binding.lytOffender.root.visibility = GONE
            binding.lytOffender.edtOffenderName.setText("Quien")
            binding.lytOffender.edtOffenderFln.setText("Resulte")
            binding.lytOffender.edtOffenderMln.setText("Responsable")
        } else {
            binding.lytOffender.root.visibility = VISIBLE
            binding.lytOffender.edtOffenderName.setText("")
            binding.lytOffender.edtOffenderFln.setText("")
            binding.lytOffender.edtOffenderMln.setText("")
        }
    }

    override fun onStatesReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnState.adapter = adapter
        binding.lytOffender.spnState.setSelection(iterator.value.getPositionState(SingletonInfraction.stateOffender))
    }

    override fun onTownshipsReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnTownship.adapter = adapter
        binding.lytOffender.spnTownship.setSelection(iterator.value.getPositionTownship(SingletonInfraction.townshipOffender))
    }

    override fun onZipCodesReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnZipCode.adapter = adapter
        binding.lytOffender.spnZipCode.setSelection(iterator.value.getPositionZipCode(SingletonInfraction.zipCodeOffender))
    }

    override fun onStatesIssuedReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnLicenseIssuedIn.adapter = adapter
        binding.lytOffender.spnLicenseIssuedIn.setSelection(iterator.value.getPositionStateLicense(SingletonInfraction.licenseIssuedInOffender))
    }

    override fun onColoniesReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnColony.adapter = adapter
        binding.lytOffender.spnColony.setSelection(iterator.value.getPositionColony(SingletonInfraction.colonnyInfraction))
    }

    override fun onTypeLicenseReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnLicenseType.adapter = adapter
        binding.lytOffender.spnLicenseType.setSelection(iterator.value.getPositionTypeLicense(SingletonInfraction.typeLicenseOffender))
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
        if (msg == getString(R.string.pt_e_print_other_error)) {
            val builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_print_ticket), getString(R.string.w_options_reprint), activity
            )
            builder.setPositiveButton("Boleta") { _, _ ->
                activity.showLoader(getString(R.string.l_preparing_printer))
                iterator.value.printTicket(activity)
            }
            builder.show()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.lytOffender.spnState.id -> {
                SingletonInfraction.stateOffender = iterator.value.statesList[p2]
                iterator.value.getTownshipsList(SingletonInfraction.stateOffender.documentReference)
            }
            binding.lytOffender.spnTownship.id -> {
                SingletonInfraction.townshipOffender = iterator.value.townshipList[p2]
                iterator.value.getZipCodesList(SingletonInfraction.townshipOffender.childReference)
            }
            binding.lytOffender.spnZipCode.id -> {
                SingletonInfraction.zipCodeOffender = iterator.value.zipCodesList[p2]
                iterator.value.getColoniesList(SingletonInfraction.zipCodeOffender.childReference)
            }
            binding.lytOffender.spnColony.id -> {
                SingletonInfraction.colonyOffender = iterator.value.coloniesList[p2]
            }
            binding.lytOffender.spnLicenseType.id -> {
                SingletonInfraction.typeLicenseOffender = iterator.value.licenseTypeList[p2]
            }
            binding.lytOffender.spnLicenseIssuedIn.id -> {
                SingletonInfraction.licenseIssuedInOffender = iterator.value.stateIssuedLicenseList[p2]
            }
        }
    }

    private fun thereIsAnOffender(): Boolean {
        if (SingletonInfraction.nameOffender != "Quien") {
            if (SingletonInfraction.lastFatherName != "Resulte") {
                if (SingletonInfraction.lastMotherName != "Responsable") {
                    return true
                }
            }
        }
        return false
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnSave.id -> {
                if (validFields()) {
                    Log.d("OFFENDER", thereIsAnOffender().toString())
                    if (thereIsAnOffender()) {
                        val builder = AlertDialogHelper.getGenericBuilder(
                                getString(R.string.w_dialog_title), getString(R.string.w_want_to_pay), activity
                        )
                        builder.setPositiveButton("Aceptar") { _, _ ->
                            //iterator.value.saveTownship()
                            activity.stepUp()
                            activity.router.value.presentPayerFragment(true, Direction.NONE)
                        }
                        builder.setNegativeButton("Cancelar") { dialog, _ ->
                            val builder2 = AlertDialogHelper.getGenericBuilder(
                                    getString(R.string.w_dialog_title), getString(R.string.w_verify_printer), activity
                            )
                            builder2.setPositiveButton("Aceptar") { _, _ ->
                                iterator.value.saveData(true)
                            }
                            builder2.show()
                        }
                        builder.show()
                    } else {
                        val builder2 = AlertDialogHelper.getGenericBuilder(
                                getString(R.string.w_dialog_title), getString(R.string.w_verify_printer), activity
                        )
                        builder2.setPositiveButton("Aceptar") { _, _ ->
                            iterator.value.saveData(true)
                        }
                        builder2.show()
                    }

                }
            }
        }
    }

    override fun onDataSaved() {
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_data_saved), Snackbar.LENGTH_SHORT)
        activity.showLoader(getString(R.string.l_preparing_printer))
        iterator.value.printTicket(activity)
    }


    override fun validFields(): Boolean {
        var isValid = true
        if (!SingletonInfraction.isPersonAbstent) {
            when {
                SingletonInfraction.nameOffender.isEmpty() -> {
                    isValid = false
                    onError(getString(R.string.e_name_offender))
                }
                SingletonInfraction.lastFatherName.isEmpty() -> {
                    isValid = false
                    onError(getString(R.string.e_last_father_offender))
                }
                SingletonInfraction.lastMotherName.isEmpty() -> {
                    isValid = false
                    onError(getString(R.string.e_last_mother_offender))
                }
                else -> {
                    if (isDirectionAnswered()) {
                        when {
                            SingletonInfraction.stateOffender.documentReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_state_offender))
                                1
                            }
                            SingletonInfraction.townshipOffender.childReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_township_offender))
                            }
                            SingletonInfraction.zipCodeOffender.childReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_zip_code))
                            }
                            SingletonInfraction.colonyOffender.childReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_colony_offender))
                            }
                            SingletonInfraction.streetOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_street_offender))
                            }
                            SingletonInfraction.noExtOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_no_ext_offender))
                            }
                        }
                    }
                    if (isLicenseAnswered()) {
                        when {
                            SingletonInfraction.noLicenseOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_no_license_offender))
                            }
                            SingletonInfraction.typeLicenseOffender.documentReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_type_license_offender))
                            }
                            SingletonInfraction.licenseIssuedInOffender.documentReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_issued_license_offender))
                            }
                        }
                    }
                }
            }
        }
        return isValid
    }

    override fun isDirectionAnswered(): Boolean {
        var isAnswered = false
        when {
            SingletonInfraction.stateOffender.documentReference != null -> isAnswered = true
            SingletonInfraction.townshipOffender.childReference != null -> isAnswered = true
            SingletonInfraction.zipCodeOffender.childReference != null -> isAnswered = true
            SingletonInfraction.colonyOffender.childReference != null -> isAnswered = true
            SingletonInfraction.streetOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.noExtOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.noIntOffender.isNotEmpty() -> isAnswered = true
        }
        return isAnswered
    }

    override fun isLicenseAnswered(): Boolean {
        var isAnswered = false
        when {
            SingletonInfraction.noLicenseOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.licenseIssuedInOffender.documentReference != null -> isAnswered = true
            SingletonInfraction.typeLicenseOffender.documentReference != null -> isAnswered = true
        }
        return isAnswered
    }

    override fun onTicketPrinted() {
        if (!isTicketCopy) {
            val builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_reprint_ticket), getString(R.string.w_want_to_reprint_ticket), activity
            )
            builder.setPositiveButton("Sí") { _, _ ->
                isTicketCopy = true
                activity.showLoader(getString(R.string.l_preparing_printer))
                iterator.value.printTicket(activity)
            }
            builder.show()
        } else {
            SingletonInfraction.cleanSingleton()
            Alarms()
            activity.finish()
        }
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
        fun newInstance() =
                OffenderFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

}
