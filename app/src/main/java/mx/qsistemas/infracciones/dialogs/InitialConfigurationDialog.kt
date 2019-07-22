package mx.qsistemas.infracciones.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.dialog_title.view.*
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.DialogInitialConfigurationBinding
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.utils.FS_COL_STATES
import mx.qsistemas.infracciones.utils.FS_COL_TERMINALS
import mx.qsistemas.infracciones.utils.FS_COL_TOWNSHIPS
import mx.qsistemas.infracciones.utils.Utils
import java.text.SimpleDateFormat
import java.util.*


class InitialConfigurationDialog : DialogFragment(), DialogPresenter, AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    var listener: InitialConfigurationCallback? = null
    private var states = mutableListOf<States>()
    private var townships = mutableListOf<Townships>()
    private lateinit var binding: DialogInitialConfigurationBinding
    private var townshipKeys = mutableListOf<String>()

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
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("enable", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener?.onDialogError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val list = mutableListOf<String>()
                for (document in snapshot.documents) {
                    val data = document.toObject(States::class.java)!!
                    list.add(data.value)
                    states.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                binding.spnState.adapter = adapter
            }
        }
    }

    override fun getTownships(idState: Int) {
        Application.firestore?.collection(FS_COL_TOWNSHIPS)?.whereEqualTo("id_state", idState)?.whereEqualTo("enable", true)?.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                listener?.onDialogError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                val list = mutableListOf<String>()
                townshipKeys = mutableListOf()
                townships = mutableListOf()
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Townships::class.java)!!
                    list.add(data.value)
                    townships.add(data)
                    townshipKeys.add(document.id)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                binding.spnTownship.adapter = adapter
            }
        }
    }

    override fun savePrefix(prefix: String, township: Townships, townKey: String) {
        Application.firestore?.collection(FS_COL_TOWNSHIPS)?.document(townKey)?.update("counter_prefix", township.counter_prefix + 1)?.addOnCompleteListener { t1 ->
            if (t1.isSuccessful) {
                val map = hashMapOf("id_township" to township.id_town,
                        "prefix" to prefix,
                        "last_synch" to Date(946688401000),
                        "last_geo" to GeoPoint(0.0, 0.0),
                        "time_geo" to Date(),
                        "android_id" to Utils.getTokenDevice(Application.getContext()),
                        "push_token" to Application.prefs?.loadData(R.string.sp_firebase_token_push, ""))
                val imei = Utils.getImeiDevice(Application.getContext())
                Application.firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.set(map, SetOptions.merge())?.addOnCompleteListener { t2 ->
                    if (t2.isSuccessful) {
                        dismiss()
                        listener?.onConfigurationSuccessful(township.id_town, prefix)
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
                getTownships(states[p2].id)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAccept.id -> {
                if (binding.spnState.childCount == 0 || binding.spnTownship.childCount == 0) {
                    listener?.onDialogError(Application.getContext().getString(R.string.e_empty_fields))
                } else {
                    val township = townships[binding.spnTownship.selectedItemPosition]
                    val state = states[binding.spnState.selectedItemPosition]
                    val prefix = township.prefix + (township.counter_prefix + 1)
                    Application.prefs?.saveDataInt(R.string.sp_id_state, state.id)
                    Application.prefs?.saveDataInt(R.string.sp_id_township, township.id_town)
                    Application.prefs?.saveData(R.string.sp_township_name, township.value)
                    Application.prefs?.saveData(R.string.sp_prefix, prefix)
                    Application.prefs?.saveDataBool(R.string.sp_has_config_prefix, true)
                    Application.prefs?.saveData(R.string.sp_last_synch, SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(946688401000))) // Date: 01/01/2000 01:00:01
                    savePrefix(prefix, township, townshipKeys[binding.spnTownship.selectedItemPosition])
                }
            }
        }
    }
}

interface DialogPresenter {
    fun getStates()
    fun getTownships(idState: Int)
    fun savePrefix(prefix: String, township: Townships, townKey: String)
}

interface InitialConfigurationCallback {
    fun onDialogError(msg: String)
    fun onConfigurationSuccessful(idTownship: Int, prefix: String)
}