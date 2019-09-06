package mx.qsistemas.infracciones.modules.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityCreateInfractionBinding
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.fr_infraction.InfractionFragment
import mx.qsistemas.infracciones.modules.create.fr_offender.OffenderFragment
import mx.qsistemas.infracciones.modules.create.fr_payer.PayerFragment
import mx.qsistemas.infracciones.modules.create.fr_vehicle.VehicleFragment
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.EXTRA_OPTION_INFRACTION
import mx.qsistemas.infracciones.utils.RC_INTENT_CAMERA_EV1
import mx.qsistemas.infracciones.utils.RC_INTENT_CAMERA_EV2

const val OPTION_CREATE_INFRACTION = 1
const val OPTION_UPDATE_INFRACTION = 2

class CreateInfractionActivity : ActivityHelper(), CreateInfractionContracts.Presenter, View.OnClickListener {

    private var isNewInfraction = false
    private lateinit var binding: ActivityCreateInfractionBinding
    val router = lazy { CreateInfractionRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_infraction)
        if (intent.extras != null) {
            when (intent.getIntExtra(EXTRA_OPTION_INFRACTION, 0)) {
                OPTION_CREATE_INFRACTION -> {
                    isNewInfraction = true
                    router.value.presentVehicleFragment(Direction.NONE)
                }
                OPTION_UPDATE_INFRACTION -> {
                    for (i in 1..3)
                        stepUp()
                    router.value.presentPayerFragment(isNewInfraction, Direction.NONE)
                    binding.txtTitleInfractionToolbar.text = "Actualizar"
                    binding.txtSubtitleInfractionToolbar.text = "Actualización de datos"
                }
            }
        }
        binding.btnBackInfraction.setOnClickListener(this)
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_SHORT)
    }

    override fun stepUp() {
        binding.stepCounter.nextStep()
    }

    override fun stepDown() {
        binding.stepCounter.backStep()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnBackInfraction.id -> {
                onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        SingletonInfraction.cleanSingleton()
        SingletonTicket.cleanData()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(binding.containerInfraction.id)
        when (fragment) {
            is VehicleFragment -> {
                val builder = AlertDialogHelper.getGenericBuilder(
                        getString(R.string.w_dialog_title), getString(R.string.w_exit_without_save), this
                )
                builder.setPositiveButton("Aceptar") { _, _ ->
                    SingletonInfraction.cleanSingleton()
                    super.onBackPressed()
                }
                builder.setNegativeButton("Cancelar") { _, _ -> }
                builder.show()
            }
            is InfractionFragment -> {
                stepDown()
                router.value.presentVehicleFragment(Direction.NONE)
            }
            is OffenderFragment -> {
                stepDown()
                router.value.presentInfractionFragment(Direction.NONE)

            }
            is PayerFragment -> {
                if (isNewInfraction) {
                    stepDown()
                    router.value.presentOffenderFragment(Direction.NONE)
                } else {
                    val builder = AlertDialogHelper.getGenericBuilder(
                            getString(R.string.w_dialog_title), getString(R.string.w_exit_without_save), this
                    )
                    builder.setPositiveButton("Aceptar") { _, _ ->
                        super.onBackPressed()
                    }
                    builder.setNegativeButton("Cancelar") { _, _ -> }
                    builder.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_INTENT_CAMERA_EV1 || requestCode == RC_INTENT_CAMERA_EV2) {
            if (resultCode == Activity.RESULT_OK) {
                val fm = supportFragmentManager.findFragmentById(binding.containerInfraction.id)
                fm!!.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
