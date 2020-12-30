package mx.qsistemas.infracciones.modules.main

import mx.qsistemas.infracciones.helpers.activity_helper.Direction

class MainContracts {

    interface Presenter {
        fun enableHighAccuracyGps()
        fun onError(msg: String)
        fun onSessionClosed()
    }

    interface Iterator {
    }

    interface Router {
        fun presentInfractionList(direction: Direction)
        fun presentNewInfraction()
        fun presentSearchInfraction()
        fun presentDetailInfraction(folio: String)
        fun presentLogIn()
        fun presentLocationSettings()
    }
}