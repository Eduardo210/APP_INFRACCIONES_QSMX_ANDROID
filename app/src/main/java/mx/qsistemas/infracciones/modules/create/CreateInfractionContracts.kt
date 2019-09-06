package mx.qsistemas.infracciones.modules.create

import mx.qsistemas.infracciones.helpers.activity_helper.Direction

class CreateInfractionContracts {
    interface Presenter {
        fun onError(msg: String)
        fun stepUp()
        fun stepDown()
    }

    interface Iterator {

    }

    interface Router {
        fun presentVehicleFragment(direction: Direction)
        fun presentInfractionFragment(direction: Direction)
        fun presentOffenderFragment(direction: Direction)
        fun presentPayerFragment(isNewInfraction: Boolean, direction: Direction)
    }
}