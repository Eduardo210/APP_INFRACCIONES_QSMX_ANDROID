package mx.qsistemas.infracciones.modules.create

import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.fr_infraction.InfractionFragment
import mx.qsistemas.infracciones.modules.create.fr_offender.OffenderFragment
import mx.qsistemas.infracciones.modules.create.fr_vehicle.VehicleFragment

class CreateInfractionRouter(val activity: ActivityHelper) : CreateInfractionContracts.Router {
    override fun presentVehicleFragment(direction: Direction) {
        activity.loadFragment(VehicleFragment.newInstance(true), R.id.container_infraction, direction, false)
    }

    override fun presentInfractionFragment(direction: Direction) {
        activity.loadFragment(InfractionFragment.newInstance(true), R.id.container_infraction, direction, false)
    }

    override fun presentOffenderFragment(isNewInfraction: Boolean, direction: Direction) {
        activity.loadFragment(OffenderFragment.newInstance(isNewInfraction), R.id.container_infraction, direction, false)
    }
}