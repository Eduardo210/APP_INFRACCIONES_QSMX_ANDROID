package mx.qsistemas.infracciones.modules.main

import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R


class MainIterator(private val presenter: MainContracts.Presenter): MainContracts.Iterator {

    override fun closeSession(){
        Application.prefs?.saveDataBool(R.string.sp_has_session, false)
    }
}