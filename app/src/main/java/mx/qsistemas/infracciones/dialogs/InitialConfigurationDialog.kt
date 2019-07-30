package mx.qsistemas.infracciones.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.dialog_title.view.*
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.DialogInitialConfigurationBinding
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.utils.FS_COL_CITIES
import mx.qsistemas.infracciones.utils.FS_COL_STATES
import mx.qsistemas.infracciones.utils.FS_COL_TERMINALS
import mx.qsistemas.infracciones.utils.Utils
import java.util.*


class InitialConfigurationDialog : DialogFragment(), DialogPresenter, AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    val BBOX = "@_x2Quetza+*qsi9900"
    var listener: InitialConfigurationCallback? = null
    private var states = mutableListOf<GenericCatalog>()
    private var townships = mutableListOf<Townships>()
    private lateinit var binding: DialogInitialConfigurationBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_initial_configuration, container, false)
        binding.dialogTitle.custom_dialog_title.text = getString(R.string.t_initial_configuration)
        binding.spnState.onItemSelectedListener = this
        binding.btnAccept.setOnClickListener(this)
        getStates()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun getStates() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener?.onDialogError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                states = mutableListOf()
                states.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    states.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                binding.spnState.adapter = adapter
            } else {
                states = mutableListOf()
                states.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                binding.spnState.adapter = adapter
            }
        }
    }

    override fun getTownships(state: DocumentReference?) {
        if (state != null) {
            Application.firestore?.collection(FS_COL_CITIES)?.whereEqualTo("reference", state)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    listener?.onDialogError(exception.message
                            ?: Application.getContext().getString(R.string.e_firestore_not_available))
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    townships = mutableListOf()
                    townships.add(Townships("PRX", 0, "Seleccionar...", state, true))
                    val list = mutableListOf<String>()
                    list.add("Seleccionar...")
                    for (document in querySnapshot.documents) {
                        val data = document.toObject(Townships::class.java)!!
                        data.childReference = document.reference
                        list.add(data.value)
                        townships.add(data)
                    }
                    val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    binding.spnTownship.adapter = adapter
                } else {
                    townships = mutableListOf()
                    townships.add(Townships("PRX", 0, "Seleccionar...", state, true))
                    val list = mutableListOf<String>()
                    list.add("Seleccionar...")
                    val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    binding.spnTownship.adapter = adapter
                }
            }
        } else {
            townships = mutableListOf()
            townships.add(Townships("PRX", 0, "Seleccionar...", state, true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            binding.spnTownship.adapter = adapter
        }
    }

    override fun savePrefix(prefix: String, township: Townships, townKey: String) {
        Application.firestore?.collection(FS_COL_CITIES)?.document(townKey)?.update("counter_prefix", township.counter_prefix + 1)?.addOnCompleteListener { t1 ->
            if (t1.isSuccessful) {
                val map = hashMapOf("city" to township.childReference,
                        "prefix" to prefix,
                        "last_geo" to GeoPoint(0.0, 0.0),
                        "time_geo" to Date(),
                        "android_id" to Utils.getTokenDevice(Application.getContext()),
                        "push_token" to Application.prefs?.loadData(R.string.sp_firebase_token_push, ""))
                val imei = Utils.getImeiDevice(Application.getContext())
                Application.firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.set(map, SetOptions.merge())?.addOnCompleteListener { t2 ->
                    if (t2.isSuccessful) {
                        dismiss()
                        listener?.onConfigurationSuccessful(township.childReference?.id
                                ?: "0", prefix)
                    } else {
                        listener?.onDialogError(Application.getContext().getString(R.string.e_firestore_not_saved))
                    }
                }
            } else {
                listener?.onDialogError(Application.getContext().getString(R.string.e_firestore_not_saved))
            }
        }
        dismiss()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.spnState.id -> {
                getTownships(states[p2].documentReference)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAccept.id -> {
                if (binding.spnState.childCount == 0 || binding.spnTownship.childCount == 0) {
                    listener?.onDialogError(Application.getContext().getString(R.string.e_empty_fields))
                } else if (binding.edtPswConfig.text.toString().trim() != BBOX) {
                    binding.edtPswConfig.error = Application.getContext().getString(R.string.e_pss_incorrect)
                } else {
                    val township = townships[binding.spnTownship.selectedItemPosition]
                    val state = states[binding.spnState.selectedItemPosition]
                    val prefix = township.prefix + (township.counter_prefix + 1)
                    Application.prefs?.saveData(R.string.sp_id_state, state.documentReference?.id
                            ?: "0")
                    Application.prefs?.saveData(R.string.sp_id_township, township.childReference?.id
                            ?: "0")
                    Application.prefs?.saveData(R.string.sp_township_name, township.value)
                    Application.prefs?.saveData(R.string.sp_prefix, prefix)
                    Application.prefs?.saveDataBool(R.string.sp_has_config_prefix, true)
                    savePrefix(prefix, township, townships[binding.spnTownship.selectedItemPosition].childReference?.id
                            ?: "0")
                }
            }
        }
    }
}

interface DialogPresenter {
    fun getStates()
    fun getTownships(state: DocumentReference?)
    fun savePrefix(prefix: String, township: Townships, townKey: String)
}

interface InitialConfigurationCallback {
    fun onDialogError(msg: String)
    fun onConfigurationSuccessful(idTownship: String, prefix: String)
}