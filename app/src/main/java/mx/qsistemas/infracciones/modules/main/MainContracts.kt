package mx.qsistemas.infracciones.modules.main

import mx.qsistemas.infracciones.helpers.activity_helper.Direction

class MainContracts {

    interface Presenter {
        fun enableHighAccuracyGps()
    }

    interface Iterator {
        fun closeSession()
    }

    interface Router {
        fun presentInfractionList(direction: Direction)
        fun presentNewInfraction()
        fun presentDetailInfraction(folio: String)
        fun presentLogIn()
    }
}