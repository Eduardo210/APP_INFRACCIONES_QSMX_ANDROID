package mx.qsistemas.infracciones.modules.create

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityCreateInfractionBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.fr_infraction.InfractionFragment
import mx.qsistemas.infracciones.modules.create.fr_offender.OffenderFragment
import mx.qsistemas.infracciones.modules.create.fr_vehicle.VehicleFragment
import mx.qsistemas.infracciones.utils.EXTRA_OPTION_INFRACTION

const val OPTION_CREATE_INFRACTION = 1
const val OPTION_UPDATE_INFRACTION = 2

class CreateInfractionActivity : ActivityHelper(), CreateInfractionContracts.Presenter, View.OnClickListener {

    private lateinit var binding: ActivityCreateInfractionBinding
    val router = lazy { CreateInfractionRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_infraction)
        if (intent.extras != null) {
            when (intent.getIntExtra(EXTRA_OPTION_INFRACTION, 0)) {
                OPTION_CREATE_INFRACTION -> {
                    router.value.presentVehicleFragment(Direction.NONE)
                }
                OPTION_UPDATE_INFRACTION -> {
                    router.value.presentOffenderFragment(Direction.NONE)
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

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(binding.containerInfraction.id)
        when (fragment) {
            is VehicleFragment -> {
                super.onBackPressed()
            }
            is InfractionFragment -> {
                stepDown()
                router.value.presentVehicleFragment(Direction.BACK)
            }
            is OffenderFragment -> {
                stepDown()
                router.value.presentInfractionFragment(Direction.BACK)
            }
        }
    }
}
