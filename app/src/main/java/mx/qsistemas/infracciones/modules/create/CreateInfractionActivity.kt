package mx.qsistemas.infracciones.modules.create

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityCreateInfractionBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.utils.EXTRA_OPTION_INFRACTION

const val OPTION_CREATE_INFRACTION = 1
const val OPTION_UPDATE_INFRACTION = 2

class CreateInfractionActivity : ActivityHelper(), CreateInfractionContracts.Presenter {

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
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_SHORT)
    }
}
